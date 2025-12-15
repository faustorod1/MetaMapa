package ar.utn.ba.ddsi.services.internal;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ImageUploaderService {
  private final Cloudinary cloudinary;
  private static final Pattern CLOUDINARY_URL_PATTERN = Pattern.compile(".*/upload/(?:v\\d+/)?(.+)\\.[a-z]+$");

  public ImageUploaderService(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  public String uploadFile(MultipartFile file) {
    try {
      Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
          "folder", "hechos_dinamica/imagenes",
          "resource_type", "auto"
      ));

      return (String) uploadResult.get("secure_url");

    } catch (IOException e) {
      throw new RuntimeException("Error al subir imagen a Cloudinary", e);
    }
  }

  public void deleteFile(String url) {
    try {
      String publicId = extractPublicIdFromUrl(url);

      cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

    } catch (IOException e) {
      throw new RuntimeException("Error al eliminar imagen de Cloudinary: " + url, e);
    }
  }

  private String extractPublicIdFromUrl(String url) {
    Matcher matcher = CLOUDINARY_URL_PATTERN.matcher(url);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return url;
  }
}


