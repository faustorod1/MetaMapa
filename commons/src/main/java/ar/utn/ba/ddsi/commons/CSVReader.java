package ar.utn.ba.ddsi.commons;

import java.io.FileReader;
import java.util.ArrayList;


public class CSVReader {
    public static ArrayList<String[]> leer(String pathArchivo) {        // Lectura de CSV genérico
        ArrayList<String[]> filas = new ArrayList<>();
        try(com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(new FileReader(pathArchivo))){
            String[] fila = csvReader.readNext();
            while((fila != null)) {
                filas.add(fila);
                fila = csvReader.readNext();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return filas;
    }

}