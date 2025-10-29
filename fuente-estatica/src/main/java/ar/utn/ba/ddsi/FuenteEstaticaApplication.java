package ar.utn.ba.ddsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class FuenteEstaticaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FuenteEstaticaApplication.class, args);
    }
}