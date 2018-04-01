import java.util.ArrayList;

/**
 * Creates a PROLOG Solver for the FSA in FSA Solver GUI and 
 * returns the PROLOG source code. 
 * @author Andrew C. Haynes
 */
public class PROLOGCreator {
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
     * PROLOGCreator will take in a string and 
     * create a PROLOG Program that resembles the incoming FSA. 
     */
    public PROLOGCreator(String automaton) {
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
     * Creates a PROLOG Solver for the FSA in FSA Solver GUI and 
     * returns the PROLOG source code. 
     * @return PROLOG FSA Solver source code
     */
    public String createPROLOG() {
        //parse the FSA to create the states and their transitions, as well as the alphabet and final states
        parseFSA(FSA);
        createAlphabet();
        createStateRules();
        System.out.println(writeToFile);
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
     * Adds alphabet to PROLOG Solver. 
     */
    private void createAlphabet() {
        for(int i = 0; i < alphabet.size(); i++) {
            writeToFile += "is" + alphabet.get(i) + "(" + alphabet.get(i) + ").\n";
        }
        //adds final state fact
        writeToFile += "isFinal(yes).\n\n";
    }
    
    /**
     * Creates the rules for states. 
     * Allows resolution to happen for PROLOG. 
     */
    private void createStateRules() {
        int startMe = 0;
        for(int i = 0; i < states.size(); i++) {
            int name = states.get(i).getName();
            if(states.get(i).isStartState()) {
                startMe = states.get(i).getName();
            }
            if(states.get(i).isFinalState()) {
                writeToFile += "state" + name + "([]) :- isFinal(yes).\n";
            }
            for(int j = 0; j < states.get(i).numTransitions(); j++) {
                writeToFile += "state" + name + "([H|T]) :- " + "is" + states.get(i).getTransition(j).nextChar + "(H) -> state"
                        + states.get(i).getTransition(j).nextState + "(T).\n"; 
            }
        }
        //Write FSA Functor
        writeToFile += "fsa(List) :- state" + startMe + "(List).\n";
    }
}
