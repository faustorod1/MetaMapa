package ar.utn.ba.ddsi.commons;

import com.opencsv.CSVWriter;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private static Reader obtenerReader(String pathArchivo) throws IOException {
        if (pathArchivo.startsWith("http")) {
            URL url = new URL(pathArchivo);
            return new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        } else {
            return new FileReader(pathArchivo);
        }
    }

    public static ArrayList<String[]> leer(String pathArchivo) {
        ArrayList<String[]> filas = new ArrayList<>();

        try (Reader reader = obtenerReader(pathArchivo);
             com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(reader)) {

            String[] fila;
            while ((fila = csvReader.readNext()) != null) {
                filas.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el CSV: " + pathArchivo, e);
        }
        return filas;
    }

    public static Boolean crear(String pathArchivo, ArrayList<String[]> info) {
        File file = new File(pathArchivo);
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file, false))) {
            csvWriter.writeAll(info);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int contarRegistros(String pathArchivo) {
        try (Reader sourceReader = obtenerReader(pathArchivo);
             LineNumberReader reader = new LineNumberReader(sourceReader)) {
            while (reader.skip(Long.MAX_VALUE) > 0) {};
            return Math.max(0, reader.getLineNumber());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Lee una parte del archivo.
     * @param skip Cantidad de registros de datos a saltar
     * @param limit Cantidad máxima de registros a leer
     */
    public static List<String[]> leerPaginado(String pathArchivo, int skip, int limit) {
        ArrayList<String[]> filas = new ArrayList<>();

        try (Reader sourceReader = obtenerReader(pathArchivo);
             com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(sourceReader)) {

            for (int i = 0; i < skip; i++) {
                if (csvReader.readNext() == null) return filas;
            }

            int leidos = 0;
            String[] fila;
            while (leidos < limit && (fila = csvReader.readNext()) != null) {
                filas.add(fila);
                leidos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer paginado: " + pathArchivo, e);
        }
        return filas;
    }
}