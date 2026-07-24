package br.com.marcelobrasil.gestao_vagas.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTProvider {
    @Value("${security.token.secret.company}")
    private String SECRET_KEY;

    public DecodedJWT validateToken(String token) {
        token = token.replace("Bearer ", "");

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        try {
            var decodedToken = JWT.require(algorithm).build().verify(token);
            return decodedToken;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
