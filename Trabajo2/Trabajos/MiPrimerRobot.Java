import kareltherobot.*;
import java.awt.Color;
public class MiPrimerRobot implements Directions
{
    public static void main(String [] args)
    { Racer first = new Racer(1, 1, East, 0);
        Racer second = new Racer(2, 1, East, 0);
    }
}

class Racer extends Robot
{ public Racer(int Street, int Avenue, Direction direction, int
        beepers)
{ super(Street, Avenue, direction, beepers);
    World.setupThread(this);
}
    public void race()
    { while(! nextToABeeper())
        move();
        pickBeeper();
        turnOff();
    }
    public void run()
    {
        race();
    }
}