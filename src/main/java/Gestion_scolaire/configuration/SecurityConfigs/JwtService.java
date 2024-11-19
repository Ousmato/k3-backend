package Gestion_scolaire.configuration.SecurityConfigs;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.RefreshToken;
import Gestion_scolaire.Services.Admin_service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Autowired
    private Admin_service adminService;

    // Replace this with a secure key in a real application, ideally fetched from environment variables
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // Generate token with given user name
    public String generateToken(String userName) {
//        System.out.println("---------------------------------email-------------------" + userName);
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    public String generateNewAccessToken(String rftoken) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, rftoken);
    }

    // Generate token with given user name
    public String generateRefreshToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, userName);
    }

    // Create a JWT token with specified claims and subject (user name)
    public String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // Token valid for 1 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Map<String, Object> claims, String userName) {
//        System.out.println("------------------claims---------------" + claims);
//        System.out.println("------------------username---------------" + userName);

        // Définir la date d'expiration à 7 jours à partir de maintenant
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000); // 7 jours en millisecondes

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate) // Token valid for 1 semaine
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get the signing key for JWT token
    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);

        return Keys.hmacShaKeyFor(keyBytes);
    }



    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build().parseClaimsJws(token).getBody();
    }

    // Check if the token is expired
    public Boolean isTokenExpired (String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken (String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromExpiredToken(String token) {
        try {
            // Utilisez la méthode pour parser le token sans valider la signature (le token peut être expiré)
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSignKey()).build()  // Utilisez la clé de signature
                    .parseClaimsJws(token);  // Cela peut lever une exception si le token est invalide

            Claims claims = claimsJws.getBody();
            // Récupérez le nom d'utilisateur depuis les claims (cela dépend de la structure de votre token)
            return claims.getSubject();  // 'subject' est souvent utilisé pour stocker le nom d'utilisateur
        } catch (ExpiredJwtException e) {
            // Si le token est expiré, vous pouvez toujours récupérer les données dans le payload
            Claims claimsJws = e.getClaims();
            return claimsJws.getSubject();  // 'subject' est souvent le nom d'utilisateur
        } catch (JwtException e) {
            // Le token est invalide ou mal formé
            throw new RuntimeException("Invalid JWT token");
        }
    }
    }

