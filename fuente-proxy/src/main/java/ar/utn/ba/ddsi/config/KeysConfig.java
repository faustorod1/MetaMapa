package ar.utn.ba.ddsi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:keys.properties", ignoreResourceNotFound = true)
public class KeysConfig {
}