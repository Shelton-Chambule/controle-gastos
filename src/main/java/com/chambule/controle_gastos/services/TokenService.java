package com.chambule.controle_gastos.services;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.chambule.controle_gastos.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TokenService {

    @Value("${api.controle.gastos}")
    private String secret;

    public String generateToken(User user) {
        log.info("Generating token for login='{}'", user.getLogin());
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("controle.gastos")
                    .withSubject(user.getLogin())
                    .withExpiresAt(expireToken())
                    .sign(algorithm);
            log.debug("Token generated successfully for login='{}'", user.getLogin());
            return token;
        } catch (JWTCreationException exception) {
            log.error("Failed to generate token for login='{}'", user.getLogin(), exception);
            throw new RuntimeException("Error while generate token!");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer("controle.gastos")
                    .build()
                    .verify(token)
                    .getSubject();
            log.debug("Token validated successfully for login='{}'", subject);
            return subject;
        } catch (JWTVerificationException exception) {
            log.warn("Token validation failed: {}", exception.getMessage());
            return null;
        }
    }

    private Instant expireToken() {
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}