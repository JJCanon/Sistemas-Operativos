package Trabajos;

import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.Semaphore;

public class Minero implements Directions {
    //Semaforos
    static Semaphore semaforoMatriz=new Semaphore(1);
    static Semaphore SemaforoMatriz2=new Semaphore(1);
    static Semaphore semaforoMinero=new Semaphore(1);
    static Semaphore semaforoExtractor=new Semaphore(1);
    static Semaphore semaforoCruceET = new Semaphore(1);
    static Semaphore semaforoCruceTM = new Semaphore(1);
    //Matriz que comparte las pocisiones de los robots
    private static boolean[][] matriz = new boolean[20][20];
    private static boolean[][] matriz2=new boolean[20][20];

    public static void main(String[] args) throws InterruptedException {
        // m = cantidad de mineroRobots, t = cantidad de Trenes || omitimos los estractores ya que por limitaciones del mundo y descripcion del enunciado, solo pueden haber 2 mineroRobots.
        int numMineros=0,numTrenes=0,numExtractores=0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-m")) {
                numMineros = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-t")) {
                numTrenes = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-e")) {
                numExtractores = Integer.parseInt(args[i + 1]);
            }
        }
        // Usamos el archivo que creamos del mundo
        World.readWorld("MinaKarel.kwld");
        World.setVisible(true);
        World.setDelay(25);
        //Argumentos a recibir

        //Variables de color, mínimos y máximos por robos
        Color colorTren = Color.blue;
        Color colorMinero = Color.black;
        Color colorExtractor = Color.red;
        int maxTren = 36, minTren = 2;
        int cantidadMineros = 2; //Por limitaciones del mundo y descripción del enunciado, sólo pueden haber 2 mineroRobots
        int maxExtractor = 10, minExtractor = 2;
        int cantidadTrenes = 2, cantidadExtractores = 2;

        if(numTrenes>minTren && numTrenes<maxTren)
            cantidadTrenes=numTrenes;
        if(numExtractores>minExtractor && numExtractores<maxExtractor)
            cantidadExtractores=numExtractores;


        //Arreglos donde se almacenan los robots que crearemos
        MineroRobot[] mineroRobots = new MineroRobot[cantidadMineros];
        Tren[] trenes = new Tren[cantidadTrenes];
        Extractor[] extractores = new Extractor[cantidadExtractores];
        int posStreet=20;
        int posAvenue=20;
        //Creacion de Robot Mineros
        for(int i = 0; i < cantidadMineros; i++) {
            mineroRobots[i] = new MineroRobot(posStreet, posAvenue, South, 0, colorMinero);
            Thread HiloMinero=new Thread((mineroRobots[i]));
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
        }

        //Creacion de Robots Extractores
        for(int i = 0; i < cantidadExtractores; i++){
            extractores[i] = new Extractor(posStreet, posAvenue,South, 0, colorExtractor,matriz2);
            Thread HiloExtractor = new Thread(extractores[i]);
            HiloExtractor.start();
            if (posAvenue > 1) {
                posAvenue= posAvenue-3;
            }
            else {
                posAvenue=20;
                if(posStreet>1)
                {
                    posStreet--;
                }
            }
            Thread.sleep(1500);
        }
    }
}

