package chemotaxis.g3;

import java.util.Map;
import chemotaxis.g3.Language.Translator;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import sun.font.TrueTypeFont;
import chemotaxis.sim.Move;
import chemotaxis.sim.SimPrinter;

public class Agent extends chemotaxis.sim.Agent {

    private Translator trans = null;

    /**
     * Agent constructor
     *
     * @param simPrinter  simulation printer
     *
     */
	public Agent(SimPrinter simPrinter) {
        super(simPrinter);
        trans = Translator.getInstance();
	}

    /**
     * Move agent
     *
     * @param randomNum        random number available for agents
     * @param previousState    byte of previous state
     * @param currentCell      current cell
     * @param neighborMap      map of cell's neighbors
     * @return                 agent move
     *
     */
	@Override
	public Move makeMove(Integer randomNum, Byte previousState, ChemicalCell currentCell, Map<DirectionType, ChemicalCell> neighborMap) {
        // TODO: self-realize silent + n/a, pause
        // TODO: try to get to a position where you can recieve the most instructions 
        // TODO: start moving erratically if haven't had instructions for a bit 
        Move move = new Move();
        Integer prevByte = (int) previousState;
        char[] nextState = new char[9];
        
        // check for new instruction first
        String instructionCheck = checkForInstructions(currentCell, neighborMap);
        String prevState = null;

        // simPrinter.println("New instruction: " + instructionCheck);
        
        // if instruction exists, get the new state
        if (instructionCheck != null) {
            prevState = trans.getState(instructionCheck, prevByte);
        }
        else {
            // if no instruction exists, keep running with the previous state
            prevState = trans.getState(prevByte);
        }

        //  X    ±    N   [.*]  Y    ±    M   [.*] [C/R]
        // '0', '+', '0', '*', '0', '+', '0', '.', 'C'
        //  0    1    2    3    4    5    6    7    8

        // based on the prevState, check the surroundings and find an opening for the next move 
        if (prevState.equals("pause")) {
            // simPrinter.println("Agent is paused");
            move.directionType = DirectionType.CURRENT;
            nextState = "pause".toCharArray();
        }
        else if (blocked(prevState, neighborMap)) {
            nextState = moveWithBlock(nextState, prevState, neighborMap);
            move.DirectionType = blockMoveDirection(nextState);
        }
        else if (mobilityUp(prevState, neighborMap)) {
            // simPrinter.println("Agent can + should move east/up");
            nextState =  moveInY(nextState, prevState);
            move.directionType = DirectionType.EAST;
        }
        else if (mobilityDown(prevState, neighborMap)) {
            // simPrinter.println("Agent can + should move west/down");
            nextState =  moveInY(nextState, prevState);
            move.directionType = DirectionType.WEST;
        }
        else if (mobilityLeft(prevState, neighborMap)) {
            // simPrinter.println("Agent can + should move north/left");
            nextState = moveInX(nextState, prevState);
            move.directionType = DirectionType.NORTH;
        }
        else if (mobilityRight(prevState, neighborMap)) {
            // simPrinter.println("Agent can + should move south/right");
            nextState = moveInX(nextState, prevState);
            move.directionType = DirectionType.SOUTH;
        }
        else {
            nextState = "pause".toCharArray();
            move.directionType = DirectionType.CURRENT;
        }

        // translate to Byte for memory 
        Byte nextByte = trans.getByte(nextState);
        // simPrinter.println("Byte for next round: " + nextByte);
        // simPrinter.println("State for next round: " + String.valueOf(nextState));
        move.currentState = nextByte;
		return move;
    }
    
