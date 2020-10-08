package chemotaxis.g2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.DirectionType;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {
    private ArrayList<Point> shortestPath;
    private ArrayList<Map.Entry<Point, DirectionType>> turns;
    /**
     * Controller constructor
     *
     * @param start       start cell coordinates
     * @param target      target cell coordinates
     * @param size     	 grid/map size
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
        if (currentTurn == 1) {
            shortestPath = getShortestPath(grid);
            turns = getTurnsList();
        }
        /*
        * TODO:
        *  check for indepent agent moves => if yes do that
        *  check for turn
        *  set last move bits
        * */

        return null;
    }

    // TODO: for deliverable
    private ArrayList<Point> getShortestPath(ChemicalCell[][] grid) {
        // after finding shortest path:
        // if (pathCost > budget) => continue to next shortest path
        return null;
    }

    // TODO: for deliverable
    private ArrayList<Map.Entry<Point, DirectionType>> getTurnsList() {
        // goes through shortest path to gets turns
        return null;
    }

    // TODO: for deliverable
    private int getPathCost() {
        return 0;
    }

    /*
    * TODOS:
    *  Longterm diagnol
    * */
}
