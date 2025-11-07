package ar.utn.ba.ddsi.schedulers;


import ar.utn.ba.ddsi.services.IEstadisticasService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class EstadisticasScheduler {
    private static final long PERIODO_DE_GENERACION_DE_ESTADISTICAS = 60000L; // 1 minuto
    private final IEstadisticasService estadisticasService;

    public EstadisticasScheduler(IEstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    // @Scheduled (cron = "0 00 2 * * *", zone = "America/Argentina/Buenos_Aires")
    @Scheduled(fixedRate = PERIODO_DE_GENERACION_DE_ESTADISTICAS)
    public void enviarEstadisticas() {
        try{
        estadisticasService.generarEstadisticas();
            System.out.println("CSVs cargados!");
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}