/*
 * se debe crear la funcion para crear un nuevo archivo .log en caso de que no exista
 * se debe crear la funcion para escribir en el archivo
 * se debe crear la funcion para leer el archivo
 * se debe crear la funcion para eliminar datos del archivo
 */

//Librerías
import java.io.File;
import java.io.IOException;

//Class
public class logs {
    // funcion para crear el archivo robots.txt
    public static void crearArchivo() {
        String pathFile = "Database/logs.txt";
        File robotsFile = new File(pathFile);
        if (buscarArchivo(robotsFile) == 1) {
            System.out.println("El archivo ya existe");
            return;
        } else {
            try {
                boolean creado = robotsFile.createNewFile();
                if (creado)
                    return;
                else
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
}