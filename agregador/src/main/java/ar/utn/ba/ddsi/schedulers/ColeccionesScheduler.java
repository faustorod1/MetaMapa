package ar.utn.ba.ddsi.schedulers;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ColeccionesScheduler {
    private IColeccionesService coleccionesService;
    private static final Logger log = LoggerFactory.getLogger(ColeccionesScheduler.class);


    public ColeccionesScheduler(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;

    }

    @Scheduled(fixedRate = 60*1000)
    //@Scheduled(cron = "0 00 4 * * *", zone = "America/Argentina/Buenos_Aires")
    @Transactional
    public void consensuarHechosDeColecciones(){
        try {
        coleccionesService.consensuarColecciones();
            log.info("Consenso realizado!");
        } catch (Exception e) {
            log.info("Error: " + e.getMessage());}
    }

}
