package ar.utn.ba.ddsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicioDeEstadisticasApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicioDeEstadisticasApplication.class, args);
    }
}