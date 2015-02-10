import java.awt.*;
import java.util.Random;

public class Disk
{

    private int height;
    private int diameter;
    private Color color;


    public Disk(int diameter, int height)
    {
        Random rand = new Random();
        this.diameter = diameter;
        this.height = height;
        //color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        color = Color.GREEN;
    }


    public int getDiameter()
    {
        return diameter;
    }


    public int getHeight()
    {
        return height;
    }

    public Color getColor()
    {
        return color;
    }

}
