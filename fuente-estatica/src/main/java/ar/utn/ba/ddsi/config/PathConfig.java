package ar.utn.ba.ddsi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class PathConfig {

    @Value("${hechos.fuente.path}")
    private String pathHechos;

    @Bean
    public String pathFuenteHechos() {
        return pathHechos;
    }
}
