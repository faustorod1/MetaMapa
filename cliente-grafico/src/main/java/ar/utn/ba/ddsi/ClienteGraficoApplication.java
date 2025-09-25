package ar.utn.ba.ddsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class ClienteGraficoApplication {
    public static void main(String[] args) {SpringApplication.run(ClienteGraficoApplication.class, args);}
}



//    Controller base
//        RequestMapping("/")
//            @get -> landing
//            @get("infolegal") -> infolegal
//            @get("login") -> login
//
//    Controller main
//        RequestMapping("/main")
//            @get -> main
//            @get("mapa") -> mapa
//
//    /main/mapa
//    /main/cargarCSV
//    /main
