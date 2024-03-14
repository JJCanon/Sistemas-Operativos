package Trabajos;

import kareltherobot.*;
import java.awt.Color;
import java.util.Random;

public class MiPrimerRobot implements Directions
{
    public static void main(String [] args)
    {
        // Usamos el archivo que creamos del mundo
        World.readWorld("MinaKarel.kwld");
        World.setVisible(true);

        //Argumentos a recibir
        int cantidadTrenes = 2, cantidadExtractores = 2;
        //Variables de color, mínimos y máximos por robos
        Color colorTren = Color.blue;
        Color colorMinero = Color.black;
        Color colorExtractor = Color.red;
        int maxTre = 37, minTren = 2;
        int cantidadMineros = 2; //Por limitaciones del mundo y descripción del enunciado, sólo pueden haber 2 mineros
        int maxExtractor = 5, minExtractor = 2;


        //Arreglos donde se almacenan los robots que crearemos
        Minero[] mineros = new Minero[cantidadMineros];
        Tren[] trenes = new Tren[cantidadTrenes];
        Extractor[] extractores = new Extractor[cantidadExtractores];
        //Se inicializan los robots aquí
        for(int i = 0; i < cantidadMineros; i++){
            mineros[i] = new Minero(randInt(12,20), randInt(12,20),West, 0, colorMinero);
        }

        for(int i = 0; i < cantidadTrenes; i++){
            trenes[i] = new Tren(randInt(12,20), randInt(12,20),West, 0, colorTren);
        }

        for(int i = 0; i < cantidadExtractores; i++){
            extractores[i] = new Extractor(randInt(12,20), randInt(12,20),West, 0, colorExtractor);
        }

    }

    //Método para generar números aleatorios para las posiciones de los robots
    public static int randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}

//Clase para implementar Mineros
class Minero extends Robot{
    //Capacidad máxima de beepers que puede almacenar
    private int max = 50;
    public Minero (int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
    }
}
//Clase para implementar Trenes
class Tren extends Robot{
    //Capacidad máxima de beepers que puede almacenar
    private int max = 120;
    public Tren (int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
    }
}
//Clase para implementar Extractores
class Extractor extends Robot{
    //Capacidad máxima de beepers que puede almacenar
    private int max = 50;
    public Extractor (int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
    }
}