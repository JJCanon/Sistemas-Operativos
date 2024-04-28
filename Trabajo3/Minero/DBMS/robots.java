/*
* se debe crear funciones para crear el archivo en caso de que no exista
* se debe crear funciones para leer el archivo
* se debe crear funciones para agregar un nuevo registro
* se debe crear funciones para eliminar un registro
* se debe crear funciones para modificar un registro
*/

//Librerías
import java.io.File;
import java.io.IOException;

//Class
public class robots {
    // funcion para crear el archivo robots.txt
    public static void crearArchivo() {
        String pathFile = "Database/robots.txt";
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
