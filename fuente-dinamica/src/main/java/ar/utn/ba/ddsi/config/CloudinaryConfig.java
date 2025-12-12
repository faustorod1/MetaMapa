package ar.utn.ba.ddsi.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@ConditionalOnProperty(
        name = "cloudinary.enabled", // Spring buscará esta propiedad
        havingValue = "true",        // Y solo creará el bean si su valor es 'true'
        matchIfMissing = false       // SI LA PROPIEDAD NO EXISTE, NO CREES EL BEAN
)
@Configuration
public class CloudinaryConfig {
  @Value("${cloudinary.cloud_name}")
  private String cloudName;

  @Value("${cloudinary.api_key}")
  private String apiKey;

  @Value("${cloudinary.api_secret}")
  private String apiSecret;

  @Bean
  public Cloudinary cloudinary() {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", cloudName);
    config.put("api_key", apiKey);
    config.put("api_secret", apiSecret);
    return new Cloudinary(config);
  }
}
