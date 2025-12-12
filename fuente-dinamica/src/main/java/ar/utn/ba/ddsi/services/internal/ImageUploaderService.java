package ar.utn.ba.ddsi.services.internal;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
/*
@Service
public class ImageUploaderService {
  private final Cloudinary cloudinary;

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
}
*/

