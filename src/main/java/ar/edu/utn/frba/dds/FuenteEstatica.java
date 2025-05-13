package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.CSVReader;

import java.util.ArrayList;

public class FuenteEstatica implements Fuente {
    private String pathArchivo;

    public FuenteEstatica(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    public ArrayList<Hecho> getHechos(){
        return CSVReader.leerHechos(pathArchivo);
    }

}
