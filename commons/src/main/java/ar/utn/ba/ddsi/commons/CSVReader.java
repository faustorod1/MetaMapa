package ar.utn.ba.ddsi.commons;

import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


public class CSVReader {
    public static ArrayList<String[]> leer(String pathArchivo) {        // Lectura de CSV genérico
        ArrayList<String[]> filas = new ArrayList<>();

        try(com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(new FileReader(pathArchivo))){
            String[] fila = csvReader.readNext();
            while((fila != null)) {
                filas.add(fila);fila = csvReader.readNext();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return filas;
    }


    public static Boolean crear(String pathArchivo, ArrayList<String[]> info) {
        File file = new File(pathArchivo);

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file, false))) {
            // el "false" indica que NO se hace append, sino sobrescritura
            csvWriter.writeAll(info);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}