/**
 * A transition that a state can do. 
 * @author Andrew C. Haynes
 */
public class Transition {
    
    public int nextState;
    public char nextChar;
    
    /**
     * Creates a transition that a state can do. 
     * @param x name of state
     * @param a character for transition
     */
    public Transition(int x, char a) {
        nextState = x;
        nextChar = a;
    }
    
    /**
     * Returns the next state this transition goes to.
     * @return nextState is the next state this transition goes into
     */
    public int getNextState() {
        return nextState;
    }
    
    /**
     * Returns the character that is expected for the transition.
     * @return expected character for transition
     */
    public char getNextChar() {
        return nextChar;
    }
}
