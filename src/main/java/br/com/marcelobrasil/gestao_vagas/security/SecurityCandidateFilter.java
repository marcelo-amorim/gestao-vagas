package br.com.marcelobrasil.gestao_vagas.security;

import br.com.marcelobrasil.gestao_vagas.providers.JWTCandidateProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityCandidateFilter extends OncePerRequestFilter {

    @Autowired private JWTCandidateProvider jwtCandidateProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // SecurityContextHolder.getContext().setAuthentication(null);

        if (request.getRequestURI().startsWith("/candidate")) {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null) {
                var validatedToken = this.jwtCandidateProvider.validateToken(authorizationHeader);

                if (validatedToken == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                var roles = validatedToken.getClaim("roles").asList(Object.class);
                var grants =
                        roles.stream()
                                .map(
                                        role ->
                                                new SimpleGrantedAuthority(
                                                        "ROLE_" + role.toString().toUpperCase()))
                                .toList();

                request.setAttribute("candidate_id", validatedToken.getSubject());
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                validatedToken.getSubject(), null, grants);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
