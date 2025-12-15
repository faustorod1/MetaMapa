package ar.utn.ba.ddsi.schedulers;

import ar.utn.ba.ddsi.services.IHechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HechosScheduler {
    private final IHechosService hechosService;
    private static final Logger log = LoggerFactory.getLogger(HechosScheduler.class);

    public HechosScheduler(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void actualizarHechos() {
        try {
            hechosService.actualizarHechos();
            log.info("Actualizados!");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
