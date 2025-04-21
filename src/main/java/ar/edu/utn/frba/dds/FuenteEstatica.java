package ar.edu.utn.frba.dds;

import com.opencsv.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import java.util.ArrayList;

public class FuenteEstatica implements Fuente {
    private String archivo;

    public FuenteEstatica(String archivoCSV) {
        this.archivo = archivoCSV;
    }

    public ArrayList<Hecho> getHechos(){

        ArrayList<Hecho> hechos = new ArrayList<Hecho>();
        try(CSVReader csvReader = new CSVReader(new FileReader(archivo))){
            csvReader.readNext(); // Salta la primera fila que es la cabecera
            String[] fila = csvReader.readNext();
            do{
                double latitud = Double.parseDouble(fila[Hecho.Campos.LATITUD.ordinal()]);
                double longitud = Double.parseDouble(fila[Hecho.Campos.LONGITUD.ordinal()]);
                String fechaString = fila[(Hecho.Campos.FECHADEHECHO.ordinal())];
                LocalDate fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Hecho h = Hecho.builder()
                    .titulo(fila[Hecho.Campos.TITULO.ordinal()])
                    .descripcion(fila[Hecho.Campos.DESCRIPCION.ordinal()])
                    .categoria(new Categoria(fila[Hecho.Campos.CATEGORIA.ordinal()]))
                    .lugarAcontecimiento(new Coordenada(latitud, longitud))
                    .fechaHecho(fecha)
                    .origen(Hecho.Origen.DATASET)
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
