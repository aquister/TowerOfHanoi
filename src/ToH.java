import javax.swing.*;


public class ToH
{

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Towers of Hanoi");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ToHpanel());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }


}
