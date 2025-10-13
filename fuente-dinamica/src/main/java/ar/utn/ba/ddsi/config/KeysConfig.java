package ar.utn.ba.ddsi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:keys.properties")
public class KeysConfig {
}