    private String checkForInstructions(ChemicalCell currentCell, Map<DirectionType, ChemicalCell> neighborMap) {
        char[] directions = new char[] {'_','_','_','_'};

        if (currentCell.getConcentration(ChemicalType.RED) == 1)
            directions[1] = 'R';
        if (currentCell.getConcentration(ChemicalType.GREEN) == 1)
            directions[2] = 'G';
        if (currentCell.getConcentration(ChemicalType.BLUE) == 1)
            directions[3] = 'B';

        char temp = ' ';
        for (DirectionType directionType : neighborMap.keySet()) {
            if (directionType == DirectionType.SOUTH) 
                temp = 'r';
            else if (directionType == DirectionType.NORTH) 
                temp = 'l';
            else if (directionType == DirectionType.EAST) 
                temp = 'u';
            else
                temp = 'd';

            if (neighborMap.get(directionType).getConcentration(ChemicalType.RED) == 1) {
                directions[0] = temp;
                directions[1] = 'R';
            }
            if (neighborMap.get(directionType).getConcentration(ChemicalType.GREEN) == 1) {
                directions[0] = temp;
                directions[2] = 'G';
            }
            if (neighborMap.get(directionType).getConcentration(ChemicalType.BLUE) == 1) {
                directions[0] = temp;
                directions[3] = 'B';
            }
            if (!(String.valueOf(directions).equals("____"))) 
                break;
        }

        if (!(String.valueOf(directions).equals("____")))
            return String.valueOf(directions);
        return null;
    }

    private Boolean mobilityUp(String state, Map<DirectionType, ChemicalCell> surroundings) {
        // There's space above this cell
        // and the instuction is moving in that direction
        // and moving up is possible according to state
        // and either – not resetting AND you can move up in this cycle 
        //         or – are resetting AND you have not maxed out moves in that axis for this cycle 
        return (state.charAt(5) == '+'
                && ((state.charAt(8) == 'C' && state.charAt(4) <= state.charAt(6) - 1) 
                    || (state.charAt(8) == 'R' && state.charAt(4) == state.charAt(6) - 1))
                && surroundings.get(DirectionType.EAST).isOpen()
        );
    }

    private Boolean mobilityDown(String state, Map<DirectionType, ChemicalCell> surroundings) {
        return (state.charAt(5) == '-'
                && ((state.charAt(8) == 'C' && state.charAt(4) <= state.charAt(6) - 1) 
                    || (state.charAt(8) == 'R' && state.charAt(4) == state.charAt(6) - 1))
                && surroundings.get(DirectionType.WEST).isOpen()
        );
    }

    private Boolean mobilityLeft(String state, Map<DirectionType, ChemicalCell> surroundings) {
        return (state.charAt(1) == '-'
                && ((state.charAt(8) == 'C' && state.charAt(0) <= state.charAt(2) - 1) 
                    || (state.charAt(8) == 'R' && state.charAt(0) == state.charAt(2) - 1))
                && surroundings.get(DirectionType.NORTH).isOpen()
        );
    }

    private Boolean mobilityRight(String state, Map<DirectionType, ChemicalCell> surroundings) {
        return (state.charAt(1) == '+'
                && ((state.charAt(8) == 'C' && state.charAt(0) <= state.charAt(2) - 1) 
                    || (state.charAt(8) == 'R' && state.charAt(0) == state.charAt(2) - 1))
                && surroundings.get(DirectionType.SOUTH).isOpen()
        );
    }

    private Boolean blocked(String state, Map<DirectionType, ChemicalCell> surroundings) {
        return (followingWall(state) || blockedInX(surroundings) || blockedInY(surroundings));
    }

