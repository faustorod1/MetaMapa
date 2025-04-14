package ar.edu.utn.frba.dds;
import java.util.Scanner;

public class SolicitudDeEliminacion {
    private String descripcion;
    private Hecho hecho;
    private boolean estaPendiente;

    public SolicitudDeEliminacion(Hecho hecho, String descripcion) {
        this.descripcion = descripcion;
        this.hecho = hecho;
        this.estaPendiente = true;
    }

    public void preguntar(){
        System.out.println("Descripcion: " + descripcion);
        System.out.println("1.Eliminar");
        System.out.println("2.Siguiente Solicitud");

        Scanner scanner = new Scanner(System.in);
        String linea = scanner.nextLine();

        switch (linea) {
            case "1":
                this.aceptar();
                break;
            case "2":
                this.rechazar();
                break;
            default:
                System.out.println("Opcion no valida");
                break;
        }
        scanner.close();
    }

    public void aceptar(){
        estaPendiente = false; //TODO: Falta logica
    }
    public void rechazar(){
        estaPendiente = false; //TODO: Falta logica
    }
}
