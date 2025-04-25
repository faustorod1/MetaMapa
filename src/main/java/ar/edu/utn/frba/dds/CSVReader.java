package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.hechos.Categoria;
import ar.edu.utn.frba.dds.hechos.Hecho;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CSVReader {

    public enum CamposHecho {
        TITULO, DESCRIPCION, CATEGORIA, LATITUD, LONGITUD, FECHADEHECHO
    }

    public static ArrayList<String[]> leer(String pathArchivo) {        // Lectura de CSV genérico
        ArrayList<String[]> filas = new ArrayList<>();
        try(com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(new FileReader(pathArchivo))){
            String[] fila = csvReader.readNext();
            while((fila != null)) {
                filas.add(fila);
                fila = csvReader.readNext();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return filas;
    }


    public static ArrayList<Hecho> leerHechos(String pathArchivo){      // Traducción de filas a <hechos>
        ArrayList<Hecho> hechos = new ArrayList<>();
        ArrayList<String[]> filasStr = leer(pathArchivo);

        // i empieza en 1 porque se saltea la fila de headers.
        for (int i = 1; i < filasStr.size(); i++) {
            String[] fila = filasStr.get(i);

            double latitud = Double.parseDouble(fila[CamposHecho.LATITUD.ordinal()]);
            double longitud = Double.parseDouble(fila[CamposHecho.LONGITUD.ordinal()]);
            String fechaString = fila[(CamposHecho.FECHADEHECHO.ordinal())];
            LocalDate fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            Hecho h = Hecho.builder()
                    .titulo(fila[CamposHecho.TITULO.ordinal()])
                    .descripcion(fila[CamposHecho.DESCRIPCION.ordinal()])
                    .categoria(new Categoria(fila[CamposHecho.CATEGORIA.ordinal()]))
                    .lugarAcontecimiento(new Coordenada(latitud, longitud))
                    .fechaHecho(fecha)
                    .origen(Hecho.Origen.DATASET)
                    .build();

            hechos.add(h);
        }
        return hechos;
    }

}
