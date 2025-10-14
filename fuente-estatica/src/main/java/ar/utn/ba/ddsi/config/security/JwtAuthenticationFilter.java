package ar.utn.ba.ddsi.config.security;

import ar.utn.ba.ddsi.commons.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;

    @Autowired
    public JwtAuthenticationFilter(@Value("${secret.string}") String secretString) {
        this.jwtValidator = new JwtValidator(secretString);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); // Saca "Bearer"
        System.out.println("Token recibido para validar: '" + token + "'");

        jwtValidator.getAuthentication(token).ifPresent(authentication -> {
            authentication.getAuthorities().forEach(grantedAuthority -> {
                System.out.println(grantedAuthority.getAuthority());
            });
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });

        filterChain.doFilter(request, response);
    }
}