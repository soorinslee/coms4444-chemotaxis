package chemotaxis.g5;

import java.util.Map;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.Move;
import chemotaxis.sim.SimPrinter;
import chemotaxis.sim.ChemicalCell.ChemicalType;

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
	   // TODO add your code here to move the agent
	   Move move = new Move();
	   // States: 
	   //0: I'm following BLUE
	   //1: I'm following GREEN
	   //2: I'm following RED

		
	   ChemicalType chosenChemicalType;
	   if (previousState == 0) {
		   chosenChemicalType= ChemicalType.BLUE;
	   } 
	   else if (previousState == 1) {
		   chosenChemicalType= ChemicalType.GREEN;
	   } 
	   else {
		   chosenChemicalType= ChemicalType.RED;
	   }
	   boolean atHighest = true;
	   boolean allZero = true;
	   double highestConcentration = currentCell.getConcentration(chosenChemicalType);
	   for(DirectionType directionType : neighborMap.keySet()) {
		   double neighborConcentration = neighborMap.get(directionType).getConcentration(chosenChemicalType);
		   //System.out.println("for loop, neighborConcentration: "+neighborConcentration);
		   //System.out.println("for loop, directionType: "+directionType);
		   if(highestConcentration < neighborConcentration) {
			   highestConcentration = neighborMap.get(directionType).getConcentration(chosenChemicalType);
			   move.directionType = directionType;
			   move.currentState = previousState;
			   atHighest = false;
		   }
		   if (neighborConcentration > 0.01) {
			   allZero = false;
		   }
	   }
	   if (atHighest) {
		   move.directionType = DirectionType.CURRENT;
		   if (!allZero) {
			   move.currentState = (byte) (previousState + 1);
			   if (move.currentState >= 3) {
				   move.currentState = 0;
			   } 
		   }
		   else {
			   move.currentState = previousState;
		   }
	   }

	   //System.out.println("Chemical Type: "+chosenChemicalType);
	   //System.out.println("highestConcentration: "+highestConcentration);
	   //System.out.println("direction: "+move.directionType);
	   //System.out.println("PreviousState: "+previousState);
	   //System.out.println("CurrentState: "+move.currentState);
		
	   return move;
	   // TODO modify the return statement to return your agent move
   }
}