
//Librerias
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBMS implements API {

    // atributos
    DBMSThread robot;
    DBMSThread logs;
    DBMSThread estadoPrograma;
    DBMSThread variablesEstaticas;
    Date fechaHoraActual; // fecha y hora
    SimpleDateFormat formatoFechaHora; // Formatear fecha

    // metodos
    // main
    /*
     * public static void main(String[] args) {
     * // crear los archivos en caso de que no existan
     * // crear los hilos
     * 
     * @SuppressWarnings("unused")
     * DBMS dbms = new DBMS();
     * 
     * }
     */

    // Constructor
    public DBMS() {
        fechaHoraActual = new Date();
        formatoFechaHora = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        // Formatear la fecha y hora actual al formato deseado
        String fechaHoraFormateada = formatoFechaHora.format(fechaHoraActual);
        // Imprimir la fecha y hora formateadas
        System.out.println("Fecha y hora actuales: " + fechaHoraFormateada);
        robot = new DBMSThread(1);
        logs = new DBMSThread(2);
        estadoPrograma = new DBMSThread(3);
        variablesEstaticas = new DBMSThread(4);
        // iniciar los hilos
        robot.start();
        logs.start();
        estadoPrograma.start();
        variablesEstaticas.start();
        robot.ReadFile();
        logs.ReadFile();
        estadoPrograma.ReadFile();
        variablesEstaticas.ReadFile();
    }

    @Override
    public void EnviarInformacion(int typeInformation, String information) {
        System.out.println("enviar");
    }

    @Override
    public void recibirDatos(int typeInformation, String information) {
        System.out.println("informacion recibida");
        procesarInformacion(typeInformation, information);

    }

    public void procesarInformacion(int typeInformation, String information) {
        System.out.println(typeInformation + " " + information);
        switch (typeInformation) {
            case 1:// Enviar a Robots
                robot.ReceiveMessage(information);
                break;
            case 2:
                logs.ReceiveMessage(information);
                break;
            case 3:
                estadoPrograma.ReceiveMessage(information);
                break;
            case 4:
                variablesEstaticas.ReceiveMessage(information);
                break;
            default:
                System.out.println("Value Type unkwon");
                break;
        }
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

    public int ReceiveMessage(String Datos) {
        switch (valor) {
            case 1:
                robots.ReceiveMessage(Datos);
                break;
            case 2:
                logs.ReceiveMessage(Datos);
                break;
            case 3:
                programState.ReceiveMessage(Datos);
                break;
            case 4:
                staticVariables.ReceiveMessage(Datos);
                break;
            default:
                System.out.println("valor errado");
                return 1;
        }
        return 0;
    }

    public int ReadFile() {
        switch (valor) {
            case 1:
                robots.ReadFile();
                break;
            case 2:
                logs.ReadFile();
                break;
            case 3:
                programState.ReadFile();
                break;
            case 4:
                staticVariables.ReadFile();
                break;
            default:
                return 1;
        }
        return 0;
    }

    public String searchData(String query) {
        switch (valor) {
            case 1:
                return robots.searchData(query);

            case 2:
                return logs.searchData(query);
            case 3:
                return programState.searchData(query);
            case 4:
                return staticVariables.searchData(query);
            default:
                return null;
        }
    }
}
