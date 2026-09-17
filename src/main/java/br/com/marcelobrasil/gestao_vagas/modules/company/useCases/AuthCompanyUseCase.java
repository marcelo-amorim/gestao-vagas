package br.com.marcelobrasil.gestao_vagas.modules.company.useCases;

import br.com.marcelobrasil.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.marcelobrasil.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.marcelobrasil.gestao_vagas.modules.company.repositories.CompanyRepository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

@Service
public class AuthCompanyUseCase {
    @Value("${security.token.secret.company}")
    private String SECRET_KEY;

    @Autowired private CompanyRepository companyRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO)
            throws AuthenticationException {
        var company =
                this.companyRepository
                        .findByUsername(authCompanyDTO.getUsername())
                        .orElseThrow(
                                () -> {
                                    throw new UsernameNotFoundException(
                                            "Invalid username or password.");
                                });

        var passwordMatches =
                this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationException("Invalid username or password.");
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var roles = Arrays.asList("COMPANY");

        var token =
                JWT.create()
                        .withIssuer("gestao-vagas")
                        .withSubject(company.getId().toString())
                        .withClaim("roles", roles)
                        .withExpiresAt(expiresIn)
                        .sign(algorithm);

        var authCompanyResponseDTO =
                AuthCompanyResponseDTO.builder()
                        .access_token(token)
                        .expires_in(expiresIn.toEpochMilli())
                        .roles(roles)
                        .build();

        return authCompanyResponseDTO;
    }
}
