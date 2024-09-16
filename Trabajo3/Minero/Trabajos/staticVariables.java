
/*
 * se debe crear funciones para crear el archivo en caso de que no exista
 * se debe crear funciones para leer el archivo
 * se debe crear funciones para agregar un nuevo registro
 * se debe crear funciones para eliminar un registro
 * se debe crear funciones para modificar un registro
 */

//Librerías
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Class
public class staticVariables {
    // atributos
    static String pathFile = "Database/staticVariables.txt";

    // funcion para crear el archivo robots.txt
    public static void crearArchivo() {
        File robotsFile = new File(pathFile);
        if (buscarArchivo(robotsFile) == 1) {
            System.out.println("El archivo staticVariables.txt ya existe");
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
            fileWriter.write("Time_Stamp,Avenida_Espera_Ext,Avenida_Espera_Tren,Avenida_Inicial," +
                    "Calle_Extractor,Calle_Minero,Calle_Tren,Calle_Espera_Ext,Beepers_Por_Bodega," +
                    "Beepers_Extractor,Beepers_Minero,Beepers_Tren,Default_Robots,Numero_Bodegas," +
                    "Tipo_Extractor,Tipo_Minero,Tipo_Tren,Veta_Avenida,Veta_Calle,Veta_Espera_Avenida" +
                    "Veta_Espera_Calle\n"); // añadir
                                            // headers
                                            // de
                                            // las
                                            // variables
            // estaticas
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
            fileWriter.write(" \r\n");
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
            // System.out.println("Empieza lectura de staticVariables");
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

   public static void exportData() {
 
    String csvPathFile = "ExportData/staticVariables.csv";
 
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile));
             FileWriter fw = new FileWriter(csvPathFile)) {
 
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < fields.length; i++) {
                    sb.append(fields[i].trim());
                    if (i < fields.length - 1) {
                        sb.append(",");
                    }
                }
                fw.write(sb.toString());
                fw.write("\n");
            }
 
            System.out.println("Conversión completada exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}