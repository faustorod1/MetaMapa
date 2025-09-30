package ar.utn.ba.ddsi.commons;

import java.util.ArrayList;
import java.util.List;

public class DivisorEnLotes {
    public static <T> List<List<T>> dividir(List <T> lista, Integer tamLote) {
        List<List<T>> lotes = new ArrayList<>();
        final int tamLista = lista.size();
        int contador = 0;
        while (contador < tamLista) {
            int fin = Math.min(contador + tamLote, tamLista);
            List<T> lote = lista.subList(contador, fin);
            lotes.add(lote);
            contador += tamLote;
        }
        return lotes;
    }
}
