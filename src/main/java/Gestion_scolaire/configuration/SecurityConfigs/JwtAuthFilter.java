package Gestion_scolaire.configuration.SecurityConfigs;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.RefreshToken;
import Gestion_scolaire.Services.Admin_service;
import Gestion_scolaire.configuration.GestionException.ApiErrorResponse;
import Gestion_scolaire.configuration.NoteFundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {


    @Autowired
    private Admin_service adminService;

    @Autowired
    private JwtService jwtService;



    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Request URL: " + request.getRequestURL());
        String authHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();
        System.out.println("Path: " + path);

        // Ignorer la validation JWT pour les endpoints spécifiques
        if (path.startsWith("/Auth/login") || path.startsWith("/Auth/refresh-token") || path.startsWith("/static/") || path.startsWith("/assets/") || path.startsWith("/js/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = authHeader.substring(7);
        String username = null;
        try {
            username = jwtService.extractUsername(token);

            Admin admin = adminService.getByEmail(username);

            RefreshToken refreshToken = adminService.getRefresh(admin.getIdAdministra());
            System.out.println("__________________________________________Token expired____________________________________" + refreshToken.getToken());


            // Essaye d'extraire le nom d'utilisateur depuis le token
        } catch (ExpiredJwtException e) {

            String email = jwtService.getUsernameFromExpiredToken(token);
            Admin adminExpired = adminService.getByEmail(email);

            RefreshToken refreshTokenError = adminService.getRefresh(adminExpired.getIdAdministra());

            if(jwtService.isTokenExpired(refreshTokenError.getToken())) {
               throw new BadCredentialsException("Bad credentials");

            }else {

                token = jwtService.generateToken(email);
                username = jwtService.extractUsername(token);

                Date refreshTokenExpirationDate = jwtService.extractExpiration(refreshTokenError.getToken());
                long refreshTokenTimeRemaining = refreshTokenExpirationDate.getTime() - System.currentTimeMillis();
                long refreshThreshold = TimeUnit.MINUTES.toMillis(4);
                long refreshTokenTimeRemainingSeconds = TimeUnit.MILLISECONDS.toSeconds(refreshTokenTimeRemaining);

// Calculer la date et l'heure d'expiration
                LocalDateTime expireDateTime = LocalDateTime.now().plusSeconds(refreshTokenTimeRemainingSeconds);


//                System.out.println("__________________________________________Token expired____________________________________" + refreshTokenTimeRemaining);
//
//                System.out.println("__________________________________________Token expired old____________________________________" + refreshThreshold);

                if (refreshTokenTimeRemaining <= refreshThreshold) {
                    System.out.println("____________________________X-Token-Expiring-Soon_________________________________________" + refreshThreshold);

                    // Ajouter un en-tête pour informer le client que le token va bientôt expirer
                    response.setHeader("X-Token-Expiring-Soon", "expire date." + expireDateTime);
                }
            }

        } catch (JwtException e) {
//            throw new JwtException(e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Si le nom d'utilisateur est valide et qu'il n'y a pas encore d'authentification dans le contexte
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = adminService.loadUserByUsername(username);

            // Validation du token
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue le filtrage de la requête
        filterChain.doFilter(request, response);
    }


}
