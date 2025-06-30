package ar.utn.ba.ddsi.schedulers;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;

public class ColeccionesScheduler {
    private IColeccionesService coleccionesService;

    private ColeccionesScheduler(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "America/Argentina/Buenos_Aires")
    public void consensuarHechos(){
        coleccionesService.consensuarColecciones();
    }
}
