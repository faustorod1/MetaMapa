package ar.utn.ba.ddsi.init;

import ar.utn.ba.ddsi.models.dtos.apigob.MunicipiosResponseDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.ProvinciasResponseDTO;
import ar.utn.ba.ddsi.models.entities.ubicacion.Municipio;
import ar.utn.ba.ddsi.models.entities.ubicacion.Provincia;
import ar.utn.ba.ddsi.models.repositories.IMunicipiosRepository;
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
    private IMunicipiosRepository municipiosRepository;

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

        if (municipiosRepository.count() == 0) {
            int municipiosPorPagina = 300;

            MunicipiosResponseDTO datosQuery = georefWebClient.get()
                    .uri("/municipios?max=1")
                    .retrieve()
                    .bodyToMono(MunicipiosResponseDTO.class)
                    .block();
            // Páginas de 300 municipios
            int cantPaginas = (int) Math.ceil( (double) datosQuery.getTotal() / municipiosPorPagina );

            List<Municipio> municipios = Flux.range(0, cantPaginas)
                    .parallel()
                    .runOn(Schedulers.parallel())
                    .flatMap(page -> georefWebClient.get()
                                    .uri(
                                            uriBuilder -> uriBuilder
                                                    .path("/municipios")
                                                    .queryParam("max", municipiosPorPagina)
                                                    .queryParam("inicio", page * municipiosPorPagina)
                                                    .build()
                                    )
                            .retrieve()
                            .bodyToMono(MunicipiosResponseDTO.class)
                            .map(MunicipiosResponseDTO::getMunicipios)
                    )
                    .sequential()
                    .flatMap(Flux::fromIterable)
                    .collectList()
                    .block();

            List<Provincia> provincias = provinciasRepository.findAll();

            municipios.forEach(municipio -> {
                municipio.setId(null);
                Provincia provincia = provincias.stream().filter(p -> p.getNombre().equals(municipio.getProvincia().getNombre())).findFirst().orElse(null);
                municipio.setProvincia(provincia);
            });
            municipiosRepository.saveAll(municipios);
        }
    }
}
