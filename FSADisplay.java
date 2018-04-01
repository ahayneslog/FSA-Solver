import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Creates a JPanel that contains the FSA. 
 * This JPanel can load and solve an FSA from user input. 
 * It can also give help if you're confused on how to use it. 
 * @author Andrew C. Haynes
 */
public class FSADisplay extends JPanel{

    String FSA;
    String testString;
    //FSA Representation
    private int numOfStates;
    private int startState;
    private ArrayList<Integer> finalStates;
    private ArrayList<String> alphabet;
    private ArrayList<String> transitions;
    //Panel parts that should be global to class for easy access
    private JTextField fsaField;
    private JTextField testStringInput;
    private JButton solve;
    private JButton loadFSA;
    private JButton createLISP;
    private JButton createPROLOG;
    //Drawing Panel
    private FSADraw drawingBoard;
    //States
    private ArrayList<State> states;
    //Transition Options
    private JFrame options;
    
    /**
     * Creates the FSA display where the user can input a test string to throw against
     * the FSA that was loaded up from a file and see if the test string is legal or not. 
     */
    public FSADisplay() {
        //initialize variables
        FSA = "";
        testString = "";
        numOfStates = 0;
        startState = -1;
        finalStates = new ArrayList<>();
        alphabet = new ArrayList<>();
        transitions = new ArrayList<>();
        states = new ArrayList<>();
        //intialize the JPanel
        initializeFSADisplay();
        actionListeners();
    }
    
    /**
     * Initializes all of the JComponents for the FSA Display.
     */
    public void initializeFSADisplay() {
        setPreferredSize(new Dimension(700,500));
        setLayout(new BorderLayout());
        //TOP OF FSA DISPLAY
        JPanel top = new JPanel(); 
        top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
        JPanel fsaP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fsaL = new JLabel("FSA: ");
        fsaP.add(fsaL);
        fsaField = new JTextField();
        fsaField.setPreferredSize(new Dimension(580, 20));
        fsaP.add(fsaField);
        //String Row
        JPanel string = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel stringL = new JLabel("String:");
        string.add(stringL);
        testStringInput = new JTextField();
        testStringInput.setPreferredSize(new Dimension(580,20));
        string.add(testStringInput);
        solve = new JButton("Solve");
        string.add(solve);
        top.add(fsaP);
        top.add(string);
        add(top, BorderLayout.NORTH);
        //CENTER OF FSA DISPLAY
        drawingBoard = new FSADraw();
        add(drawingBoard, BorderLayout.CENTER);
        //WEST OF FSA DISPLAY
        JPanel west = new JPanel();
        west.setLayout(new BoxLayout(west, BoxLayout.PAGE_AXIS));
        loadFSA = new JButton("Load");
        createLISP = new JButton("LISP");
        createPROLOG = new JButton("PROLOG");
        west.add(loadFSA);
        west.add(createLISP);
        west.add(createPROLOG);
        add(west, BorderLayout.WEST);
    }

