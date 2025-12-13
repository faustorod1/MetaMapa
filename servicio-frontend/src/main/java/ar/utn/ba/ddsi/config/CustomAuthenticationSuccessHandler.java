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

    Object details = authentication.getDetails();
    AuthResponseDTO authResponse = null;

    if (details instanceof AuthResponseDTO) {
      authResponse = (AuthResponseDTO) details;
    }
    if (authResponse == null) {
      response.sendRedirect("/login?error=true&message=auth_failed");
      return;
    }

    HttpSession session = request.getSession();

    session.setAttribute("accessToken", authResponse.getAccessToken());
    session.setAttribute("refreshToken", authResponse.getRefreshToken());
    session.setAttribute("email", authentication.getName());

    String rol = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse("ROLE_USER");
    session.setAttribute("role", rol.replace("ROLE_", ""));

    String requestedView = request.getParameter("requestedView");
    System.out.println(requestedView);
    if (requestedView != null && !requestedView.isEmpty()) {
      response.sendRedirect(requestedView);
    } else {
      response.sendRedirect("/main");
    }
  }
}