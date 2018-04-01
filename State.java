import java.util.ArrayList;

/**
 * A State contains its name, transitions, location in a drawing panel, 
 * size, and if its a starting state or accepting state. 
 * @author Andrew C. Haynes
 */
public class State {
    
    private int name;
    private ArrayList<Transition> transitions;
    private int FSAstart;
    private int FSAfinal;
    private int on;
    private int xLoc;
    private int yLoc;
    private int sizeX;
    private int sizeY;
    
    /**
     * Initializes all of the main attributes of the State object by declaring it's name and 
     * giving it all transitions of the FSA. 
     * @param inputName it's numbered name
     * @param trans all transitions of the FSA, it finds its own and saves it
     */
    public State(int inputName, ArrayList<String> trans) {
        name = inputName;
        FSAstart = 0;
        FSAfinal = 0;
        on = 0;
        xLoc = 0;
        yLoc = 0;
        sizeX = 0;
        sizeY = 0;
        transitions = new ArrayList<>();
        parseTransitions(trans);
    }
    
    /**
     * Parses transition string from FSA. If it's part of this state's transition functions, 
     * add them to this state!
     * @param parseTrans An Array List of strings that are transitions
     */
    public void parseTransitions(ArrayList<String> parseTrans) {
        for(int i = 0; i < parseTrans.size(); i++) {
            String test[] = parseTrans.get(i).split(":");
            int startState = Integer.parseInt(test[0].substring(1)); //take ( out
            //if transition is part of this state, add it!
            if(name == startState) {
                transitions.add(new Transition(Integer.parseInt(test[1]), test[2].charAt(0))); // ) is taken out for test[2]
            }
        }
    }
    
    /**
     * Retrieves name of the state that is a number. 
     * @return state's name as a number
     */
    public int getName() {
        return name;
    }
    
    /**
     * Sets as start state.
     */
    public void setFSAstart() {
        FSAstart = 1;
    }
    
    /**
     * Returns if state is start state or not. 
     * @return true if is start state
     */
    public boolean isStartState() {
        if(FSAstart == 1)
            return true;
        return false;
    }
    
    /**
     * Sets as final state. 
     */
    public void setFSAfinal() {
        FSAfinal = 1;
    }
    
    /**
     * Returns if state is final state or not. 
     * @return true if is final state
     */
    public boolean isFinalState() {
        if(FSAfinal == 1)
            return true;
        return false;
    }
    
    /**
     * Sets state as traversed. 
     */
    public void setOn() {
        on = 1;
    }
    
    /**
     * Sets state as un-traversed.
     */
    public void setOff() {
        on = 0;
    }
    
    /**
     * Is the state on?
     * @return 1 if on, 0 if off
     */
    public int getOn() {
        return on;
    }
    
    /**
     * Returns the x location of the state on the Drawing Board.
     * @return x coordinates
     */
    public int getXLoc() {
        return xLoc;
    }
    
    /**
     * Returns the x location of the state on the Drawing Board.
     * @return y coordinates
     */
    public int getYLoc() {
        return yLoc;
    }
    
    /**
     * Set the State's X location. 
     * @param x the x coordinate
     */
    public void setXLoc(int x) {
        xLoc = x;
    }
    
    /**
     * Set the State's Y location.
     * @param y the y coordinate
     */
    public void setYLoc(int y) {
        yLoc = y;
    }
    
    /**
     * Returns the x location of the state on the Drawing Board.
     * @return x coordinates
     */
    public int getSizeX() {
        return sizeX;
    }
    
    /**
     * Returns the x location of the state on the Drawing Board.
     * @return y coordinates
     */
    public int getSizeY() {
        return sizeY;
    }
    
    /**
     * Set the State's X Size. 
     * @param x the circle's x size
     */
    public void setSizeX(int x) {
        sizeX = x;
    }
    
    /**
     * Set the State's Y Size.
     * @param y the circle's y size
     */
    public void setSizeY(int y) {
        sizeY = y;
    }
    
    /**
     * Returns the number of transitions for this state.
     * @return number of transitions
     */
    public int numTransitions() {
        return transitions.size();
    }
    
    /**
     * Returns a transition.
     * @param i a specific transition in the collection of transitions in state.
     * @return a transition
     */
    public Transition getTransition(int i) {
        return transitions.get(i);
    }
}
