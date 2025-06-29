package ar.utn.ba.ddsi.schedulers;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;

public class ColeccionesScheduler {
    private IColeccionesService coleccionesService;

    private ColeccionesScheduler(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "America/Argentina/Buenos_Aires") //A las 4am todos los dias
    public void consensuarHechos(){
        //TODO
        //colecciones.forAll(coleccion -> coleccion.consensuar());
    }
}
