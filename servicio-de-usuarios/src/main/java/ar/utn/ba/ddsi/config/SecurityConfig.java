package ar.utn.ba.ddsi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF es común para APIs REST
                .csrf(AbstractHttpConfigurer::disable)
                // Aquí se definen las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso sin autenticación a cualquier ruta bajo /api/auth/
                        .requestMatchers("/api/auth/**").permitAll()
                        // Para cualquier otra ruta, se requiere autenticación
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}