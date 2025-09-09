package ar.utn.ba.ddsi.schedulers;


import ar.utn.ba.ddsi.services.IEstadisticasService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class EstadisticasScheduler {
    private final IEstadisticasService estadisticasService;

    public EstadisticasScheduler(IEstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    // @Scheduled (cron = "0 00 2 * * *", zone = "America/Argentina/Buenos_Aires")
    @Scheduled(fixedRate = 5000)
    public void enviarEstadisticas() {
        try{
        estadisticasService.updateEstadisticas();
            System.out.println("CSVs cargados!");
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //@Scheduled(cron = "0 0 0 1 * ?")
    @Scheduled(fixedRate = 30000)
    public void eliminarEstadisticasAntiguas() {
        try{
        estadisticasService.eliminarEstadisticasViejas();
            System.out.println("CSVs antiguos eliminados!");
        }catch (Exception e){
            System.out.println("No pudo eliminarse contenido");
        }
    }

}
