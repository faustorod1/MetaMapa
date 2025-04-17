package ar.edu.utn.frba.dds;

import com.opencsv.CSVReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FuenteEstatica {
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
                Hecho h = new Hecho();
                h.setTitulo(fila[Hecho.Campos.TITULO.ordinal()]);
                h.setDescripcion(fila[Hecho.Campos.DESCRIPCION.ordinal()]);
                float latitud = Integer.parseInt(fila[Hecho.Campos.LATITUD.ordinal()]);
                float longitud = Integer.parseInt(fila[Hecho.Campos.LONGITUD.ordinal()]);
                Coordenada lugarAcontecimiento = new Coordenada(latitud, longitud);
                h.setLugarAcontecimiento(lugarAcontecimiento);
                String fechaString = fila[(Hecho.Campos.FECHADEHECHO.ordinal())];
                this.setearFechaHecho(fechaString,h);
                //TODO: falta settear la categoria
                hechos.add(h);
                fila = csvReader.readNext();
            } while((fila != null));
        }catch(Exception e){
            e.printStackTrace();
        }
        return hechos;
    }

    public void setearFechaHecho(String fechaString,Hecho h) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(fechaString, formateador);
        h.setFechaHecho(fecha);

    }

}
