package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.UserRolesDTO;
import ar.utn.ba.ddsi.models.entities.Administrador;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.Rol;
import ar.utn.ba.ddsi.models.entities.Usuario;
import ar.utn.ba.ddsi.models.exceptions.BadCodeException;
import ar.utn.ba.ddsi.models.exceptions.NotFoundException;
import ar.utn.ba.ddsi.models.exceptions.UsuarioExistenteException;
import ar.utn.ba.ddsi.models.repositories.AdministradorRepository;
import ar.utn.ba.ddsi.models.repositories.ContribuyenteRespository;
import ar.utn.ba.ddsi.models.repositories.UsuariosRepository;
import ar.utn.ba.ddsi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
@Transactional
public class LoginService {

    private final UsuariosRepository usuariosRepository;
    private final ContribuyenteRespository contribuyenteRepository;
    private final AdministradorRepository administradorRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginService(UsuariosRepository usuariosRepository, ContribuyenteRespository contribuyenteRepository, AdministradorRepository administradorRepository,JwtUtil jwtUtil) {
        this.usuariosRepository = usuariosRepository;
        this.contribuyenteRepository = contribuyenteRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    @Transactional(readOnly = true)
    public Usuario autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", email);
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new NotFoundException("Usuario", email);
        }

        return usuario;
    }

    public String generarAccessToken(Usuario usuario) {
        return jwtUtil.generarAccessToken(usuario);
    }

    public String generarRefreshToken(Usuario usuario) {
        return jwtUtil.generarRefreshToken(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuario(String email){
        Optional<Usuario> usuarioOpt = usuariosRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", email);
        }

        return usuarioOpt.get();
    }

    public Usuario registrarUsuario(String email, String password, String nombre, String apellido, Integer code) {
        Optional<Usuario> existente = usuariosRepository.findByEmail(email);
        if (existente.isPresent()) {
            throw new UsuarioExistenteException(email + " ya existe este usuario");
        }

        Usuario usuario = Usuario.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .nombre(nombre)
            .apellido(apellido)
            .build();

        if (code != null && code.equals(3333)) {
            usuario.setRol(Rol.ADMIN);
            usuariosRepository.save(usuario);

            Administrador administrador = new Administrador();
            administrador.setUsuario(usuario);
            administradorRepository.save(administrador);
            return usuario;
        }else if(code != null && !code.equals(3333)){
            throw new BadCodeException("Codigo de administrador no valido");
        }else {
            usuario.setRol(Rol.CONTRIBUYENTE);
            usuariosRepository.save(usuario);
            Contribuyente contribuyente = new Contribuyente();
            contribuyente.setUsuario(usuario);
            contribuyenteRepository.save(contribuyente);
        }
        return usuario;
    }

    @Transactional(readOnly = true)
    public UserRolesDTO obtenerRolUsuario(String email){
        Optional<Usuario> usuarioOpt = usuariosRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", email);
        }

        Usuario usuario = usuarioOpt.get();

        return UserRolesDTO.builder()
                .email(usuario.getEmail())
                .role(usuario.getRol())
                .build();
    }
}