package Gestion_scolaire.configuration.SecurityConfigs;

import Gestion_scolaire.Services.Admin_service;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {


    @Autowired
    private Admin_service adminService;

    @Autowired
    private JwtService jwtService;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request URI: " + request.getRequestURI());
        String authHeader = request.getHeader("Authorization");

        // Ignorer la validation JWT pour l'endpoint de connexion
        if (request.getRequestURI().startsWith("/Auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Vérifiez si l'en-tête Authorization est présent et commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraction du token après "Bearer "
        String token = authHeader.substring(7);
        String username = null;

        try {
            username = jwtService.extractUsername(token); // Extrait le nom d'utilisateur
            System.out.println("Username extracted from token: " + username);
        } catch (JwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
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
