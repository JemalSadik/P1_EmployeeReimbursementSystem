package com.revature.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final String SECRET = System.getenv("SECRET_KEY");
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateToken(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutes expiry
                .sign(algorithm);
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim("roles").asList(String.class);
    }

    public Date getExpiresAt(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getExpiresAt();
    }

    public boolean validateToken(String token, String username) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(username)
                    .build(); // Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject().equals(username) && jwt.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            // TODO: Implement logging
            return false;
        }
    }

}
