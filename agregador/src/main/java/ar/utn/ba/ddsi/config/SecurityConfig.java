package ar.utn.ba.ddsi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/colecciones").hasRole("ADMIN")
                .requestMatchers(String.valueOf(RequestMethod.GET), "/api/colecciones").permitAll()
                .requestMatchers("/api/colecciones/{identificador}/hechos").permitAll()
                .requestMatchers("/api/colecciones/con-hechos").permitAll()

                .requestMatchers("/api/hechos").permitAll()
                .requestMatchers("/api/hechos/contribuyente/{id}").hasRole("CONTRAYENTE")

                .requestMatchers(String.valueOf(RequestMethod.POST),"/api/solicitudes").hasAnyRole("CONTRIBUYENTE","ADMIN")
                .requestMatchers(String.valueOf(RequestMethod.GET),"/api/solicitudes").hasRole("ADMIN")
                .requestMatchers("/api/solicitudes/{id}/estado").hasAnyRole("ADMIN")

                .anyRequest().hasAnyRole("ADMIN")
            );

        return http.build();
    }
}