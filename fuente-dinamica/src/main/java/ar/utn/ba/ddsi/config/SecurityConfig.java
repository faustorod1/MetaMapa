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
                        .requestMatchers(HttpMethod.POST, "/api/hechos").hasRole("CONTRIBUYENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/hechos").hasAnyRole("SYSTEM", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/hechos").hasAnyRole("SYSTEM", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/hechos").hasAnyRole("SYSTEM", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/hechos/ids").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/hechos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/solicitudes").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/solicitudes/{id}/estado").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);;

        return http.build();
    }
}