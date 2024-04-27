package com.revature.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // TODO: Handle the secret key more securely (env variables?)
    private static final String SECRET = "secret_key"; // You should secure this key
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutes expiry
                .sign(algorithm);
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(username)
                    .build(); // Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            // TODO: verify token is valid
            return jwt.getSubject().equals(username);
        } catch (JWTVerificationException e) {
            // TODO: Implement logging
            return false;
        }
    }

}
