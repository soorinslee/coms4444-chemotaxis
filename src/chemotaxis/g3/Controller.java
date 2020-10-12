package chemotaxis.g3;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.SimPrinter;

import chemotaxis.g3.Language;

public class Controller extends chemotaxis.sim.Controller {
    
    Language lang;
    Point lastPoint = new Point(-1,-1);

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
        this.lang = new Language(simPrinter);
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
        // TODO implement UwLR, DwLR, LwUD, RwUD, pause
        // TODO: instruct agent to make it to open field 
        // TODO: find obstacles in path

        simPrinter.println("\ncurrent turn: " + currentTurn);
        // if (lastPoint.equals(currentLocation) || lastPoint.equals(new Point(-1,-1))) {
        if (currentTurn == 1) {
            simPrinter.println("Agent did not move in turn " + (currentTurn - 1) );
            int currentX = currentLocation.x;
            int currentY = currentLocation.y;
            lastPoint.setLocation(currentX, currentY);
            
            ChemicalPlacement chemicalPlacement = new ChemicalPlacement();
            List<ChemicalType> chemicals = new ArrayList<>();

            // calculate angle between agent and target 
            double angle = Math.toDegrees(Math.atan2(this.target.y - currentY, this.target.x - currentX));

            if (angle < 0){
                angle += 360;
            }
                    
            // Pass angle into language --> returns with where to place colors
            String placements = lang.getColor(angle);
            // simPrinter.println("Calculated angle is: " + angle + " degrees.");
            simPrinter.println("Placing new chemical: " + placements);

            // Break apart colors to see where to place, ex => "d_GB"
            if (placements.charAt(0) == 'u') 
                chemicalPlacement.location = new Point(currentX, currentY+1);
            else if (placements.charAt(0) == 'd') 
                chemicalPlacement.location = new Point(currentX, currentY-1);
            else if (placements.charAt(0) == 'l') 
                chemicalPlacement.location = new Point(currentX-1, currentY);
            else if (placements.charAt(0) == 'r') 
                chemicalPlacement.location = new Point(currentX+1, currentY);
            else 
                chemicalPlacement.location = new Point(currentX, currentY);

            if (placements.charAt(1) == 'R') 
                chemicals.add(ChemicalType.RED);
            if (placements.charAt(2) == 'G') 
                chemicals.add(ChemicalType.GREEN);
            if (placements.charAt(3) == 'B') 
                chemicals.add(ChemicalType.BLUE);
        
            chemicalPlacement.chemicals = chemicals;
            
            return chemicalPlacement;
        }

        return new ChemicalPlacement();
	} 	
}
