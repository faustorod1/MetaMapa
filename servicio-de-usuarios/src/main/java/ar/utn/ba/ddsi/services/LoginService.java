package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.entities.Usuario;
import ar.utn.ba.ddsi.models.exceptions.NotFoundException;
import ar.utn.ba.ddsi.models.repositories.UsuariosRepository;
import ar.utn.ba.ddsi.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class LoginService {
    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByNombre(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new NotFoundException("Usuario", username);
        }

        return usuario;
    }

    public String generarAccessToken(String username) {
        return JwtUtil.generarAccessToken(username);
    }

    public String generarRefreshToken(String username) {
        return JwtUtil.generarRefreshToken(username);
    }
}
