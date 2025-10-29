package ar.utn.ba.ddsi.config;

import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

    AuthResponseDTO authResponse = (AuthResponseDTO) authentication.getCredentials();

    HttpSession session = request.getSession();

    session.setAttribute("accessToken", authResponse.getAccessToken());
    session.setAttribute("refreshToken", authResponse.getRefreshToken());
    session.setAttribute("email", authentication.getName());

    String rol = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .findFirst()
        .orElse("ROLE_USER");
    session.setAttribute("role", rol.replace("ROLE_", ""));

    response.sendRedirect("/main");
  }
}