package chemotaxis.g3;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {
	
    /**
     * Controller constructor
     *
     * @param start       start cell coordinates
     * @param target      target cell coordinates
     * @param size     	  grid/map size
     * @param simTime     simulation time
     * @param budget      chemical budget
     * @param seed        random seed
     * @param simPrinter  simulation printer
     *
     */
	public Controller(Point start, Point target, Integer size, Integer simTime, Integer budget, Integer seed, SimPrinter simPrinter) {
		super(start, target, size, simTime, budget, seed, simPrinter);
	}

    /**
     * Apply chemicals to the map
     *
     * @param currentTurn         current turn in the simulation
     * @param chemicalsRemaining  number of chemicals remaining
     * @param currentLocation     current location of the agent
     * @param grid                game grid/map
     * @return                    a cell location and list of chemicals to apply
     *
     */
 	@Override
	public ChemicalPlacement applyChemicals(Integer currentTurn, Integer chemicalsRemaining, Point currentLocation, ChemicalCell[][] grid) {
 		ChemicalPlacement chemicalPlacement = new ChemicalPlacement();
 		
 		int period = Math.max(1, this.simTime / 20);

 		int currentX = currentLocation.x;
 		int currentY = currentLocation.y;
 		
 		int leftEdgeX = Math.max(1, currentX - 5);
 		int rightEdgeX = Math.min(size, currentX + 5);
 		int topEdgeY = Math.max(1, currentY - 5);
 		int bottomEdgeY = Math.min(size, currentY + 5);
 		
 		int randomX = this.random.nextInt(rightEdgeX - leftEdgeX + 1) + leftEdgeX;
 		int randomY = this.random.nextInt(bottomEdgeY - topEdgeY + 1) + topEdgeY ;
 		
 		List<ChemicalType> chemicals = new ArrayList<>();
 		chemicals.add(ChemicalType.BLUE);
 		
 		chemicalPlacement.location = new Point(randomX, randomY);
 		chemicalPlacement.chemicals = chemicals;
 		
 		return chemicalPlacement;
	} 	
}
