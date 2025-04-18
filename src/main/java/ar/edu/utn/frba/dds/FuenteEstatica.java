package ar.edu.utn.frba.dds;

import com.opencsv.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FuenteEstatica extends Fuente {
    private String archivo;

    public FuenteEstatica(String archivoCSV) {
        this.archivo = archivoCSV;
    }

    public List<Hecho> consultarCSV(){

        List<Hecho> hechos = new ArrayList<Hecho>();
        try(CSVReader csvReader = new CSVReader(new FileReader(archivo))){
            String[] headers = csvReader.readNext();
            String[] fila = csvReader.readNext();
            do{
                float latitud = Integer.parseInt(fila[Hecho.Campos.LATITUD.ordinal()]);
                float longitud = Integer.parseInt(fila[Hecho.Campos.LONGITUD.ordinal()]);
                String fechaString = fila[(Hecho.Campos.FECHADEHECHO.ordinal())];
                LocalDate fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Hecho h = Hecho.builder()
                    .titulo(fila[Hecho.Campos.TITULO.ordinal()])
                    .descripcion(fila[Hecho.Campos.DESCRIPCION.ordinal()])
                    .categoria(new Categoria(fila[Hecho.Campos.CATEGORIA.ordinal()]))
                    .lugarAcontecimiento(new Coordenada(latitud, longitud))
                    .fechaHecho(fecha)
                    .origen(OrigenHecho.DATASET)
                    .build();

                hechos.add(h);
                fila = csvReader.readNext();
            } while((fila != null));
        }catch(Exception e){
            e.printStackTrace();
        }
        return hechos;
    }
}
