package ar.utn.ba.ddsi.models.entities.APIAdapters;

import ar.utn.ba.ddsi.models.entities.Hecho;
import java.util.List;

public interface IAPIAdapter {
    List<Hecho> getHechos();
}
