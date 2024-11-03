package Gestion_scolaire.Authentification;

import Gestion_scolaire.Models.UsersAbstract;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class Jeton_Auth {


    // Clé de signature. Remplacez-la par une clé sécurisée et robuste.
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    ;

    // Durée de validité du jeton en millisecondes (par exemple, 1 heure)
    private static final long TOKEN_VALIDITY = 3600000;

    // Méthode pour extraire toutes les revendications du jeton
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigInKey())
                .build().parseClaimsJws(token).getBody();

    }


    private Key getSigInKey(){
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }
//    ---------------------------------------------------------
    // Méthode pour générer un jeton JWT à partir des informations de l'utilisateur
    public String generateToken(Object userDetails) {
        // Générer la clé de signature
        Key signingKey =  Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setSubject(userDetails.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(signingKey)
                .compact();
    }
}
