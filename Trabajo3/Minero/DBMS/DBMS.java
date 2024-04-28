
//Librerias
import java.lang.Thread;

public class DBMS {
    // main
    public static void main(String[] args) {
        // crear los archivos en caso de que no existan
        // crear los hilos
        DBMSThread robot = new DBMSThread(1);
        DBMSThread logs = new DBMSThread(2);
        DBMSThread estadoPrograma = new DBMSThread(3);
        DBMSThread variablesEstaticas = new DBMSThread(4);

        robot.start();
        logs.start();
        estadoPrograma.start();
        variablesEstaticas.start();
    }

}

class DBMSThread extends Thread {

    private int valor;

    public DBMSThread(int valor) {
        this.valor = valor;
    }

    public void run() {
        crearArchivos(this.valor);
    }

    public int crearArchivos(int valor) {
        switch (valor) {
            case 1:
                robots.crearArchivo();
                break;
            case 2:
                logs.crearArchivo();
                break;
            case 3:
                programState.crearArchivo();
                break;
            case 4:
                staticVariables.crearArchivo();
                break;
            default:
                System.out.println("valor errado");
                break;
        }

        return 0;
    }
}
