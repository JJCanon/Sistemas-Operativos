package Trabajos;

import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.Semaphore;

public class MiPrimerRobot implements Directions {
    //Semaforos
    static Semaphore semaforoMatriz=new Semaphore(1);
    static Semaphore semaforoMinero=new Semaphore(1);
    static Semaphore semaforoExtractor=new Semaphore(1);
    static Semaphore semaforoCruce = new Semaphore(1);
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
class Minero extends Robot implements Runnable{
    final Semaphore semaforoMatriz = MiPrimerRobot.semaforoMatriz;
    final Semaphore semaforoMinero = MiPrimerRobot.semaforoMinero;
    int street,avenue;
    Direction direction;
    boolean[][] matriz;
    //Capacidad máxima de beepers que puede almacenar
    private int maxBeepers = 50;
    private int beepersInBag;
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
        beepersInBag=0;
    }
    @Override
    public void run() {
        moverAEntrada(8,1);
        moverAMina();
        while(true)
            minar();
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
    private void minar(){
        try{
            semaforoMinero.acquire();
            excavar();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            semaforoMinero.release();
        }
        for (int descargarMinas = 0; descargarMinas < beepersInBag; descargarMinas++) {
            putBeeper();
        }
        beepersInBag=0;
        girar(South);
        mover();
        girar(East);
        mover();
        girar(North);
    }
    private void excavar(){
        mover();
        girar(East);
        for (int movements = 0; movements < 5; movements++)
            mover();
        girar(West);
        buscarMinas();
    }
    private void buscarMinas(){
        for (int movements = 0; movements < 6; movements++) {
            while (nextToABeeper() &&(beepersInBag < maxBeepers)){
                    pickBeeper();
                    beepersInBag++;
            }
            mover();
        }
    }
    private void mover() {
        int currentStreet=street,currentAvenue=avenue;
        //verificar(direction);
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
            return matriz[street-1][avenue-2];
        else return false;
    }
}
//Clase para implementar Trenes
class Tren extends Robot implements Runnable{
    final Semaphore semaforoMatriz = MiPrimerRobot.semaforoMatriz;
    final Semaphore semaforoCruce = MiPrimerRobot.semaforoCruce;
    int street,avenue;
    Direction direction;
    //Capacidad máxima de beepers que puede almacenar
    private int maxBeepers=120;
    //cantidad de beepers en maleta
    private int beepersInBag;
    //Matriz Compartida
    boolean[][] matriz;
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
        beepersInBag=0;
    }
    @Override
    public void run() {
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
        recogerBeepers();
        girar(South);
        for (int movements = 0; movements < 5; movements++)
            mover();
        girar(West);
        for (int movements = 0; movements < 10; movements++)
            mover();
        girar(South);
        for (int movements = 0; movements < 4; movements++)
            mover();
        
        try{
            semaforoCruce.acquire();
            move();
            girar(West);
            mover();
            descargarBeepers();
            girar(East);
            mover();
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            semaforoCruce.release();
        }
    }
    private void mover() {
        int currentStreet=street,currentAvenue=avenue;
       // verificar(direction);
        move();
       // actualizar(currentStreet, currentAvenue);
    }
    private void recogerBeepers(){
        while (nextToABeeper() &&(beepersInBag < maxBeepers)){
            pickBeeper();
            beepersInBag++;
        }
    }
    private void descargarBeepers(){
        for (int descargarMinas = 0; descargarMinas < beepersInBag; descargarMinas++) {
            putBeeper();
        }
        beepersInBag=0;
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
    final Semaphore semaforoCruce = MiPrimerRobot.semaforoCruce;
    private int street,avenue;
    private Direction direction;
    //Matriz Compartida
    boolean[][] matriz;
    //Capacidad máxima de beepers que puede almacenar
    private int maxBeepers = 50;
    private int beepersInBag;
    public Extractor (int Street, int Avenue, Direction direction, int beepers, Color color,boolean[][] matriz){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
        this.street=Street;
        this.avenue=Avenue;
        this.direction=direction;
        this.matriz=matriz;
        beepersInBag=0;
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
    public void run() {
        moverAEntrada(8,1);
        while(true)
            Extraer();
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
    private void Extraer(){
        try{
            semaforoExtractor.acquire();
            buscarBeepers();
            extraerBeepers();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            semaforoExtractor.release();
        }
        almacenarBeepers();
    }
    private void buscarBeepers(){
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

    }
    private void extraerBeepers(){
        try{
            semaforoCruce.acquire();
            mover();
            girar(West);
            while(nextToABeeper() && beepersInBag<maxBeepers) {
            pickBeeper();
            beepersInBag++;
            }
            mover();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            semaforoCruce.release();
        }
        girar(North);
        for (int movements = 0; movements < 6; movements++)
            mover();
        girar(East);
        mover();
        girar(North);
        mover();
        girar(East);
        mover();
        girar(North);
    }
    private void almacenarBeepers(){
        for (int movements = 0; movements < 4; movements++)
            mover();
        girar(East);
        for (int movements = 0; movements < 17; movements++)
            mover();
        girar(North);
        for (int movements = 0; movements < 8; movements++)
            mover();
        descargarBeepers();
        girar(West);
        for (int movements = 0; movements < street-1; movements++)
            mover();
        girar(South);
        for (int movements = 0; movements < 12; movements++)
            mover();
    }
    private void descargarBeepers(){
        for (int beepers = 0; beepers < beepersInBag; beepers++) {
            putBeeper();
        }
    }
    private void mover() {
        int currentStreet=street,currentAvenue=avenue;
        //verificar(direction);
        move();
        actualizar(currentStreet, currentAvenue);
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