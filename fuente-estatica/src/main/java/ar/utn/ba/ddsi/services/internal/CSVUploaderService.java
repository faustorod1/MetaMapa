package ar.utn.ba.ddsi.services.internal;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CSVUploaderService {
    private final Cloudinary cloudinary;

    public CSVUploaderService(@Autowired Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map uploadCsv(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String nameWithoutExtension = originalName;
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            int lastDotIndex = originalName.lastIndexOf('.');
            nameWithoutExtension = originalName.substring(0, lastDotIndex);
            extension = originalName.substring(lastDotIndex);
        }

        String finalName = nameWithoutExtension + "_" + UUID.randomUUID() + extension;

        Map params = ObjectUtils.asMap(
                "resource_type", "raw",
                "public_id", finalName,
                "folder", "csvs"
        );

        return cloudinary.uploader().upload(file.getBytes(), params);
    }
}
