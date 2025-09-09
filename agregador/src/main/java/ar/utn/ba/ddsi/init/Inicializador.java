package ar.utn.ba.ddsi.init;

import ar.utn.ba.ddsi.models.dtos.apigob.DepartamentosResponseDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.ProvinciasResponseDTO;
import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;
import ar.utn.ba.ddsi.models.entities.ubicacion.Provincia;
import ar.utn.ba.ddsi.models.repositories.IDepartamentosRepository;
import ar.utn.ba.ddsi.models.repositories.IProvinciasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Component
public class Inicializador implements CommandLineRunner {
    @Autowired
    private IProvinciasRepository provinciasRepository;
    @Autowired
    private IDepartamentosRepository departamentosRepository;

    private WebClient georefWebClient;

    public Inicializador(@Value("${georef.api.base-url}") String georefApiBaseUrl) {
        georefWebClient = WebClient.builder().baseUrl(georefApiBaseUrl).build();
    }

    public void run(String[] args) {
        if (provinciasRepository.count() == 0) {
            List<Provincia> provincias = georefWebClient.get()
                    .uri("/provincias")
                    .retrieve()
                    .bodyToMono(ProvinciasResponseDTO.class)
                    .map(ProvinciasResponseDTO::getProvincias)
                    .block();
            provincias.forEach(provincia -> provincia.setId(null));
            provinciasRepository.saveAll(provincias);
        }

        if (departamentosRepository.count() == 0) {
            int departamentosPorPagina = 300;

            DepartamentosResponseDTO datosQuery = georefWebClient.get()
                    .uri("/departamentos?max=1")
                    .retrieve()
                    .bodyToMono(DepartamentosResponseDTO.class)
                    .block();
            // Páginas de 300 departamentos
            int cantPaginas = (int) Math.ceil( (double) datosQuery.getTotal() / departamentosPorPagina );

            List<Departamento> departamentos = Flux.range(0, cantPaginas)
                    .parallel()
                    .runOn(Schedulers.parallel())
                    .flatMap(page -> georefWebClient.get()
                                    .uri(
                                            uriBuilder -> uriBuilder
                                                    .path("/departamentos")
                                                    .queryParam("max", departamentosPorPagina)
                                                    .queryParam("inicio", page * departamentosPorPagina)
                                                    .build()
                                    )
                            .retrieve()
                            .bodyToMono(DepartamentosResponseDTO.class)
                            .map(DepartamentosResponseDTO::getDepartamentos)
                    )
                    .sequential()
                    .flatMap(Flux::fromIterable)
                    .collectList()
                    .block();

            List<Provincia> provincias = provinciasRepository.findAll();

            departamentos.forEach(departamento -> {
                departamento.setId(null);
                Provincia provincia = provincias.stream().filter(p -> p.getNombre().equals(departamento.getProvincia().getNombre())).findFirst().orElse(null);
                departamento.setProvincia(provincia);
            });
            departamentosRepository.saveAll(departamentos);
        }
    }
}
