
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
import java.io.FileReader;

//Class
public class logs {
    // atributos
    static String pathFile = "Database/logs.txt";

    // funcion para crear el archivo robots.txt
    public static void crearArchivo() {
        File robotsFile = new File(pathFile);
        if (buscarArchivo(robotsFile) == 1) {
            System.out.println("El archivo logs.txt ya existe");
            return;
        } else {
            try {
                boolean creado = robotsFile.createNewFile();
                if (creado) {
                    writeHeader();
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

    private static int writeHeader() {
        try {
            FileWriter fileWriter = new FileWriter(pathFile);
            fileWriter.write("Time_stamp,id_robot,tipo_robot,Avenue,Street,Beepers,Accion\n");
            fileWriter.close();
            return 1;
        } catch (IOException e) {
            System.out.println("Ocurrio un error al escribir en el archivo: " + e.getMessage());
        }
        return 0;
    }

    public static int writeInformation(String info) {
        try {
            FileWriter fileWriter = new FileWriter(pathFile, true);
            fileWriter.write(info);
            fileWriter.write("\n");
            fileWriter.close();
            return 1;
        } catch (IOException e) {
            System.out.println("Ocurrio un error al escribir en el archivo: " + e.getMessage());
        }
        return 0;
    }

    public static void ReceiveMessage(String Datos) {

    }

    public static void ReadFile() {
        try {
            System.out.println("Empieza lectura de logs");
            FileReader fileReader = new FileReader(pathFile);
            int lineExist = fileReader.read();
            while (lineExist != -1) {
                System.out.print((char) lineExist);
                lineExist = fileReader.read();
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error al leer el archivo: " + e.getMessage());
        }
    }

    public static String searchData(String query) {
        return null;
    }
}