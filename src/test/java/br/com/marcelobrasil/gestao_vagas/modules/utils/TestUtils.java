package br.com.marcelobrasil.gestao_vagas.modules.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class TestUtils {
    public static String objectToJSON(Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateToken(UUID companyId, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var token =
                JWT.create()
                        .withIssuer("gestao-vagas")
                        .withSubject(companyId.toString())
                        .withClaim("roles", Arrays.asList("COMPANY"))
                        .withExpiresAt(expiresIn)
                        .sign(algorithm);

        return token.toString();
    }
}
