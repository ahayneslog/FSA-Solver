import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * A Panel that allows the user to create a FSA 
 * that is represented in a string format.
 * @author Andrew C. haynes
 */
public class InfoPanel extends JPanel {
    
    //JLabels
    private JLabel nums;
    private JLabel alphas;
    private JLabel starts;
    private JLabel finals;
    //JTextFields
    private JTextField numsT;
    private JTextField alphasT;
    private JTextField startsT;
    private JTextField finalsT;
    //JButtons
    private JButton translate;
    //JPanel for Adding new Transition States
    private JPanel transitionS;
    //JLabels for Transition States Panel
    JLabel beginL = new JLabel("Begin State: ");
    JLabel endL = new JLabel("Next State: ");
    JLabel nextCharL = new JLabel("Next Char: "); 
    //TextFields,JButton for Transition Panel
    JTextField begin = new JTextField("");
    JTextField end  = new JTextField("");
    JTextField nextChar = new JTextField("");
    JButton addNewTransition = new JButton("Add");
    //FSA Representation
    private int numOfStates;
    private int startState;
    private ArrayList<Integer> finalStates;
    private String[] alphabet;
    private ArrayList<String> transitions;
    //Error
    private int[] error;
    //Error Count
    private int errorCount;
    private String FSA;
    //Graphics for Displaying FSA
    JTextArea displayFSA;
    
    /**
     * Creates an Info Panel that will go onto the window that allows the user
     * to create an FSA. 
     */
    public InfoPanel() {
        numOfStates = 0;
        startState = 0;
        transitions = new ArrayList<>();
        finalStates = new ArrayList<>();
        FSA = "";
        error = new int[5];
        errorCount = 0;
        this.initializePanel();
    }
    
