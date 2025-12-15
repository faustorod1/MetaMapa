package ar.utn.ba.ddsi.config;

import ar.utn.ba.ddsi.providers.CustomAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    private final CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .eraseCredentials(false)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/login", "/logout", "/register", "/informacion-legal-y-privacidad","/main","/main/mapa", "/main/buscador",
                                "/hechos/formulario-de-carga","/hechos/cargar","/hechos/importarCSV","/hechos/importar","/hechos/detalle-hecho/{id_hecho}", "/colecciones", "/colecciones/{id}/con-hechos", "/colecciones/{identificador}/filtrar-hechos").permitAll()
                        .requestMatchers("/api/solicitudes/eliminacion/{id}", "/api/solicitudes/solicitarEliminacion", "/api/solicitudes/modificacion/{id_hecho}", "api/solicitudes/solicitarModificacion").hasAnyRole("CONTRIBUYENTE","ADMIN")
                        .requestMatchers("/colecciones/formulario-de-carga", "/colecciones/cargar", "/colecciones/formulario-de-edicion/{id_coleccion}","/colecciones/editar", "/api/solicitudes/tratarEliminaciones", "/api/solicitudes/tratarEliminacion/{id}", "/api/solicitudes/resolverEliminacion", "/api/solicitudes/tratarModificaciones", "/api/solicitudes/tratarModificacion/{solicitudId}").hasRole("ADMIN")
                        .requestMatchers("/404","/403","/401").permitAll()
                        .requestMatchers("/panel","/panel/actualizarHechos", "/panel/consensuarColecciones","/sinSolicitudesDeEliminacionPendientes", "/sinSolicitudesDeModificacionPendientes").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(successHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            String path = request.getRequestURI();

                            if (request.getQueryString() != null) {
                                path += "?" + request.getQueryString();
                            }
                            String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8);
                            response.sendRedirect("/login?requestedView=" + encodedPath);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/403")
                        )

                );
        return http.build();
    }

}