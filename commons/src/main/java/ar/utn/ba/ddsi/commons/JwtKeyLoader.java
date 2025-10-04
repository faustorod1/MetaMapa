package ar.utn.ba.ddsi.commons;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtKeyLoader {

  @Value("${jwt.private.key}")
  private String privateKeyPem;

  @Value("${jwt.public.key}")
  private String publicKeyPem;

  private PrivateKey privateKey;
  private PublicKey publicKey;

  @PostConstruct
  public void init() throws Exception {
    this.privateKey = loadPrivateKey(privateKeyPem);
    this.publicKey = loadPublicKey(publicKeyPem);
  }

  private PrivateKey loadPrivateKey(String pem) throws Exception {
    String cleanPem = pem
        .replaceAll("-----BEGIN PRIVATE KEY-----", "")
        .replaceAll("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s", "");
    byte[] decoded = Base64.getDecoder().decode(cleanPem);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(keySpec);
  }

  private PublicKey loadPublicKey(String pem) throws Exception {
    String cleanPem = pem
        .replaceAll("-----BEGIN PUBLIC KEY-----", "")
        .replaceAll("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", "");
    byte[] decoded = Base64.getDecoder().decode(cleanPem);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(keySpec);
  }

  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }
}
