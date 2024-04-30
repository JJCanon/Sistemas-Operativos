/*
 * se debe crear la funcion para crear un nuevo archivo .log en caso de que no exista
 * se debe crear la funcion para escribir en el archivo
 * se debe crear la funcion para leer el archivo
 * se debe crear la funcion para eliminar datos del archivo
 */

//Librerías
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Class
public class logs {
    // funcion para crear el archivo robots.txt
    public static void crearArchivo() {
        String pathFile = "Database/logs.txt";
        File robotsFile = new File(pathFile);
        if (buscarArchivo(robotsFile) == 1) {
            System.out.println("El archivo logs.txt ya existe");
            return;
        } else {
            try {
                boolean creado = robotsFile.createNewFile();
                if (creado) {
                    writeHeader(pathFile);
                    return;
                } else
                    System.out.println("No se pudo crear el archivo");
            } catch (IOException e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    // busca el archivo, si existe retorna 1, si no, retorna 0
    private static int buscarArchivo(File robotsFile) {
        if (robotsFile.exists()) {
            return 1;
        }
        return 0;
    }

    private static int writeHeader(String pathFile) {
        try {
            FileWriter fileWriter = new FileWriter(pathFile);
            fileWriter.write("Time_stamp,id_robot,Avenue,Street,Beepers\n");
            fileWriter.close();
            return 1;
        } catch (IOException e) {
            System.out.println("Ocurrio un error al escribir en el archivo: " + e.getMessage());
        }
        return 0;
    }

    public static void ReceiveMessage(String Datos) {

    }

    public static String searchData(String query) {
        return null;
    }
}