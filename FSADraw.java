import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A drawing panel that can draw states in an FSA.
 * @author Andrew C. Haynes
 */
public class FSADraw extends JPanel {
    
    private Graphics2D g2;
    private ArrayList<State> states;
    private ArrayList<String> alphabet;
    private static final int circle = 256;
    private int a = circle / 2;
    private int b = a;
    private int r = 4 * circle / 5;
    
    /**
     * Creates a drawing panel that can draw states in an FSA.
     */
    public FSADraw() {
        //take in the input string and States array
        initDrawingPanel();
        states = new ArrayList<>();
        alphabet = new ArrayList<>();
    }
    
    /**
     * Initializes the Drawing Panel.
     */
    private void initDrawingPanel() {
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    /**
     * Sets States in FSA for Drawing Board to draw. 
     * @param s an array list of states
     */
    public void setStates(ArrayList<State> s) {
        states = s;
    }
    
    /**
     * Draws all states in the FSA.
     */
    private void setStateDrawings() {
        //if FSA exists, draw bubbles of states in a circular motion
        if(states.size() > 0) {
            a = getWidth() / 2; //size of JPanel's Width / 2
            b = getHeight() / 2; //size of JPanel's Height / 2
            int m = Math.min(a, b); //grab the minimum of the two
            r = 4 * m / 5; //create the radius of our circular motion
            int r2 = Math.abs(m - r) / 2; //create the radius of our bubble
            g2.setColor(Color.black);
            for (int i = 0; i < states.size(); i++) {
                double t = 2 * Math.PI * i / states.size();
                int x = (int) Math.round(a + r * Math.cos(t)); //lovely geometry!
                int y = (int) Math.round(b + r * Math.sin(t));
                //set circle in circular motion using bubble's radius
                g2.drawOval(x - r2, y - r2, 2 * r2, 2 * r2);
                //let the i-th state know it's location so that we can draw transitions
                states.get(i).setXLoc(x-r2);
                states.get(i).setYLoc(y-r2);
                states.get(i).setSizeX(2*r2);
                states.get(i).setSizeY(2*r2);
            }
        }
        //write in state number with drawString
        for(int numbers = 0; numbers < states.size(); numbers++) {
            g2.drawString(String.valueOf(states.get(numbers).getName()), states.get(numbers).getXLoc()+20, states.get(numbers).getYLoc()+28);
        }
    }
    
    /**
     * Finishes drawing all of the final states in the FSA.
     */
    private void drawFinalStates() {
        for(int i = 0; i < states.size(); i++) {
            if(states.get(i).isFinalState()) {
                State t = states.get(i);
                g2.drawOval(t.getXLoc()+6, t.getYLoc()+6, 3*t.getSizeX()/4, 3*t.getSizeY()/4);
            }
        }
    }
    
    /**
     * Is it in the middle?
     * @param x a state to check if its in the middle
     * @return 
     */
    private boolean isMiddle(State x) {
        int minimum = 999999999;
        int maximum = 0;
        for(int i = 0; i < states.size(); i++) {
            minimum = Math.min(minimum, states.get(i).getXLoc());
            maximum = Math.max(maximum, states.get(i).getXLoc());
        }
        if(x.getXLoc() > minimum && x.getXLoc() < maximum)
            return true;
        return false;
    }
    
    /**
     * Draw all transitions for each state.
     * NOTE: This function is disgusting because I made sure all paths were covered. 
     */
    private void drawStraightTransitions() {
        //draw straight line for current state to another state
        for(int i = 0; i < states.size(); i++) {
            State currentState = states.get(i);
            for(int j = 0; j < currentState.numTransitions(); j++) {
                int nextState = currentState.getTransition(j).getNextState(); //grab name of next state
                int nextX = states.get(nextState).getXLoc(); //grab next state's x location
                int nextY = states.get(nextState).getYLoc(); //grab next state's y location
                //is the next state higher? (moves arrow to lower states)
                if(nextY > currentState.getYLoc()) {
                    //next state is to the right
                    if(nextX > currentState.getXLoc())
                        if(currentState.getYLoc() >= getHeight()/2)
                            g2.drawLine(currentState.getXLoc()+46, currentState.getYLoc()+20, nextX+20, nextY);
                        else if(currentState.getYLoc() >= getHeight()/2 && nextY < getHeight()/2)
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc()+46, nextX, nextY+20);
                        else
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc()+46, nextX+20, nextY);
                    //next state is to the left
                    else if(nextX < currentState.getXLoc())
                        if(nextY <= getHeight()/2)
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc()+46, nextX+46, nextY+20);
                        else
                            if(currentState.getYLoc() >= getHeight()/2)
                                g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc()+46, nextX+20, nextY);
                            else
                                g2.drawLine(currentState.getXLoc(), currentState.getYLoc()+20, nextX+20, nextY);
                    //next state is in same x coordinates
                    else
                        if(nextX < ((getWidth()/2)-50))  //draws on right side
                            g2.drawLine(currentState.getXLoc()+46, currentState.getYLoc()+20, nextX+46, nextY+20);
                        else if(nextX > (getWidth()/2)-50 && nextX < (getWidth()/2)+50) 
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc()+46, nextX+20, nextY);
                        else { //draws on left side
                            g2.drawLine(currentState.getXLoc(), currentState.getYLoc()+20, nextX, nextY+20);
                        }
                }
                //is the next state lower? (moves arrow to higher states)
                else if(nextY < currentState.getYLoc()) {
                    //next state is right
                    if(nextX > currentState.getXLoc()) {
                        //state position within top half or bottom half changes start locations of line
                        if(currentState.getYLoc() >= getHeight()/2 && nextY >= getHeight()/2)
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+20, nextY+46);
                        else if(currentState.getYLoc() >= getHeight()/2) {
                            if(nextY <= getHeight()/2-50)
                                g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+20, nextY+46);
                            else
                                g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+20, nextY);
                        }
                        else
                            g2.drawLine(currentState.getXLoc()+46, currentState.getYLoc()+20, nextX+20, nextY+46);
                    }
                    //next state is left
                    else if(nextX < currentState.getXLoc()) {
                        //state position within top half or bottom half changes start locations of line
                        if(currentState.getYLoc() >= getHeight()/2 && nextY >= getHeight()/2)
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+46, nextY+20);
                        else if(currentState.getYLoc() >= getHeight()/2) {
                            if(nextY <= getHeight()/2-50)
                                g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+20, nextY+46);
                            else
                                g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+46, nextY+20);
                        }
                        else
                            g2.drawLine(currentState.getXLoc(), currentState.getYLoc()+20, nextX+20, nextY+46);
                    }
                    //next state is above or under
                    else {
                        if(isMiddle(currentState))
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+20, nextY+46);
                        else if(nextX > getWidth()/2) //draw on left
                            g2.drawLine(currentState.getXLoc(), currentState.getYLoc()+20, nextX, nextY+20);
                        else //draw on right
                            g2.drawLine(currentState.getXLoc()+46, currentState.getYLoc()+20, nextX+46, nextY+20);
                    }
                }
                //or are they on the same level? (Y coordinate wise)
                else {
                    //if nextState is right of currentState
                    if(nextX > currentState.getXLoc()) {
                        //start line goes at middle-bottom of top states
                        g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc()+46, nextX+20, nextY+46);
                    }
                    //if nextState is left of currentState
                    else if(nextX < currentState.getXLoc()) {
                        //if similar states are above middle of JPanel, draw above
                        if(currentState.getYLoc() >= (getHeight()/2)+50)
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc(), nextX+20, nextY);
                        //if in middle of height of panel (range of 
                        else if(currentState.getYLoc() > (getHeight()/2)-50 && currentState.getYLoc() < (getHeight()/2)+50) {
                            g2.drawLine(currentState.getXLoc(), currentState.getYLoc()+20, nextX+46, nextY+20); //draw right middle to left middle?
                        }
                        else //draw under 
                            g2.drawLine(currentState.getXLoc()+20, currentState.getYLoc()+46, nextX+20, nextY+46);
                    }
                    //no need for else because if you got here, current state is next state
                    //because of the length of this function, arcs will be drawn in another one
                }
            }
        }
    }
    
    /**
     * Draws all looped transitions to a state that has a transition that goes back
     * to itself. 
     */
    private void drawSelfTransitions() {
        for(int i = 0; i < states.size(); i++) {
            State current = states.get(i);
            for(int j = 0; j < current.numTransitions(); j++) {
                if(current.getTransition(j).nextState == current.getName()) {
                    if(states.get(i).getXLoc() < (getWidth()/2)-50) {
                        //draw on left side
                        g2.drawArc(states.get(i).getXLoc()-25, states.get(i).getYLoc()+10, 30, 30, 50, 270);
                    }
                    else {
                        //draw on right side
                        g2.drawArc(states.get(i).getXLoc()+43, states.get(i).getYLoc()+10, 30, 30, 225, 270);
                    }
                }
            }
        }
    }
    
    private void drawStartArrow() {
        for(State a: states) {
            if(a.isStartState()) {
                g2.drawString("Start", a.getXLoc()+46, a.getYLoc());
            }
        }
    }
    
    /**
     * Draws all states in FSA.
     * @param g Java graphics context
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON); //renders the image to better quality
        setStateDrawings(); //set all state drawings up
        drawFinalStates();
        drawStraightTransitions();
        drawSelfTransitions();
        drawStartArrow();
        //Solving the test string against the FSA has happened, show it!
        //color in current active state when finished with user's test string
        g2.setColor(new Color(0, 204, 204)); //use this color for current state
        for(int colorMe = 0; colorMe < states.size(); colorMe++) {
            if(states.get(colorMe).getOn() == 1)
                g2.fillOval(states.get(colorMe).getXLoc(), states.get(colorMe).getYLoc(), states.get(colorMe).getSizeX(), states.get(colorMe).getSizeY());
        }
        //Redraw final states and numbers. They were "over-painted" by current state.
        g2.setColor(Color.black); //go back to black
        //write in state number with drawString
        for(int numbers = 0; numbers < states.size(); numbers++) {
            g2.drawString(String.valueOf(states.get(numbers).getName()), states.get(numbers).getXLoc()+20, states.get(numbers).getYLoc()+28);
        }
        //will need to redraw final states
        drawFinalStates();    
        
        //Also draw the transition strings
    }
    
}
