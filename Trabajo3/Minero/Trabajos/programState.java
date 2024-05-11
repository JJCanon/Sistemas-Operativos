
/* 
 * se debe crear funciones para crear el archivo en caso de que no exista
 * se debe crear funciones para leer el archivo
 * se debe crear funciones para agregar un nuevo registro
 * se debe crear funciones para eliminar un registro
 * se debe crear funciones para modificar un registro
 */
//Librerías
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Class
public class programState {
    // atributos
    static String pathFile = "Database/programState.txt";

    // funcion para crear el archivo robots.txt
    public static void crearArchivo() {
        File robotsFile = new File(pathFile);
        if (buscarArchivo(robotsFile) == 1) {
            System.out.println("El archivo programState.txt ya existe");
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
            fileWriter.write("Time_stamp,State\n");
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
            System.out.println("Empieza lectura de programState");
            FileReader fileReader = new FileReader(pathFile);
            int valor = fileReader.read();
            while (valor != -1) {
                System.out.print((char) valor);
                valor = fileReader.read();
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