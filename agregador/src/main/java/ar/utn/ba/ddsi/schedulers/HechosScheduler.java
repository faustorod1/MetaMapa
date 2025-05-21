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

    @Scheduled(fixedRate = 5000)        // 5 segundos
    public void actualizarHechos(){
        hechosService.actualizarHechos();
    }


}