    /**
     * Creates action listeners for all of the buttons on the FSA Display.
     */
    public void actionListeners() {
        //Loads an FSA
        loadFSA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadIt(); //JFileChooser that loads up an FSA string and creates all states for FSA solving
            }
        });
        //Solve an FSA
        solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(solveFSA()) {
                    JOptionPane.showMessageDialog(null, "Test string is accepted.");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Test string is rejected.");
                }
            }
        });
        //Create LISP Program that solves the FSA
        createLISP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LISPCreator hey = new LISPCreator(FSA);
                String createMe = hey.createLISP();
                int option = JOptionPane.showConfirmDialog(null, "Turn this FSA into a LISP Solver?");
                if(option == JOptionPane.YES_OPTION) {
                    //Use JFileChooser and put the string into a file
                    saveLISP(createMe);
                } 
            }
        });
        //Create a PROLOG Program that solves the FSA
        createPROLOG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PROLOGCreator hey = new PROLOGCreator(FSA);
                String createMe = hey.createPROLOG();
                int option = JOptionPane.showConfirmDialog(null, "Turn this FSA into a PROLOG Solver?");
                if(option == JOptionPane.YES_OPTION) {
                    //Use JFileChooser and put the string into a file
                    savePROLOG(createMe);
                } 
            }
        });
    }
    
    /**
     * Loads a file and grabs the FSA representation string.
     */
    public void loadIt() {
        clearFSA();
        File loadedGrid = null;
        int yes = 0;
        String what = ""; //string to be parsed
        JFileChooser choose = new JFileChooser(System.getProperty("user.dir"));
        int returnVal = choose.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            loadedGrid = choose.getSelectedFile();
            yes = 1;
        }
        else
            JOptionPane.showMessageDialog(null, "No file selected.");
        if(yes == 1) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(loadedGrid));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    what = line;
                }
                reader.close();
            } 
            catch (IOException x) {
                System.err.println(x);
            }
            //Go ahead and start parsing the string appropriately
            FSA = what;
            fsaField.setText(FSA);
            parseFSA(what);
            drawingBoard.setStates(states);
            drawingBoard.repaint();
            if(options == null)
                options = createTransitionOptionFrame();
            else {
                options.dispose();
                options = createTransitionOptionFrame();
            }
        }
    }
    
    /**
     * Parses the string from the FSA file and attaches the appropriate information
     * to the FSA representation in this program.
     * @param what FSA to be parsed
     */
    public void parseFSA(String what) {
        String[] input = what.split(";");
        //make sure FSA has the right amount of semicolons
        if(input.length == 5) {
            //Number of States
            numOfStates = Integer.parseInt(input[0]); //make sure to do error checking later
            //Alphabet
            String[] alpha = input[1].split(",");
            for(int i = 0; i < alpha.length; i++) {
                alphabet.add(alpha[i]);
            }
            //Transitions
            String[] trans = input[2].split(",");
            for(int j = 0; j < trans.length; j++) {
                transitions.add(trans[j]);
            }
            //Start State
            startState = Integer.parseInt(input[3]);
            //Final States
            String [] finals = input[4].split(",");
            for(int k = 0; k < finals.length; k++) {
                finalStates.add(Integer.parseInt(finals[k]));
            }
            //check everything in FSA variables before making states
            createStates();
        }
    }
    
    /**
     * Creates a JFrame that will state every transition that is possible for the 
     * current FSA on the screen. 
     * @return JFrame that contains transition information.
     */
    private JFrame createTransitionOptionFrame() {
        JFrame current = new JFrame();
        current.setTitle("Transitions");
        current.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        current.setResizable(false);
        current.setLocationRelativeTo(this);
        //Create a JPanel that will go on a scroll pane
        JPanel display = new JPanel();
        JTextArea writeStates = new JTextArea("");
        String displayTrans = "";
        for(int i = 0; i < transitions.size(); i++) {
            displayTrans += transitions.get(i) + "\n";
        }
        writeStates.setText(displayTrans);
        JScrollPane paneS = new JScrollPane(writeStates);
        paneS.setPreferredSize(new Dimension(200,200));
        display.add(paneS);
        current.getContentPane().add(display);
        current.pack();
        current.setVisible(true);
        return current;
    }
    
    /**
     * Creates all of the states in the FSA and puts them into an array of states.
     */
    public void createStates() {
        //create all states for FSA
        for(int i = 0; i < numOfStates; i++) {
            states.add(new State(i, transitions)); //add state and transitions to States collection
        }
        //set the start state
        for(int j = 0; j < numOfStates; j++) {
            if(startState == states.get(j).getName()) {
                states.get(j).setFSAstart();
            }
        }
        //set final states
        for(int k = 0; k < numOfStates; k++) {
            for(int l = 0; l < finalStates.size(); l++) {
                //is current state with k also a final state? If so, set it as one
                if(finalStates.get(l) == states.get(k).getName())  
                    states.get(k).setFSAfinal();
            }
        }
    }
    
    /**
     * Clears all FSA representations.
     */
    public void clearFSA() {
        numOfStates = 0;
        startState = -1;
        finalStates = new ArrayList<>();
        alphabet = new ArrayList<>();
        transitions = new ArrayList<>();
        states = new ArrayList<>();
        FSA = "";
        testString = "";
    }
    
    /**
     * Tests if the user's inputted string is accepted or rejected with the current FSA.
     * @return true if accepted
     */
    public boolean solveFSA() {
        clearOnStates();
        drawingBoard.repaint();
        //grab test string from user
        testString = testStringInput.getText();
        //find start state in FSA
        State currentState = findStartState();
        //if one exist, go ahead and test input string against this FSA!
        if(currentState != null) {
            //go through test string
            for(int i = 0; i < testString.length(); i++) {
                //used for illegal characters
                int noChar = 0;
                //for animation purposes
                states.get(states.indexOf(currentState)).setOn();
                //test c against all possible transitions
                char c = testString.charAt(i);
                for(int j = 0; j < currentState.numTransitions(); j++) {
                    //grab a transition and test c against it
                    Transition currentTransition = currentState.getTransition(j);
                    //if there is a transition, move to the next state
                    if(c == currentTransition.nextChar) {
                        states.get(states.indexOf(currentState)).setOff();
                        currentState = findState(currentTransition.nextState);
                        noChar = 1;
                        if(currentState == null) {
                            return false; //means this state does not exist and transition was false
                        }
                        states.get(states.indexOf(currentState)).setOn();
                    }
                }
                if(noChar == 0) {
                    return false;
                }
            }
            //test if in final state. If so, return true
            if(currentState.isFinalState()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Finds the start state in the FSA. 
     * @return the start state
     */
    public State findStartState() {
        for(int i = 0; i < states.size(); i++) {
            if(states.get(i).isStartState())
                return states.get(i);
        }
        return null;
    }
    
    /**
     * Finds a state in the FSA. 
     * @param j a specific state with this integer name
     * @return the specified state
     */
    public State findState(int j) {
        for(int i = 0; i < states.size(); i++) {
            if(states.get(i).getName() == j) {
                return states.get(i);
            }
        }
        return null;
    }
    
    /**
     * Clears the traversal of each state on Drawing Board.
     */
    public void clearOnStates() {
        for(int a = 0; a < states.size(); a++) {
            states.get(a).setOff();
        }
    }
    
    /**
     * Saves the FSA into a file for another program to parse and use for solving inputs. 
     */
    private void saveLISP(String LISP) {
        JFileChooser save = new JFileChooser(System.getProperty("user.dir"));
        int choice = save.showSaveDialog(null);
        if(choice == JFileChooser.APPROVE_OPTION) {
            try {
                PrintWriter s = new PrintWriter(new File(save.getSelectedFile().getCanonicalPath()));
                s.println(LISP);
                s.close();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null,"FSA cannot be saved.");
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No LISP Solver file saved.");
        }
    }
    
    /**
     * Saves the FSA into a file for another program to parse and use for solving inputs. 
     */
    private void savePROLOG(String PROLOG) {
        JFileChooser save = new JFileChooser(System.getProperty("user.dir"));
        int choice = save.showSaveDialog(null);
        if(choice == JFileChooser.APPROVE_OPTION) {
            try {
                PrintWriter s = new PrintWriter(new File(save.getSelectedFile().getCanonicalPath()));
                s.println(PROLOG);
                s.close();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null,"FSA cannot be saved.");
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No LISP Solver file saved.");
        }
    }
}