    private char[] moveWithBlock(char[] nextState, String prevState, Map<DirectionType, ChemicalCell> surroundings) {
        // if you previously were blocked in an axis and no longer am
        if (followingWall(prevState) && prevState.charAt(0) == 'Y' && !blockedInY(surroundings) {
            // take the movement from before and apply it to you now if possible
            // if you are blocked in that direction, turn around and follow the previous wall in the opposite direction
        }

        if (followingWall(prevState) && prevState.charAt(0) == 'X' && !blockedInX(surroundings) {

        }
        }
        // if you were previously blocked and still are 
            // which axis are you blocked in?
            // both
                // turn around, follow previous wall in opposite direction
            // new axis, but not old
                // follow axis in preferable directoin if possible
            // same axis
                // continue on your way 
        
        // if you were not blocked before and now you are 
        if (!followingWall(prevState) && blockedInX(surroundings)) {
            // see if you can continue moving in your position position
                // if not, translate to blocked language 
                // if so, move in that direction and keep going 
        }

        if (!followingWall(prevState) && blockedInY(surroundings)) {

        }

        // if moving in perpendicular manner (regardless of your previous movements) 
        // and not don't know which direction to move in now that you've hit something 
            // is there a way to move that isn't where you came? 
            // random 
            
        try to go where you want
        try to go somewhere else that you want
        try not to go back from where you came
        go back from where you came 

    
    
        
        return nextState;
    }

    private DirectionType blockMoveDirection(char[] nextState) {
        if (nextState[8] == 'W') {
            if (nextState[1] == 'R') return DirectionType.SOUTH;
            else if (nextState[1] == 'L') return DirectionType.NORTH;
            else if (nextState[1] == 'U') return DirectionType.EAST;
            else  return DirectionType.WEST;
        }
        else {
            if (nextState[3] == '*' && nextState[7] == '.') {
                if (nextState[1] == '+') return DirectionType.SOUTH;
                else return DirectionType.NORTH;
            }
            else {
                if (nextState[5] == '+') return DirectionType.EAST;
                else return DirectionType.WEST;
            }
        }
    }

    private Boolean blockedInX(Map<DirectionType, ChemicalCell> surroundings) {
        return (surroundings.get(DirectionType.NORTH).isBlocked()
                || surroundings.get(DirectionType.SOUTH).isBlocked());
    }

    private Boolean blockedInY(Map<DirectionType, ChemicalCell> surroundings) {
        return (surroundings.get(DirectionType.WEST).isBlocked()
                || surroundings.get(DirectionType.EAST).isBlocked());
    }

    // creates new state string according to translation laws, preserving direction
    private char[] moveInX(char[] nextState, String prevState) {
        if (needsToRepeat(prevState)) {
            nextState = new char[] { '0', prevState.charAt(1), prevState.charAt(2), '*',
                                     '0', prevState.charAt(5), prevState.charAt(6), '.', 'C'};
            if (nextState[2] == '0' || nextState[6] == '0')
                nextState[8] = 'R';
        }
        else {
            nextState = new char[] { (char)((int) prevState.charAt(0) + 1), prevState.charAt(1), prevState.charAt(2), '*',
                                     prevState.charAt(4), prevState.charAt(5), prevState.charAt(6), '.', 'C'};
            if ((nextState[4] == (char)((int)nextState[6] - 1) && nextState[0] == nextState[2]) ||
                (nextState[0] == (char)((int)nextState[2] - 1) && nextState[4] == nextState[6]))
                nextState[8] = 'R';
        } 
        return nextState;
    }

    private char[] moveInY(char[] nextState, String prevState) {
        if (needsToRepeat(prevState)) {
            nextState = new char[] { '0', prevState.charAt(1), prevState.charAt(2), '.',
                                     '0', prevState.charAt(5), prevState.charAt(6), '*', 'C'};
            if (nextState[2] == '0' || nextState[6] == '0')
                nextState[8] = 'R';
        }
        else {
            nextState = new char[] { prevState.charAt(0), prevState.charAt(1), prevState.charAt(2), '.',
                                     (char)((int) prevState.charAt(4) + 1), prevState.charAt(5), prevState.charAt(6), '*', 'C'};
            if ((nextState[4] == (char)((int)nextState[6] - 1) && nextState[0] == nextState[2]) ||
                (nextState[0] == (char)((int)nextState[2] - 1) && nextState[4] == nextState[6]))
                nextState[8] = 'R';
        }
        return nextState;
    }

    private Boolean needsToRepeat(String state) {
        return state.charAt(8) == 'R';
    }

    private Boolean followingWall(String state) {
        return state.charAt(8) == 'W';
    }
    

}