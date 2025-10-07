package ar.utn.ba.ddsi.providers;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;
import ar.utn.ba.ddsi.services.impl.RootService;
import ar.utn.ba.ddsi.utils.JwtKeyLoader;

public class CustomAuthProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final RootService externalAuthService;

    public CustomAuthProvider(RootService externalAuthService) {
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
            log.info("Parseando Token");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtKeyLoader.loadPublicKey())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            log.info("Email logueado! Configurando sesión...");
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            request.getSession().setAttribute("accessToken", authResponse.getAccessToken());
            request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
            request.getSession().setAttribute("email", email);



        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
