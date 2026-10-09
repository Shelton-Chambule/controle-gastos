package com.chambule.controle_gastos.services;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.chambule.controle_gastos.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${api.secret.key}")
    private String secret;

    public String generateToken(User user){

        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
                String token = JWT.create()
                        .withIssuer("controle.gastos")  // name of the application
                        .withSubject(user.getLogin())
                        .withExpiresAt(expireToken())
                        .sign(algorithm);
                return  token;
        }catch (JWTCreationException exception){
            throw  new RuntimeException("Error while generate token!");
        }
    }

    public  String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            return "";
        }
    }

    private Instant expireToken(){
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}
