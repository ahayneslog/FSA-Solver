import java.util.ArrayList;

/**
 * LISP Creator creates a LISP program that solves the FSA declared 
 * in the FSA Solver GUI. 
 * @author Andrew C. Haynes
 */
public class LISPCreator {
    
    //FSA Representation
    private String FSA;
    //The number of states in this FSA
    private int numOfStates;
    //The Starting State
    private int startState;
    //The Final States
    private ArrayList<Integer> finalStates;
    //The Alphabet
    private ArrayList<String> alphabet;
    //Transitions
    private ArrayList<String> transitions;
    //States
    private ArrayList<State> states;
    
    //Write this String to the LISP Source File
    private String writeToFile;
    
    /**
     * LISPCreator will take in a string and 
     * create a LISP Program that resembles the incoming FSA. 
     */
    public LISPCreator(String automaton) {
        FSA = automaton;
        numOfStates = 0;
        startState = 0;
        finalStates = new ArrayList<>();
        alphabet = new ArrayList<>();
        transitions = new ArrayList<>();
        states = new ArrayList<>();
        writeToFile = "";
    }
    
    /**
     * Returns a string that is the LISP Source Code. 
     * @return writeToFile LISP Solver Source Code
     */
    public String createLISP() {
        //parse the FSA to create the states and their transitions, as well as the alphabet and final states
        parseFSA(FSA);
        writeToFile += "(DEFUN fsa (L)\n\t(COND\t((ATOM L) (PPRINT \"Please use a list\"))\n";
        writeToFile += "\t\t(T (STATE" + startState + " L))\n\t)\n)\n";
        //Create the rest of the LISP Source File
        createLISPStateFunctions();
        return writeToFile;
    }
    
    /**
     * Parses the string from the FSA file and attaches the appropriate information
     * to the FSA representation in this program.
     * @param what FSA to be parsed
     */
    private void parseFSA(String what) {
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
     * Creates all of the states in the FSA and puts them into an array of states.
     */
    private void createStates() {
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
     * Creates the state functions in LISP and writes it
     * to the file string.
     */
    private void createLISPStateFunctions() {
        State test = null;
        for(int i = 0; i < states.size(); i++) {
            test = states.get(i);
            if(test.isFinalState()) {
                writeToFile += "(DEFUN STATE" + test.getName() + " (L)\n\t(COND\t((NULL L) (PPRINT \"String is ACCEPTED in State " + test.getName() + ".\"))" +
                        "\n\t\t((ATOM L) (PPRINT \"Please use a list\"))\n";
            }
            else {
                writeToFile += "(DEFUN STATE" + test.getName() + " (L)\n\t(COND\t((NULL L) (PPRINT \"String is REJECTED in State "+ test.getName() + ".\"))" + 
                        "\n\t\t((ATOM L) (PPRINT \"Please use a list\"))\n";
            }
            for(int j = 0; j < test.numTransitions(); j++) {
                char nextChar = test.getTransition(j).getNextChar();
                int nextState = test.getTransition(j).getNextState();
                writeToFile += "\t\t((EQUAL '" + nextChar + " (CAR L)) (STATE" + nextState + " (CDR L)))\n";
            }
            writeToFile += "\t\t(T (PPRINT \"String is REJECTED in State " + test.getName() + ".\"))\n\t)\n)\n";
        }    
    }
}
