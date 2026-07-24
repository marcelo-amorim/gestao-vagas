package br.com.marcelobrasil.gestao_vagas.security;

import br.com.marcelobrasil.gestao_vagas.providers.JWTProvider;

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
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Clear the security context at the beginning of each request
        // SecurityContextHolder.getContext().setAuthentication(null);

        if (request.getRequestURI().startsWith("/company")) {
            // Get the Authorization header from the request
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null) {
                var validatedToken = jwtProvider.validateToken(authorizationHeader);

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

                request.setAttribute("company_id", validatedToken.getSubject());
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(validatedToken, null, grants);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
