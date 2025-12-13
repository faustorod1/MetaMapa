package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.CSVReader;
import ar.utn.ba.ddsi.commons.Coordenada;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LectorDeCSV {
    private PathDataset pathArchivo;

    public LectorDeCSV(PathDataset pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    public int contarHechos() {
        return CSVReader.contarRegistros(pathArchivo.getPath()) - 1; // Resta 1 por el header
    }

    public List<Hecho> getHechosPaginados(int skip, int limit) {
        // Agrega el +1 para que saltee el header
        List<String[]> filasRaw = CSVReader.leerPaginado(pathArchivo.getPath(), skip + 1, limit);
        return mapearFilasAHechos(filasRaw, skip);
    }

    public ArrayList<Hecho> getHechos(){
        return leerHechosDeCSV();
    }

    private ArrayList<Hecho> leerHechosDeCSV(){      // Traducción de String[] a List<Hecho>
        ArrayList<Hecho> hechos = new ArrayList<>();
        ArrayList<String[]> filasStr = CSVReader.leer(pathArchivo.getPath());

        // i empieza en 1 porque se saltea la fila de headers.
        for (int i = 1; i < filasStr.size(); i++) {
            String[] fila = filasStr.get(i);

            Double latitud = Double.parseDouble(fila[CamposHecho.LATITUD.ordinal()]);
            Double longitud = Double.parseDouble(fila[CamposHecho.LONGITUD.ordinal()]);
            String fechaString = fila[(CamposHecho.FECHADEHECHO.ordinal())];
            LocalDateTime fecha = LocalDate.parse(fechaString.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();

            Hecho h = Hecho.builder()
                    .id((long) i)
                    .idDataset(pathArchivo.getId())
                    .titulo(fila[CamposHecho.TITULO.ordinal()])
                    .descripcion(fila[CamposHecho.DESCRIPCION.ordinal()])
                    .categoria(new Categoria(fila[CamposHecho.CATEGORIA.ordinal()]))
                    .lugarAcontecimiento(new Coordenada(latitud, longitud))
                    .fechaHecho(fecha)
                    .fechaDeCarga(pathArchivo.fechaCarga)
                    .build();

            hechos.add(h);
        }
        return hechos;
    }


    private List<Hecho> mapearFilasAHechos(List<String[]> filasStr, int offsetId) {
        ArrayList<Hecho> hechos = new ArrayList<>();

        for (int i = 0; i < filasStr.size(); i++) {
            String[] fila = filasStr.get(i);
            try {
                Double latitud = Double.parseDouble(fila[CamposHecho.LATITUD.ordinal()]);
                Double longitud = Double.parseDouble(fila[CamposHecho.LONGITUD.ordinal()]);
                String fechaString = fila[(CamposHecho.FECHADEHECHO.ordinal())];
                LocalDateTime fecha = LocalDate.parse(fechaString.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();

                Hecho h = Hecho.builder()
                        .id((long) (offsetId + i + 1))
                        .idDataset(pathArchivo.getId())
                        .titulo(fila[CamposHecho.TITULO.ordinal()])
                        .descripcion(fila[CamposHecho.DESCRIPCION.ordinal()])
                        .categoria(new Categoria(fila[CamposHecho.CATEGORIA.ordinal()]))
                        .lugarAcontecimiento(new Coordenada(latitud, longitud))
                        .fechaHecho(fecha)
                        .fechaDeCarga(pathArchivo.fechaCarga)
                        .build();

                hechos.add(h);
            } catch (Exception e) {
                System.err.println("Error parseando fila CSV: " + e.getMessage());
            }
        }
        return hechos;
    }
}