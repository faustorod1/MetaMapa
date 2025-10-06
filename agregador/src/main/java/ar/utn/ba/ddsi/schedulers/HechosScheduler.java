package ar.utn.ba.ddsi.schedulers;

import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HechosScheduler {
    private final IHechosService hechosService;

    public HechosScheduler(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @Scheduled(fixedRate = 50000) //50 segundos  //TODO: DEBE SER 1HR (60*60*1000)
    public void actualizarHechos() {
        try {
            hechosService.actualizarHechos();
            System.out.println("Actualizados!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
