
//Librerias
import java.lang.Thread;

public class DBMS {
    
    // atributos
    DBMSThread robot;
    DBMSThread logs;
    DBMSThread estadoPrograma;
    DBMSThread variablesEstaticas;
    
    // metodos
    // main
    public static void main(String[] args) {
        // crear los archivos en caso de que no existan
        // crear los hilos
        @SuppressWarnings("unused")
        DBMS dbms = new DBMS();
        
    }
    
    // Constructor
    public DBMS() {
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
    
    public void EnviarInformacion(int typeInformation, String information) {
        
    }
    
    public String recibirInformacion(int typeInformation, String information) {
        procesarInformacion(typeInformation, information);
        return null;
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

    public String ReadFile() {
        switch (valor) {
            case 1:
                robots.ReadFile();
            case 2:
                logs.ReadFile();
            case 3:
                programState.ReadFile();
            case 4:
                staticVariables.ReadFile();
            default:
                return null;
        
        }
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
