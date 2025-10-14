package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.ServiceCredentialsDTO;
import ar.utn.ba.ddsi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/internal/auth")
public class ServiceTokenController {
  private final JwtUtil jwtUtil;

  @Value("${service.client.id}")
  private String serviceClientId;

  @Value("${service.client.secret}")
  private String serviceClientSecret;

  public ServiceTokenController(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/token")
  public ResponseEntity<Map<String, String>> generateServiceToken(@RequestBody ServiceCredentialsDTO credentials) {
    if (serviceClientId.equals(credentials.getClientId()) && serviceClientSecret.equals(credentials.getClientSecret())) {
      String token = jwtUtil.generarSystemToken();

      return ResponseEntity.ok(Map.of("access_token", token));
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  // Sabemos que implementar un token de sistema sin fecha de expiracion y sin refersh token es potencialmente peligroso
  // pero debido a la complejidad que presenta implementar por ahora decidimos evitarlo

}