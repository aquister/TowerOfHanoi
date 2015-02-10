import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ToHpanel extends JPanel implements Runnable
{
    private final int WINDOW_WIDTH = 1300;
    private final int TOP_SPACE = 50, DISK_SPACE = 5;
    private final int DISK_HEIGHT = 15;
    private final int TOWER_WIDTH = 40;
    private final int MAX_DISK_WIDTH = (WINDOW_WIDTH / 3 - TOWER_WIDTH), MIN_DISK_WIDTH = (5*TOWER_WIDTH/4);

    private int window_height;
    private int wait_time;
    private int disks;
    private int movingCount;
    private int numOfMoves;
    private int time;
    private long startTime;

    private Tower[] towers;
    private ArrayList<Disk>[] diskTowerArray;

    //-----------------------------------------------------------------
    //  Constructor sets up the JPanel
    //-----------------------------------------------------------------
    public ToHpanel ()
    {
        // Set up initial values
        disks = Integer.parseInt(JOptionPane.showInputDialog(null,
                "How many disks do you want to move?", "Disks?", JOptionPane.QUESTION_MESSAGE));
        wait_time = Integer.parseInt(JOptionPane.showInputDialog(null,
                "How many milliseconds to wait between each move?", "Wait time?", JOptionPane.QUESTION_MESSAGE));
        movingCount = 0;
        numOfMoves = (int)(Math.pow(2, disks) - 1);
        time = numOfMoves * wait_time / 1000;
        startTime = System.currentTimeMillis();

        // Calculate height of window using number and size of disks
        window_height = (DISK_HEIGHT + DISK_SPACE) * disks + DISK_SPACE*2 + TOP_SPACE;

        // Create towers and array containing ArrayLists containing Disks
        towers = new Tower[3];
        diskTowerArray = new ArrayList[3];
        for (int i = 0, k = 1; i < 3; i++, k += 2)
        {
            towers[i] = new Tower((k * WINDOW_WIDTH / 6) - (TOWER_WIDTH / 2), window_height-TOP_SPACE, TOWER_WIDTH);
            diskTowerArray[i] = new ArrayList<Disk>();
        }

        // Fill first tower with disks
        int width = MAX_DISK_WIDTH;
        int diff = (MAX_DISK_WIDTH - MIN_DISK_WIDTH) / disks;
        for (int i = 0; i < disks; i++)
        {
            diskTowerArray[0].add(new Disk(width, DISK_HEIGHT));
            width -= diff;
        }

        // Set Window size and background color
        setPreferredSize (new Dimension (WINDOW_WIDTH, window_height));
        setBackground (Color.WHITE);

        // Start moving
        (new Thread(this)).start();

    }

    //-----------------------------------------------------------------
    //  Paint the window; info, towers and disks
    //-----------------------------------------------------------------
    public void paintComponent (Graphics page)
    {
       super.paintComponent (page);

       int timeUsed = (int) (System.currentTimeMillis() - startTime) / 1000;

       // Draw info
       page.drawString("        Disks: " + disks
               + "              Wait time: " + wait_time + " ms"
               + "              Number of moves: " + numOfMoves
               + "              Time: " + time + " sec"
               + "          ||          Movings: " + movingCount
               + "              Time used: " + timeUsed + " sec"
               + "              Movings per second: "
               + ((timeUsed != 0) ? ((wait_time < 1000) ? movingCount/timeUsed : (double)movingCount/timeUsed) : "0"),
               0, 10);

       // Draw towers
       page.setColor(Color.DARK_GRAY);
       for (Tower tower : towers)
           page.fillRect(tower.getxPos(), window_height - tower.getHeight(), tower.getWidth(), tower.getHeight());

       int middle, x, y;

       // Draw disks
       for (int i = 0; i < diskTowerArray.length; i++)
       {
           middle = towers[i].getxPos() + towers[i].getWidth() / 2;
           y = window_height;

           for (int j = 0; j < diskTowerArray[i].size(); j++)
           {
               x = middle - ((Disk)diskTowerArray[i].get(j)).getDiameter() / 2;
               y -= ((Disk)diskTowerArray[i].get(j)).getHeight() + DISK_SPACE;
               page.setColor(((Disk)diskTowerArray[i].get(j)).getColor());
               page.fillRect (x, y, ((Disk)diskTowerArray[i].get(j)).getDiameter(),
                       ((Disk)diskTowerArray[i].get(j)).getHeight());
           }
       }

   }

    //-----------------------------------------------------------------
    //  Performs the initial call to moveTower to solve the puzzle.
    //  Moves the disks from tower 0 to tower 2 using tower 1.
    //-----------------------------------------------------------------
    public void run ()
    {
        try
        {
            JOptionPane.showMessageDialog(null, "Start moving", "Ready", JOptionPane.INFORMATION_MESSAGE);
            startTime = System.currentTimeMillis();
            Thread.sleep(wait_time);
            moveTower (disks, 0, 2, 1);
            JOptionPane.showMessageDialog(null, "Finished moving", "Done", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (InterruptedException ie)
        {
            JOptionPane.showMessageDialog(null, "InterruptedException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //-----------------------------------------------------------------
    //  Moves the specified number of disks from one tower to another
    //  by moving a subtower of n-1 disks out of the way, moving one
    //  disk, then moving the subtower back. Base case of 1 disk.
    //-----------------------------------------------------------------
    private void moveTower (int numDisks, int start, int end, int temp) throws InterruptedException
    {
        if (numDisks == 1)
            moveOneDisk (start, end);
        else
        {
            moveTower (numDisks-1, start, temp, end);
            moveOneDisk (start, end);
            moveTower (numDisks-1, temp, end, start);
        }
    }

    //-----------------------------------------------------------------
    //  Move one disk from one tower to another tower
    //-----------------------------------------------------------------
    private void moveOneDisk (int start, int end) throws InterruptedException
    {
        diskTowerArray[end].add(diskTowerArray[start].get(diskTowerArray[start].size()-1));
        diskTowerArray[start].remove(diskTowerArray[start].size()-1);

        movingCount++;
        repaint();
        Thread.sleep(wait_time);
    }

}