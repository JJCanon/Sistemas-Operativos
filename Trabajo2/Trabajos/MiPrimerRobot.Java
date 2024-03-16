package Trabajos;

import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.Semaphore;

import static java.lang.System.out;

public class MiPrimerRobot implements Directions
{
    //Semaforos
    static Semaphore semaforoMatriz=new Semaphore(1);
    //Matriz que comparte las pocisiones de los robots
    private static boolean[][] matriz = new boolean[20][20];

    public static void main(String [] args)
    {
        // m = cantidad de mineros, t = cantidad de Trenes || omitimos los estractores ya que por limitaciones del mundo y descripcion del enunciado, solo pueden haber 2 mineros.
        int m,t;

        // Usamos el archivo que creamos del mundo
        World.readWorld("MinaKarel.kwld");
        World.setVisible(true);

        //Argumentos a recibir
        int cantidadTrenes = 2, cantidadExtractores = 2;
        //Variables de color, mínimos y máximos por robos
        Color colorTren = Color.blue;
        Color colorMinero = Color.black;
        Color colorExtractor = Color.red;
        int maxTren = 37, minTren = 2;
        int cantidadMineros = 2; //Por limitaciones del mundo y descripción del enunciado, sólo pueden haber 2 mineros
        int maxExtractor = 5, minExtractor = 2;


        //Arreglos donde se almacenan los robots que crearemos
        Minero[] mineros = new Minero[cantidadMineros];
        Tren[] trenes = new Tren[cantidadTrenes];
        Extractor[] extractores = new Extractor[cantidadExtractores];
        int posStreet=20;
        int posAvenue=20;
        //Creacion de Robot Mineros
        /*for(int i = 0; i < cantidadMineros; i++) {
            mineros[i] = new Minero(posStreet, posAvenue, South, 0, colorMinero, matriz);
            if (posAvenue > 1) {
                posAvenue--;
            }
            else {
                posAvenue=20;
                if(posStreet>1)
                {
                    posStreet--;
                }
            }
        }

        //Creacion de Robots Trenes
        for(int i = 0; i < cantidadTrenes; i++){
            trenes[i] = new Tren(posStreet, posAvenue,South, 0, colorTren,matriz);
            if (posAvenue > 1) {
                posAvenue--;
            }
            else {
                posAvenue=20;
                if(posStreet>1)
                {
                    posStreet--;
                }
            }
        }*/
        //Creacion de Robots Extractores
        for(int i = 0; i < cantidadExtractores; i++){
            extractores[i] = new Extractor(posStreet, posAvenue,South, 0, colorExtractor,matriz);
            extractores[i].run();
            if (posAvenue > 1) {
                posAvenue--;
            }
            else {
                posAvenue=20;
                if(posStreet>1)
                {
                    posStreet--;
                }
            }
        }

    }
}

//Clase para implementar Mineros
class Minero extends Robot{
    final Semaphore semaforoMatriz = MiPrimerRobot.semaforoMatriz;
    int street,avenue;
    Direction direction;
    boolean[][] matriz;
    //Capacidad máxima de beepers que puede almacenar
    private int maxBeepers = 50;
    public Minero (int Street, int Avenue, Direction direction, int beepers, Color color,boolean[][] matriz){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
        this.street=Street;
        this.avenue=Avenue;
        this.direction=direction;
        this.matriz=matriz;
        try {
            MiPrimerRobot.semaforoMatriz.acquire();
            matriz[Street-1][Avenue-1] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MiPrimerRobot.semaforoMatriz.release();
        }
    }

    private void moverAEntrada()
    {
        int streetEntry = 7;
        int avenueEntry = 1;
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);
    }
    private void mover()
    {
        int currentStreet=street,currentAvenue=avenue;
        move();
        if (direction==North)
        {
            street++;

        }
        else if (direction==South)
        {
            street--;
        }else if (direction==East)
        {
            avenue++;
        }
        else if (direction==West)
        {
            avenue--;
        }
        actualizar(currentStreet,currentAvenue);
    }
    private void actualizar(int oldStreet,int oldAvenue)
    {

        try
        {
            semaforoMatriz.acquire();
            matriz[oldStreet-1][oldAvenue-1]=false;
            matriz[street-1][avenue-1]=true;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaforoMatriz.release();
        }
    }
}
//Clase para implementar Trenes
class Tren extends Robot{
    final Semaphore semaforoMatriz = MiPrimerRobot.semaforoMatriz;
    int street,avenue;
    Direction direction;
    //Matriz Compartida
    boolean[][] matriz;
    //Capacidad máxima de beepers que puede almacenar
    private int max = 120;
    public Tren (int Street, int Avenue, Direction direction, int beepers, Color color,boolean[][] matriz){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
        this.street=Street;
        this.avenue=Avenue;
        this.direction=direction;
        this.matriz=matriz;
        try {
            MiPrimerRobot.semaforoMatriz.acquire();
            matriz[Street-1][Avenue-1] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MiPrimerRobot.semaforoMatriz.release();
        }
    }

