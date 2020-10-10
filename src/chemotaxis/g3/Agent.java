package chemotaxis.g3;

import java.util.Map;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.Move;
import chemotaxis.sim.SimPrinter;

public class Agent extends chemotaxis.sim.Agent {

    /**
     * Agent constructor
     *
     * @param simPrinter  simulation printer
     *
     */
	public Agent(SimPrinter simPrinter) {
		super(simPrinter);
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
        Move move = new Move();
        
        // check for new instruction first
        String instructionCheck = checkForInstruction(currentCell, neighborMap);
        
        // if no instructions, check with previous state 
        if (instructionCheck != null) {
            string state = lang.getCoord(instructionCheck);
        }
        else {

        }

        move.currentState = 0;
		
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
        }

        if !(String.valueOf(directions).equals("____")
            return String.valueOf(directions);
        return null;
    }
}