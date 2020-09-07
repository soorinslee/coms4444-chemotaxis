package chemotaxis.dummy;

import java.util.Map;

import chemotaxis.sim.ChemicalType;
import chemotaxis.sim.DirectionType;
import chemotaxis.sim.Gradient;
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
     * @param currentGradient  current cell's gradient
     * @param neighborMap      map of cell's neighbors
     * @return                 agent move
     *
     */
	@Override
	public Move makeMove(Integer randomNum, Byte previousState, Gradient currentGradient, Map<DirectionType, Gradient> neighborMap) {
		Move move = new Move();
		
		ChemicalType chosenChemicalType = ChemicalType.BLUE;
		
		double highestConcentration = -1.0;
		for(DirectionType directionType : neighborMap.keySet()) {
			if(highestConcentration < neighborMap.get(directionType).getConcentration(chosenChemicalType)) {
				highestConcentration = neighborMap.get(directionType).getConcentration(chosenChemicalType);
				move.directionType = directionType;
			}
		}
		
		return move;
	}	
}