    private void moverAEntrada()
    {
        int streetEntry = 7;
        int avenueEntry = 1;
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);
        mover();
        out.println(street);
        out.println(avenue);

    }
    private void mover()
    {
        int currentStreet=street,currentAvenue=avenue;
        move();
        if (direction==North)
        {
            street++;

        }
        else if (direction==South)
        {
            street--;
        }else if (direction==East)
        {
            avenue++;
        }
        else if (direction==West)
        {
            avenue--;
        }
        actualizar(currentStreet,currentAvenue);
    }
    private void actualizar(int oldStreet,int oldAvenue)
    {

        try
        {
            semaforoMatriz.acquire();
            matriz[oldStreet-1][oldAvenue-1]=false;
            matriz[street-1][avenue-1]=true;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaforoMatriz.release();
        }
    }
}
//Clase para implementar Extractores
class Extractor extends Robot{
    final Semaphore semaforoMatriz = MiPrimerRobot.semaforoMatriz;
    private int street,avenue;
    private Direction direction;
    //Matriz Compartida
    boolean[][] matriz;
    //Capacidad máxima de beepers que puede almacenar
    private int max = 50;
    int Beepers;
    public Extractor (int Street, int Avenue, Direction direction, int beepers, Color color,boolean[][] matriz){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
        this.street=Street;
        this.avenue=Avenue;
        this.direction=direction;
        this.matriz=matriz;
        Beepers=0;
        try {
            MiPrimerRobot.semaforoMatriz.acquire();
            matriz[Street-1][Avenue-1] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MiPrimerRobot.semaforoMatriz.release();
        }
    }
    private void moverAEntrada()
    {
        //Street=Y Avenue=X
        int streetEntry = 7;
        int avenueEntry = 8;
        while(street!=streetEntry) {
            if(streetEntry<street) {
                girar(West);
                mover();
            }else if(streetEntry>street) {
                girar(East);
                mover();
            }}
        while(avenue!=avenueEntry){
            if (avenueEntry>avenue) {
                girar(North);
                mover();
            } else if (avenueEntry<avenue) {
                girar(South);
                mover();
            }
        }
    }

    private void mover()
    {
        int currentStreet=street,currentAvenue=avenue;
        move();
        if (direction==North)
        {
            avenue++;

        }
        else if (direction==South)
        {
            avenue--;
        }else if (direction==East)
        {
            street++;
        }
        else if (direction==West)
        {
            street--;
        }
        actualizar(currentStreet,currentAvenue);
    }
    private void actualizar(int oldStreet,int oldAvenue)
    {
        try
        {
            semaforoMatriz.acquire();
            matriz[oldStreet-1][oldAvenue-1]=false;
            matriz[street-1][avenue-1]=true;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaforoMatriz.release();
        }
    }
    private void girar(Direction direction)
    {
        if(direction==North) {
            if(this.direction==South) {
                turnLeft();
                turnLeft();
                this.direction=direction;
            } else if (this.direction==West) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=direction;
            } else if (this.direction==East) {
                turnLeft();
                this.direction=direction;
            }

        } else if (direction==South) {
            if(this.direction==North) {
                turnLeft();
                turnLeft();
                this.direction=direction;
            } else if (this.direction==West) {
                turnLeft();
                this.direction=direction;
            } else if (this.direction==East) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=direction;
            }
        } else if (direction==West) {
            if(this.direction==North) {
                turnLeft();
                this.direction=direction;
            } else if (this.direction==South) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=direction;
            } else if (this.direction==East) {
                turnLeft();
                turnLeft();
                this.direction=direction;
            }
        } else if (direction==East) {
            if(this.direction==North) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=direction;
            } else if (this.direction==South) {
                turnLeft();
                this.direction=direction;
            } else if (this.direction==West) {
                turnLeft();
                turnLeft();
                this.direction=direction;
            }
        }
    }
    public void run()
    {
        moverAEntrada();
    }
}