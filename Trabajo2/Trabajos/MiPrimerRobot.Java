package Trabajos;

import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.Semaphore;

public class MiPrimerRobot implements Directions
{
    //Semaforos
    static Semaphore semaforoMatriz=new Semaphore(1);
    static Semaphore semaforoMinero=new Semaphore(1);
    static Semaphore semaforoExtractor=new Semaphore(1);
    //Matriz que comparte las pocisiones de los robots
    private static boolean[][] matriz = new boolean[20][20];

    public static void main(String [] args)
    {
        // m = cantidad de mineros, t = cantidad de Trenes || omitimos los estractores ya que por limitaciones del mundo y descripcion del enunciado, solo pueden haber 2 mineros.
        int m,t;

        // Usamos el archivo que creamos del mundo
        World.readWorld("MinaKarel.kwld");
        World.setVisible(true);
        World.setDelay(20);

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
        for(int i = 0; i < cantidadMineros; i++) {
            mineros[i] = new Minero(posStreet, posAvenue, South, 0, colorMinero, matriz);
            Thread HiloMinero=new Thread((mineros[i]));
            HiloMinero.start();
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
            Thread HiloTrenes = new Thread(trenes[i]);
            HiloTrenes.start();
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
        //Creacion de Robots Extractores
        for(int i = 0; i < cantidadExtractores; i++){
            extractores[i] = new Extractor(posStreet, posAvenue,South, 0, colorExtractor,matriz);
            Thread HiloExtractor = new Thread(extractores[i]);
            HiloExtractor.start();
            if (posAvenue > 1) {
                posAvenue--;
            }
            else {
                posAvenue=20;
                if(posStreet>1)
                {
                    posStreet--;
                }else{
                    posStreet=20;
                }
            }
        }

    }
}

//Clase para implementar Mineros
class Minero extends Robot{
    final Semaphore semaforoMatriz = MiPrimerRobot.semaforoMatriz;
    final Semaphore semaforoMinero = MiPrimerRobot.semaforoMinero;
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
            semaforoMatriz.acquire();
            matriz[Street-1][Avenue-1] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforoMatriz.release();
        }
    }
    @Override
    public void run()
    {
        moverAEntrada(8,1);
        moverAMina();
    }
    private void moverAEntrada( int streetE,int avenueE) {
        //Street=Y Avenue=X
        Direction streetD,avenueD;
        int horizontal,vertical;
        if (streetE<this.street) {
            vertical = this.street - streetE;
            streetD = South;
        }
        else {
            vertical = streetE - this.street;
            streetD = North;
        }
        if (avenueE<this.avenue) {
            horizontal = this.avenue - avenueE;
            avenueD = West;
        }
        else {
            horizontal = avenueE - this.avenue;
            avenueD = East;
        }
        girar(avenueD);
        while(horizontal!=0){
            mover();
            horizontal--;
        }
        girar(streetD);
        while(vertical!=0){
            mover();
            vertical--;
        }
    }
    private void moverAMina() {
        girar(East);
        mover();
        girar(South);
        mover();
        girar(West);
        move();
        girar(South);
        for(int movements = 0;movements < 6;movements++)
            mover();
        girar(East);
        for (int movements = 0; movements < 7; movements++)
            mover();
        girar(North);
        for (int movements = 0; movements < 10; movements++)
            mover();
        girar(East);
        for (int movements = 0; movements < 6; movements++)
            mover();
        girar(South);
        mover();
        girar(North);
    }
    private void mover() {
        int currentStreet=street,currentAvenue=avenue;
        move();
        //actualizar(currentStreet, currentAvenue);
    }
    private void girar(Direction direction) {
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
    private void actualizar(int oldStreet,int oldAvenue) {
            if (direction==North)
                avenue++;
            else if (direction==South)
                avenue--;
            else if (direction==East)
                street++;
            else if (direction==West)
                street--;
            try {
                semaforoMatriz.acquire();
                matriz[oldStreet - 1][oldAvenue - 1] = false;
                matriz[street - 1][avenue - 1] = true;
            }catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaforoMatriz.release();
            }
    }
    private boolean verificar(Direction direction) {
        if (direction==North)
            return matriz[street][avenue-1];
        else if (direction==South)
            return matriz[street-2][avenue-1];
        else if (direction==East)
            return matriz[street-1][avenue];
        else if (direction==West)
            return matriz[street-1][avenue];
        else return false;
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
            semaforoMatriz.acquire();
            matriz[Street-1][Avenue-1] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforoMatriz.release();
        }
    }
    @Override
    public void run()
    {
        moverAEntrada(8,1);
        moverAIniciotren();
        while(true)
            realizarRecorrido();
    }
    private void moverAEntrada( int streetE,int avenueE) {
        //Street=Y Avenue=X
        Direction streetD,avenueD;
        int horizontal,vertical;
        if (streetE<this.street) {
            vertical = this.street - streetE;
            streetD = South;
        }
        else {
            vertical = streetE - this.street;
            streetD = North;
        }
        if (avenueE<this.avenue) {
            horizontal = this.avenue - avenueE;
            avenueD = West;
        }
        else {
            horizontal = avenueE - this.avenue;
            avenueD = East;
        }
        girar(avenueD);
        while(horizontal!=0){
            mover();
            horizontal--;
        }
        girar(streetD);
        while(vertical!=0){
            mover();
            vertical--;
        }
    }
    private void moverAIniciotren() {
        girar(East);
        mover();
        girar(South);
        mover();
        girar(West);
        move();
        girar(South);
        for(int movements = 0;movements < 6;movements++)
            mover();
        girar(East);
        mover();
        mover();
    }
    private void realizarRecorrido() {
        for (int movements = 0; movements < 5; movements++)
            mover();
        girar(North);
        for (int movements = 0; movements < 10; movements++)
            mover();
        girar(East);
        for (int movements = 0; movements < 5; movements++)
            mover();
        /*
       Aqui se debe esperar a que hayan beepers
         */
        girar(South);
        for (int movements = 0; movements < 5; movements++)
            mover();
        girar(West);
        for (int movements = 0; movements < 10; movements++)
            mover();
        girar(South);
        for (int movements = 0; movements < 5; movements++)
            mover();
        girar(East);
        /*
        Aqui se deben descargar los beepers
         */
    }
    private void mover() {
        int currentStreet=street,currentAvenue=avenue;
        move();
        //actualizar(currentStreet, currentAvenue);
    }
    private void girar(Direction direction) {
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
    private void actualizar(int oldStreet,int oldAvenue) {
        if (direction==North)
            avenue++;
        else if (direction==South)
            avenue--;
        else if (direction==East)
            street++;
        else if (direction==West)
            street--;
        try {
            semaforoMatriz.acquire();
            matriz[oldStreet - 1][oldAvenue - 1] = false;
            matriz[street - 1][avenue - 1] = true;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaforoMatriz.release();
        }
    }
    private boolean verificar(Direction direction) {
        if (direction==North)
            return matriz[street][avenue-1];
        else if (direction==South)
            return matriz[street-2][avenue-1];
        else if (direction==East)
            return matriz[street-1][avenue];
        else if (direction==West)
            return matriz[street-1][avenue];
        else return false;
    }
}
//Clase para implementar Extractores
class Extractor extends Robot implements Runnable{
    final Semaphore semaforoMatriz = MiPrimerRobot.semaforoMatriz;
    final Semaphore semaforoExtractor=MiPrimerRobot.semaforoExtractor;
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
            semaforoMatriz.acquire();
            matriz[Street-1][Avenue-1] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforoMatriz.release();
        }
    }
    @Override
    public void run()
    {
        moverAEntrada(8,1);
    }
    private void moverAEntrada( int streetE,int avenueE) {
        //Street=Y Avenue=X
        Direction streetD,avenueD;
        int horizontal,vertical;
        if (streetE<this.street) {
            vertical = this.street - streetE;
            streetD = South;
        }
        else {
            vertical = streetE - this.street;
            streetD = North;
        }
        if (avenueE<this.avenue) {
            horizontal = this.avenue - avenueE;
            avenueD = West;
        }
        else {
            horizontal = avenueE - this.avenue;
            avenueD = East;
        }
        girar(avenueD);
        while(horizontal!=0){
            mover();
            horizontal--;
        }
        girar(streetD);
        while(vertical!=0){
            mover();
            vertical--;
        }
    }
    private void mover() {
        int currentStreet=street,currentAvenue=avenue;
        move();
        //actualizar(currentStreet, currentAvenue);
    }
    private void girar(Direction direction) {
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
    private void actualizar(int oldStreet,int oldAvenue) {
        if (direction==North)
            avenue++;
        else if (direction==South)
            avenue--;
        else if (direction==East)
            street++;
        else if (direction==West)
            street--;
        try {
            MiPrimerRobot.semaforoMatriz.acquire();
            matriz[oldStreet - 1][oldAvenue - 1] = false;
            matriz[street - 1][avenue - 1] = true;
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            MiPrimerRobot.semaforoMatriz.release();
        }
    }
    private boolean verificar(Direction direction) {
        if (direction==North)
            return matriz[street][avenue-1];
        else if (direction==South)
            return matriz[street-2][avenue-1];
        else if (direction==East)
            return matriz[street-1][avenue];
        else if (direction==West)
            return matriz[street-1][avenue];
        else return false;
    }
}