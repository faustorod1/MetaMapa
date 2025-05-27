package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.CSVReader;
import ar.utn.ba.ddsi.commons.Coordenada;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LectorDeCSV {
    private PathDataset pathArchivo;

    public LectorDeCSV(PathDataset pathArchivo) {
        this.pathArchivo = pathArchivo;
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
            LocalDate fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

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

}