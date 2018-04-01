import javax.swing.*;
/**
 * Controls the FSA Creator, this is where the FSA String disk file is made.
 * @author ach31
 */
public class FSACreator extends JFrame {
    
    public InfoPanel info;
    
    /**
     * Controller class of Part 1.
     * User can create an FSA string on disk file here.
     */
    public FSACreator() {
        setTitle("FSA");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        info = new InfoPanel();
        getContentPane().add(info);
        pack();
        setVisible(true);
    }
    
    
    /**
     * Driver of Part 1. 
     * @param args no arguments should be taken in
     */
    public static void main(String[] args) {
        FSACreator h = new FSACreator();
    }
}