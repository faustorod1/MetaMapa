package ar.utn.ba.ddsi.MetaMapa.models.entities;

import java.util.ArrayList;

public class FuenteEstatica implements Fuente {
    private String pathArchivo;

    public FuenteEstatica(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    @Override
    public ArrayList<Hecho> getHechos(){
        return CSVReader.leerHechos(pathArchivo);
    }

}