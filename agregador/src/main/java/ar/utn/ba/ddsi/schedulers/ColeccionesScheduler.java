package ar.utn.ba.ddsi.schedulers;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ColeccionesScheduler {
    private IColeccionesService coleccionesService;

    private ColeccionesScheduler(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

   // @Scheduled(fixedRate = 30000)
    @Scheduled(cron = "0 00 4 * * *", zone = "America/Argentina/Buenos_Aires")
    //@Scheduled(fixedRate = 30000)
    public void consensuarHechosDeColecciones(){
        coleccionesService.consensuarColecciones();
        System.out.println("Hechos consensuados!!");
    }

}
