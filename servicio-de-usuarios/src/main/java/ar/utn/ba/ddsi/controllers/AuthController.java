package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.AuthResponseDTO;
import ar.utn.ba.ddsi.models.dto.RefreshRequestDTO;
import ar.utn.ba.ddsi.models.dto.TokenResponseDTO;
import ar.utn.ba.ddsi.models.exceptions.NotFoundException;
import ar.utn.ba.ddsi.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.utn.ba.ddsi.services.LoginService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            loginService.autenticarUsuario(username, password);

            String accessToken = loginService.generarAccessToken(username);
            String refreshToken = loginService.generarRefreshToken(username);

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
    
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequestDTO request) {
        try {
            String username = JwtUtil.validarToken(request.getRefreshToken());

            // Validar que el token sea de tipo refresh
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getKey())
                    .build()
                    .parseClaimsJws(request.getRefreshToken())
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().build();
            }

            String newAccessToken = JwtUtil.generarAccessToken(username);
            TokenResponseDTO response = new TokenResponseDTO(newAccessToken, request.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



}
