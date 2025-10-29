package ar.utn.ba.ddsi.providers;


import ar.utn.ba.ddsi.models.dto.Rol;
import ar.utn.ba.ddsi.models.dto.external.UserRolesDTO;
import ar.utn.ba.ddsi.services.IRootService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final IRootService externalAuthService;

    public CustomAuthProvider(IRootService externalAuthService) {
        this.externalAuthService = externalAuthService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName(); // Asumiendo que el principal (getName()) ahora es el email
        String password = authentication.getCredentials().toString();

        try {
            AuthResponseDTO authResponse = externalAuthService.login(email, password);

            if (authResponse == null) {
                throw new BadCredentialsException("Email o contraseña inválidos");
            }

            String accessToken = authResponse.getAccessToken();

            if (accessToken == null || accessToken.isEmpty()) {
                throw new BadCredentialsException("Token de acceso inválido");
            }

            log.info("Buscando roles y permisos del usuario");

            UserRolesDTO userRoles = externalAuthService.getRole(authResponse.getAccessToken());
            Rol rol = userRoles.getRole();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));

            return new UsernamePasswordAuthenticationToken(email, authResponse, authorities);
        } catch (RuntimeException e) {
            throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
