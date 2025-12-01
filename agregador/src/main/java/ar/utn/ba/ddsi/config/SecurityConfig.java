package ar.utn.ba.ddsi.config;

import ar.utn.ba.ddsi.config.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET, "/api/solicitudes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes/idsPendientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/solicitudes").hasAnyRole("CONTRIBUYENTE","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/solicitudes/{id}/estado").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/colecciones").permitAll()
                        .requestMatchers("/api/colecciones").hasRole("ADMIN")
                        .requestMatchers("/api/colecciones/{identificador}/hechos").permitAll()
                        .requestMatchers("/api/colecciones/con-hechos").permitAll()

                        .requestMatchers("/api/hechos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hechos/{id}").permitAll()
                        .requestMatchers("/api/hechos/contribuyente").hasRole("CONTRIBUYENTE")
                        .requestMatchers("/api/solicitudes/{id}/estado").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/hechos/destacados/{cantidad_hechos_destacados}", "/api/colecciones/destacadas/{cantidad_colecciones_destacadas}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/categorias", "/api/categorias/{id}").permitAll()


                        .anyRequest().hasAnyRole("ADMIN")
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}