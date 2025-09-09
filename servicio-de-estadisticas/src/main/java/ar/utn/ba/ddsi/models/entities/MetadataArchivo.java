package ar.utn.ba.ddsi.models.entities;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MetadataArchivo {
  public static void main(String[] args) {
    Path path = Paths.get("C:\\Users\\Juan\\Desktop\\ejemplo"); // archivo o carpeta

    try {
      BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

      System.out.println("Fecha de creación: " + attr.creationTime());
      System.out.println("Última modificación: " + attr.lastModifiedTime());
      System.out.println("Último acceso: " + attr.lastAccessTime());

      if (attr.isDirectory()) {
        System.out.println("Es una carpeta.");
      } else {
        System.out.println("Es un archivo.");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
