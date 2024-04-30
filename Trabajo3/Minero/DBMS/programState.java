
/* 
 * se debe crear funciones para crear el archivo en caso de que no exista
 * se debe crear funciones para leer el archivo
 * se debe crear funciones para agregar un nuevo registro
 * se debe crear funciones para eliminar un registro
 * se debe crear funciones para modificar un registro
 */
//Librerías
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Class
public class programState {
    // funcion para crear el archivo robots.txt
    public static void crearArchivo() {
        String pathFile = "Database/programState.txt";
        File robotsFile = new File(pathFile);
        if (buscarArchivo(robotsFile) == 1) {
            System.out.println("El archivo programState.txt ya existe");
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
            fileWriter.write("Time_stamp,State\n");
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