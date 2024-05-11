
//Librerias
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class DBMS implements API {

    // atributos
    // Hilos que manejan los archivos
    DBMSThread robot;
    DBMSThread logs;
    DBMSThread estadoPrograma;
    DBMSThread variablesEstaticas;
    Procesador procesador;
    Date fechaHoraActual; // fecha y hora
    SimpleDateFormat formatoFechaHora; // Formatear fecha
    // cola de datos
    static Queue<String> datosRecibidos;
    // Semaforo(s)
    static Semaphore semaforoCola = new Semaphore(1);
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
        // crear formato de la fecha
        formatoFechaHora = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        datosRecibidos = new LinkedList<String>();
        robot = new DBMSThread(1);
        logs = new DBMSThread(2);
        estadoPrograma = new DBMSThread(3);
        variablesEstaticas = new DBMSThread(4);
        procesador = new Procesador(datosRecibidos, this);
        // iniciar los hilos
        robot.start();
        logs.start();
        estadoPrograma.start();
        variablesEstaticas.start();
        procesador.start();

        Thread hiloVerificador = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    verificarHilos();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void EnviarInformacion(int typeInformation, String information) {

    }

    @Override
    public void recibirDatos(int typeInformation, String information) {
        // Obtener la fecha en que se recibió la información:
        fechaHoraActual = new Date();
        String fechaHoraFormateada = formatoFechaHora.format(fechaHoraActual);
        // crear string con la informacion completa
        String informationComplete = typeInformation + "," + fechaHoraFormateada + "," + information;
        // guardar informacion en la cola
        boolean guardado = false;
        try {
            semaforoCola.acquire();
            guardado = datosRecibidos.add(informationComplete);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforoCola.release();
        }
        // System.out.println("informacion recibida: " + informationComplete);
        if (guardado)
            System.out.println("informacion recibida: " + informationComplete);
        else
            System.out.println("Hubo algun error");
    }

    public void leerCola() {
        boolean datoExist = false;
        if (!datosRecibidos.isEmpty()) {
            String dato = datosRecibidos.poll();
            datoExist = true;
        }
        if (datoExist) {
            // enviar datos
        }

    }

    public void procesarInformacion(int typeInformation, String information) {
        System.out.println(typeInformation + " " + information);
        switch (typeInformation) {
            case 1:// Enviar a Robots
                robot.escribirArchivo(typeInformation, information);
                break;
            case 2:
                logs.escribirArchivo(typeInformation, information);
                break;
            case 3:
                estadoPrograma.escribirArchivo(typeInformation, information);
                break;
            case 4:
                variablesEstaticas.escribirArchivo(typeInformation, information);
                break;
            default:
                System.out.println("Value Type unkwon");
                break;
        }
    }

    public void verificarHilos() {
        // Verificar el hilo Robot
        if (!robot.isAlive()) {
            System.out.println("El hilo robot ha fallado. Relanzando...");
            robot = new DBMSThread(1);
            robot.start();
        }
        // Verificar el hilo Logs
        if (!logs.isAlive()) {
            System.out.println("El hilo logs ha fallado. Relanzando...");
            logs = new DBMSThread(2);
            logs.start();
        }
        // Verificar el hilo Program State
        if (!estadoPrograma.isAlive()) {
            System.out.println("El hilo estadoPrograma ha fallado. Relanzando...");
            estadoPrograma = new DBMSThread(3);
            estadoPrograma.start();
        }
        // Verificar el hilo Static Variables
        if (!variablesEstaticas.isAlive()) {
            System.out.println("El hilo variablesEstaticas ha fallado. Relanzando...");
            variablesEstaticas = new DBMSThread(4);
            variablesEstaticas.start();
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

    public int escribirArchivo(int valor, String info) {
        switch (valor) {
            case 1:
                robots.writeInformation(info);
                break;
            case 2:
                logs.writeInformation(info);
                break;
            case 3:
                programState.writeInformation(info);
                break;
            case 4:
                staticVariables.writeInformation(info);
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

    public String exportData() {
        switch (valor) {
            case 1:
                return robots.exportData();
            case 2:
                return logs.exportData();
            case 3:
                return programState.exportData();
            case 4:
                return staticVariables.exportData();
            default:
                return null;
        }
    }
}

class Procesador extends Thread {
    final Semaphore semaforoCola = DBMS.semaforoCola;
    Queue<String> datosRecibidos;
    private DBMS dbms;

    public Procesador(Queue<String> datosRecibidos, DBMS dbms) {
        this.datosRecibidos = datosRecibidos;
        this.dbms = dbms;
    }

    public void run() {
        while (true) {
            leerDatos();
        }
    }

    private void leerDatos() {
        String data = null;
        boolean dato = false;
        try {
            semaforoCola.acquire();
            if (!datosRecibidos.isEmpty()) {
                data = datosRecibidos.poll();
                dato = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforoCola.release();
        }
        if (dato) {
            String[] dataSplit = data.split(",", 2);
            int typeInformation = Integer.parseInt(dataSplit[0]);
            String information = dataSplit[1];
            dbms.procesarInformacion(typeInformation, information);
        }
    }
}