//Clase para implementar Mineros
class MineroRobot extends Robot implements Runnable{
    final Semaphore semaforoMinero = Minero.semaforoMinero;
    final Semaphore semaforoCruceTM=Minero.semaforoCruceTM;
    int street,avenue;
    Direction direction;
    boolean[][] matriz;
    //Capacidad máxima de beepers que puede almacenar
    private int maxBeepers = 50;
    private int beepersInBag;
    public MineroRobot(int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
        this.street=Street;
        this.avenue=Avenue;
        this.direction=direction;
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
        mover();
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
        for (int movements = 0; movements < 5; movements++)
            mover();
        girar(South);
        mover();
        girar(East);
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
            try{
                semaforoCruceTM.acquire();
                mover();
                for (int descargarMinas = 0; descargarMinas < beepersInBag; descargarMinas++) {
                    putBeeper();
                }
                beepersInBag=0;
                girar(South);
                mover();
                girar(East);
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                semaforoMinero.release();
                semaforoCruceTM.release();
            }
        }
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
        for (int movements = 0; movements < 5; movements++) {
            while (nextToABeeper() &&(beepersInBag < maxBeepers)){
                    pickBeeper();
                    beepersInBag++;
            }
            mover();
        }
        while (nextToABeeper() && beepersInBag <maxBeepers)
        {
            pickBeeper();
            beepersInBag++;
        }
    }
    private void mover() {
        move();
        if (direction==North)
            this.street++;
        else if (direction==South)
            this.street--;
        else if (direction==East)
            this.avenue++;
        else if (direction==West)
            this.avenue--;
    }
    private void girar(Direction direction) {
        if(direction==North) {
            if(this.direction==South) {
                turnLeft();
                turnLeft();
                this.direction=North;
            } else if (this.direction==West) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=North;
            } else if (this.direction==East) {
                turnLeft();
                this.direction=North;
            }

        } else if (direction==South) {
            if(this.direction==North) {
                turnLeft();
                turnLeft();
                this.direction=South;
            } else if (this.direction==West) {
                turnLeft();
                this.direction=South;
            } else if (this.direction==East) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=South;
            }
        } else if (direction==West) {
            if(this.direction==North) {
                turnLeft();
                this.direction=West;
            } else if (this.direction==South) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=West;
            } else if (this.direction==East) {
                turnLeft();
                turnLeft();
                this.direction=West;
            }
        } else if (direction==East) {
            if(this.direction==North) {
                turnLeft();
                turnLeft();
                turnLeft();
                this.direction=East;
            } else if (this.direction==South) {
                turnLeft();
                this.direction=East;
            } else if (this.direction==West) {
                turnLeft();
                turnLeft();
                this.direction=East;
            }
        }
    }
}
//Clase para implementar Trenes
class Tren extends Robot implements Runnable{
    final Semaphore semaforoMatriz = Minero.semaforoMatriz;
    final Semaphore semaforoCruceET = Minero.semaforoCruceET;
    final Semaphore semaforoCruceTM=Minero.semaforoCruceTM;
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
        mover();
        girar(South);
        for(int movements = 0;movements < 6;movements++)
            mover();
        girar(East);
        mover();
        mover();
    }
    private void realizarRecorrido() {
        for (int movements = 0; movements < 5; movements++)
            mover2();
        girar(North);
        for (int movements = 0; movements < 10; movements++)
            mover2();
        girar(East);
        for (int movements = 0; movements < 4; movements++)
            mover2();
        try {
            semaforoCruceTM.acquire();
            mover2();
            recogerBeepers();
            girar(South);
            mover2();
            mover2();
        } catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            semaforoCruceTM.release();
        }
        for (int movements = 0; movements < 3; movements++)
            mover2();
        girar(West);
        for (int movements = 0; movements < 10; movements++)
            mover2();
        girar(South);
        for (int movements = 0; movements < 4; movements++)
            mover2();
        try{
            semaforoCruceET.acquire();
            mover2();
            girar(West);
            mover();
            descargarBeepers();
            girar(East);
            mover();
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            semaforoCruceET.release();
        }
    }
    private void mover() {
        move();
        if (direction==North)
            this.street++;
        else if (direction==South)
            this.street--;
        else if (direction==East)
            this.avenue++;
        else if (direction==West)
            this.avenue--;
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
            matriz[oldStreet - 1][oldAvenue - 1] = false;
            matriz[street - 1][avenue - 1] = true;
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
    private void mover2(){
        boolean movido=false;
        while(!movido)
        {
            try {
                semaforoMatriz.acquire();
                if (!verificar(this.direction)){
                    int currentStreet=street,currentAvenue=avenue;
                    mover();
                    movido=true;
                    actualizar(currentStreet,currentAvenue);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                semaforoMatriz.release();
            }
        }
    }
}
//Clase para implementar Extractores
class Extractor extends Robot implements Runnable{
    final Semaphore semaforoMatriz2 = Minero.SemaforoMatriz2;
    final Semaphore semaforoExtractor=Minero.semaforoExtractor;
    final Semaphore semaforoCruceET = Minero.semaforoCruceET;
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
            mover2();
            horizontal--;
        }
        girar(streetD);
        while(vertical!=0){
            mover2();
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
        mover2();
        matriz[street-1][avenue-1]=false;
        girar(South);
        mover();
        girar(West);
        mover();
        girar(South);
        for(int movements = 0;movements < 6;movements++)
            mover();
        girar(East);

    }
    private void extraerBeepers(){
        try{
            semaforoCruceET.acquire();
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
            semaforoCruceET.release();
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
            mover2();
        girar(East);
        for (int movements = 0; movements < 17; movements++)
            mover2();
        girar(North);
        for (int movements = 0; movements < 8; movements++)
            mover2();
        if (beepersInBag!=0)
            descargarBeepers();
        girar(West);
        moverAEntrada2(8,1);
    }
    private void descargarBeepers(){
        for (int beepers = 0; beepers < beepersInBag; beepers++) {
            putBeeper();
        }
    }
    private void mover() {
        move();
        if (direction==North)
            this.street++;
        else if (direction==South)
            this.street--;
        else if (direction==East)
            this.avenue++;
        else if (direction==West)
            this.avenue--;
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
        matriz[oldStreet - 1][oldAvenue - 1] = false;
        matriz[street - 1][avenue - 1] = true;
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
    private void mover2(){
        boolean movido=false;
        while(!movido)
        {
            try {
                semaforoMatriz2.acquire();
                if (!verificar(this.direction)){
                    int currentStreet=street,currentAvenue=avenue;
                    mover();
                    movido=true;
                    actualizar(currentStreet,currentAvenue);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                semaforoMatriz2.release();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void moverAEntrada2( int streetE,int avenueE) {
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
            mover2();
            horizontal--;
        }
        girar(streetD);
        while(vertical!=0){
            mover2();
            vertical--;
        }
    }
}