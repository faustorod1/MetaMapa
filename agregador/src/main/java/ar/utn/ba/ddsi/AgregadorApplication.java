package ar.utn.ba.ddsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgregadorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgregadorApplication.class, args);
    }

}