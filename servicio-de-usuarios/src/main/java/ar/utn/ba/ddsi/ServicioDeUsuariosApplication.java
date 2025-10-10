package ar.utn.ba.ddsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("ar.utn.ba.ddsi.models.repositories")
public class ServicioDeUsuariosApplication {
    public static void main(String[] args) { SpringApplication.run(ServicioDeUsuariosApplication.class, args);
    }
}