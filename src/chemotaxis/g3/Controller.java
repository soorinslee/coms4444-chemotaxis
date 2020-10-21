package chemotaxis.g3;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.SimPrinter;

import chemotaxis.g3.Language.Translator;
import chemotaxis.g3.PathFinder;

public class Controller extends chemotaxis.sim.Controller {
    
    Point lastPoint = new Point(-1,-1);
    private Translator trans = null;
    private String lastPlacement = null;

    private List<Point> path = null;
    private Point targetLocation = null;
    private Point expectedLocation = start;
    private int steppingStone = 0;
    // private boolean oooo = true;

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
        this.trans = Translator.getInstance();
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
        // TODO: instruct agent to make it to open field 

        Boolean giveInstruction = false;
        // if (!currentLocation.equals(expectedLocation)) {
        if (!inVicinity(currentLocation,expectedLocation,1)) {
            giveInstruction = true;
            simPrinter.println("Expected: " + expectedLocation);
            simPrinter.println("At: " + currentLocation);
        }

        simPrinter.println("\nRound:" + currentTurn);

        // find path
        if (path == null) {
            path = PathFinder.getPath(start, target, grid, size);
            path = PathFinder.cleanPath(path);
            targetLocation = path.get(steppingStone++);
            // PathFinder.triPath(path);
        }

        // see if the cell has deviated from a path 
        expectedLocation = PathFinder.getPath(currentLocation, target, grid, size).get(1);

        // cell's current location
        int currentX = currentLocation.x;
        int currentY = currentLocation.y;

        // check to see if we have made it where we need to
        if (inVicinity(currentLocation,targetLocation,4)) {
        // if (currentLocation.equals(targetLocation)) {
            if (steppingStone < path.size())
                targetLocation = path.get(steppingStone++);
        }

        // TODO: check to see if the agent went in the wrong direction 

        // calculate angle between agent and target 
        // double angle = Math.toDegrees(Math.atan2(target.y - currentY, target.x - currentX));
        double angle = Math.toDegrees(Math.atan2(targetLocation.y - currentY, targetLocation.x - currentX));

        if (angle < 0) 
            angle += 360;

        // Pass angle into language --> returns with where to place colors
        String placements = trans.getColor(angle);
        // simPrinter.println("Calculated angle is: " + angle + " degrees.");
        // simPrinter.println("Placing new chemical: " + placements);

        // if (oooo) {
        //     oooo = false;
        // simPrinter.println("\ncurrent turn: " + currentTurn);
        // if ((giveInstruction) || ( !agentBlocked(currentLocation, grid)
        if (giveInstruction) {
            lastPlacement = placements;
            lastPoint.setLocation(currentX, currentY);
            ChemicalPlacement chemicalPlacement = new ChemicalPlacement();
            List<ChemicalType> chemicals = new ArrayList<>();

            double angle2 = Math.toDegrees(Math.atan2(target.y - currentY, target.x - currentX));
            if (angle2 < 0) 
                angle2 += 360;
            placements = trans.getColor(angle2);

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

        if (( !agentBlocked(currentLocation, grid)
            && (lastPoint.equals(currentLocation) 
            || lastPoint.equals(new Point(-1,-1)) 
            || ((angle%90 == 0) && !(placements.equals(lastPlacement)))))) {
            // simPrinter.println("Agent did not move in turn " + (currentTurn - 1) );
            lastPlacement = placements;
            lastPoint.setLocation(currentX, currentY);

            ChemicalPlacement chemicalPlacement = new ChemicalPlacement();
            List<ChemicalType> chemicals = new ArrayList<>();

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
        
        lastPoint.setLocation(currentX, currentY);
        return new ChemicalPlacement();
    } 	

    private boolean inVicinity(Point a, Point b, int c) {
        return (Math.abs(a.x - b.x) <= c || Math.abs(a.y - b.y) <= c);
    }

    private boolean agentBlocked(Point a, ChemicalCell[][] grid) {
        boolean one = true;
        boolean two = true;
        boolean three = true;
        boolean four = true;
        try { one = grid[a.x+1][a.y].isBlocked();
        } catch (Exception e) { ; }
        try { two = grid[a.x-1][a.y].isBlocked();
        } catch (Exception e) { ; }
        try { three = grid[a.x][a.y+1].isBlocked();
        } catch (Exception e) { ; }
        try { four = grid[a.x][a.y-1].isBlocked();
        } catch (Exception e) { ; }
        return (one || two || three || four); 
        // return (grid[a.x+1][a.y].isBlocked()
        //         || grid[a.x-1][a.y].isBlocked()
        //         || grid[a.x][a.y+1].isBlocked()
        //         || grid[a.x][a.y-1].isBlocked());
    }

}
