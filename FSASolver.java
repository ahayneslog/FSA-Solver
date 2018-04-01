import javax.swing.JFrame;

/**
 * Takes in a file and if the file contains a legal FSA represented as a string, 
 * see if the second input (a string to be tested against the FSA) is legal 
 * in the given FSA. 
 * @author Andrew C. Haynes
 */
public class FSASolver extends JFrame {
    private FSADisplay fsa;
    
    /**
     * A Finite State Automaton solver that receives a 
     * file that contains a FSA string and a string 
     * to be tested against that FSA. 
     */
    public FSASolver() {
        //Let this class be a JFrame that holds the FSADisplay JPanel
        setTitle("FSA Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        fsa = new FSADisplay();
        getContentPane().add(fsa);
        pack();
        setVisible(true);
    }

    
    public static void main(String[] args) {
        FSASolver fs = new FSASolver();
    }
}