    /**
     * Initializes a panel that allows the user to create a FSA
     * through starting states, final states, an alphabet, number of states and 
     * transition states. 
     */
    private void initializePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(new Dimension(170,300));
        createTransitionMaker();
        nums = new JLabel("Number of States: ");
        alphas = new JLabel("Alphabet: ");
        starts = new JLabel("Start State: ");
        finals = new JLabel("Final States: ");
        numsT = new JTextField("");
        numsT.setPreferredSize(new Dimension(50, 20));
        alphasT = new JTextField("");
        alphasT.setPreferredSize(new Dimension(99,20));
        startsT = new JTextField("");
        startsT.setPreferredSize(new Dimension(50,20));
        finalsT = new JTextField("");
        finalsT.setPreferredSize(new Dimension(80, 20));
        translate = new JButton("Translate");
        //Number's Row
        JPanel numsP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numsP.add(nums);
        numsP.add(numsT);
        add(numsP);
        //Alphabet's Row
        JPanel alphaP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alphaP.add(alphas);
        alphaP.add(alphasT);
        add(alphaP);
        //Transition's Row
        JPanel transP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        transP.add(transitionS);
        add(transP);
        //Start's Row
        JPanel startsP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startsP.add(starts);
        startsP.add(startsT);
        add(startsP);
        //Final's Row
        JPanel finalsP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        finalsP.add(finals);
        finalsP.add(finalsT);
        add(finalsP);
        //FSA Display
        displayFSA = new JTextArea("");
        JScrollPane paneS = new JScrollPane(displayFSA);
        paneS.setPreferredSize(new Dimension(60,50));
        add(paneS);
        //Button's Row
        JPanel buttonsP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsP.add(translate);
        add(buttonsP);
        translateAction(); //translates the input into a FSA representation
    }
    
    /**
     * Creates a JPanel that allows the user to create a transition state. 
     */
    private void createTransitionMaker() {
        transitionS = new JPanel(new GridLayout(4, 2));
        transitionS.setMinimumSize(new Dimension(100,100));
        transitionS.add(beginL);
        transitionS.add(begin);
        transitionS.add(endL);
        transitionS.add(end);
        transitionS.add(nextCharL);
        transitionS.add(nextChar);
        transitionS.add(new JPanel());
        transitionS.add(addNewTransition);
        //add button action listener, adds a transition function to list of transition functions
        addNewTransition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkTransitionPanel();
            }
        });
    }
    
    /**
     * Error checks all of the inputs and if no error exists, 
     * put the FSA representation into a selected file by the user. 
     */
    private void translateAction() {
        translate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(printError(read()) == true) {
                    removeIllegalTransitions();
                    createFSA();
                    displayFSA.setText(FSA);
                    int option = JOptionPane.showConfirmDialog(null, isNFA() + "Is this the FSA you want?");
                    if(option == JOptionPane.YES_OPTION) {
                        //Use JFileChooser and put the string into a file
                        saveFSA();
                        //clear everything
                        clearFSAandPanel();
                    }
                }
            }
        });
    }
    
    /**
     * read() will check all text fields and make sure they follow the correct format.
     * If they do, they will be part of the FSA. 
     * @return 1 if error in num states, 2 if error in alphabet, 3 if error in transition state, 
     * 4 if error in start state, 5 if error in final states
     * @return error is a stack that contains all errors made by user
     */
    private int[] read() {
        //num of state format: int
        if(isInteger(numsT.getText()) == false)
            error[errorCount++] = 1;
        else 
            numOfStates = Integer.parseInt(numsT.getText());
        //alphabet format: x,y,z
        if(isAlphabetLegal(alphasT.getText()) == false)
            error[errorCount++] = 2;    
        else
            createAlphabet();
        //start state: only one state number that is within range
        if(isInteger(startsT.getText()) == false)
            error[errorCount++] = 3;
        else
            startState = Integer.parseInt(startsT.getText());
        //final states: final states <= number of states
        if(checkFinalStates(finalsT.getText()) == false)
                error[errorCount++] = 4;
        if(transitions.isEmpty()) {
            error[errorCount++] = 5;
        }
        return error;
    }
    
    /**
     * Adds every letter into the alphabet.
     */
    private void createAlphabet() {
        alphabet = alphasT.getText().split(",");
    }
    
    /**
     * Checks if input is indeed a number.
     * @param s string input
     * @return true if it is a number; false if else
     */
    private boolean isInteger(String s) {
        int x;
        //checks if integer
        try { 
            x = Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            x = -1; //it's not a number so return false in number check below
        }
        //checks if integer is greater than -1
        if(x >= 0)
            return true;
        else
            return false;
    }
    
    /**
     * Checks if input is without numbers.
     * @param s string input
     * @return true if it is without numbers, false if else
     */
    private boolean isAlpha(String s) {
        return s.matches("[a-zA-Z]+");
    }
    
    /**
     * Tests if any integers are in the alphabet.
     * @param s alphabet to be tested
     * @return true if legal alphabet, false if else
     */
    private boolean isAlphabetLegal(String s) {
        String[] test;
        test = s.split(",");
        for(String what:test) {
            if(isAlpha(what) == false)
                return false;
        }
        return true;
    }
    
    /**
     * Creates the FSA that's represented as a string.
     */
    private void createFSA() {
        //print FSA
        FSA = numOfStates + ";";
        for(String a: alphabet)
            FSA += a + ",";
        if(FSA.contains(",")) {
            FSA = FSA.substring(0, FSA.lastIndexOf(",")); }
        FSA += ";";
        for(String t: transitions)
            FSA += t + ",";
        if(FSA.contains(",")) {
            FSA = FSA.substring(0, FSA.lastIndexOf(",")); }
        FSA += ";";
        FSA += startState + ";";
        for(int f: finalStates)
            FSA += f + ",";
        if(FSA.contains(",")) {
            FSA = FSA.substring(0, FSA.lastIndexOf(",")); }
        FSA += ";";
    }
    
    /**
     * Removes illegal transitions in the FSA.
     */
    private void removeIllegalTransitions() {
        //removes redundant transitions
        for(int x = 0; x < transitions.size(); x++) {
            for(int y = x + 1; y < transitions.size(); y++) {
                if(transitions.get(x).equals(transitions.get(y))) {
                    transitions.remove(y);
                }
            }
        }
        //Fix begin state and next state writes, character could become 10+
        for(int i = 0; i < transitions.size(); i++) {
            String[] test = transitions.get(i).split(":");
            //if begin state is out of bound, end state is out of bound or char not in alphabet
            String startState = test[0].substring(1);
            String endState = test[1];
            if(Integer.parseInt(startState) >= numOfStates) { 
                transitions.remove(i);
            }
            else if(Integer.parseInt(endState) > numOfStates) {
                transitions.remove(i);
            }    
            else if(!isInAlphabet(transitions.get(i).charAt(5))) { 
                transitions.remove(i);
            }
        }
    }
    
    /**
     * Declares if FSA is an NFA or not. 
     * @return true if NFA, else false if not NFA
     */
    private String isNFA() {
        //starting transition
        for(int x = 0; x < transitions.size(); x++) {
            String[] test = transitions.get(x).split(":");
            int startState = Integer.parseInt(test[0].substring(1));
            char testChar = test[2].charAt(0);
            //next transition
            for(int y = x+1; y < transitions.size(); y++) {
                String[] test1 = transitions.get(y).split(":");
                int startState1 = Integer.parseInt(test1[0].substring(1));
                char testChar1 = test1[2].charAt(0);
                if(startState == startState1) {
                    if(testChar == testChar1)
                        return "This is a NFA.\n";
                }
            }
        }
        return "This is a DFA.\n";
    }
    
    /**
     * Checks input in transition boxes.
     * If all input is legal, add it to transition stack.
     */
    private void checkTransitionPanel() {
        String test = "";
        boolean done = true;
        if(isInteger(begin.getText()) == false) { done = false; } //do nothing
        else {
            String beginState = begin.getText();
            test += "(" + beginState + ":";
        }
        if(isInteger(end.getText()) == false) { done = false; } //do nothing
        else {
            String endState = end.getText();
            test += endState + ":";
        }
        if(isAlpha(nextChar.getText()) == false) { done = false; }
        else {
            String nextCharState = nextChar.getText();
            test += nextCharState + ")";
        }
        if(done) {
            transitions.add(test);
            begin.setText("");
            end.setText("");
            nextChar.setText("");
        }
        else {
            JOptionPane.showMessageDialog(null, "Transition State has an error. Try again.");
        }
    }
    
    /**
     * Checks if every input is an integer and if it's within range of number of states.
     * @param s final states
     * @return true if legal, false if else
     */
    private boolean checkFinalStates(String s) {
        if(numOfStates == 0)
            return false;
        String[] test;
        int x;
        test = s.split(",");
        for(String what: test) {
            if(isInteger(what) == false) {
                return false;
            }
            else {
                x = Integer.parseInt(what);
                if(x > numOfStates) { 
                    return false; 
                } 
                else { 
                    if(finalStates.contains(x)) {} //do nothing
                    else { finalStates.add(x); }
                }
            }
        }
        return true;
    }
    
    /**
     * Sees if nextChar is legal (if it's in the FSA's alphabet).
     * @param x char to check for legal use
     * @return true if legal
     */
    private boolean isInAlphabet(char x) {
        for(String t: alphabet) {
            if(t.charAt(0) == x) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Reports the error of program.
     * @param error contains the error codes
     */
    private boolean printError(int[] error) {
        String reportError = "";
        if(errorCount != 0) {
            for(int x: error) {
                if(x == 1) {
                    reportError += "Error in Number of States.\n"; 
                }
                if(x == 2) {
                    reportError += "Error in Alphabet.\n";
                }
                if(x == 3) {
                    reportError += "Error in Start State\n";
                }
                if(x == 4) {
                    reportError += "Error in Final States.\n";
                }
                if(x == 5) {
                    reportError += "No transitions are available.\n";
                }
            }
            //clear the error stack and reset error stack counter
            for(int x = 0; x < errorCount; x++)
                error[x] = 0;
            errorCount = 0;
            //Report the error
            JOptionPane.showMessageDialog(null, reportError);
            return false;
        }
        return true;
    }
    
    /**
     * Clears everything for a new FSA. 
     */
    private void clearFSAandPanel() {
        numsT.setText("");
        alphasT.setText("");
        begin.setText("");
        end.setText("");
        nextChar.setText("");
        startsT.setText("");
        finalsT.setText("");
        displayFSA.setText("");
        numOfStates = 0;
        startState = 0;
        int i = 0;
        for(String a: alphabet)
            alphabet[i] = "";
        for(int j = 0; j < transitions.size(); j++)
            transitions.remove(j);
        for(int k = 0; k < finalStates.size(); k++)
            finalStates.remove(k);
        FSA = "";
    }
    
    /**
     * Saves the FSA into a file for another program to parse and use for solving inputs. 
     */
    private void saveFSA() {
        JFileChooser save = new JFileChooser(System.getProperty("user.dir"));
        int choice = save.showSaveDialog(null);
        if(choice == JFileChooser.APPROVE_OPTION) {
            try {
                PrintWriter s = new PrintWriter(new File(save.getSelectedFile().getCanonicalPath()));
                s.println(FSA);
                s.close();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null,"FSA cannot be saved.");
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No FSA file saved.");
        }
    }
}