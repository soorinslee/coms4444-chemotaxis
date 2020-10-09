package chemotaxis.g2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.DirectionType;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {
    private final DirectionType INITIAL_AGENT_DIR = DirectionType.NORTH;
    private ArrayList<Point> shortestPath;
    private ArrayList<Map.Entry<Point, DirectionType>> turns;
    private DirectionType prevDir;
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
        this.prevDir = INITIAL_AGENT_DIR;
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
        ChemicalPlacement cp = new ChemicalPlacement();
        if (currentTurn == 1) {
            shortestPath = getShortestPath(grid);
            turns = getTurnsList();
        }

        Map.Entry<Point, DirectionType> nextTurn = turns.get(0);
        if (nextTurn.getKey().equals(currentLocation)) {
            if (currentTurn == 1 || chemicalIsRequiredForTurn(currentLocation, grid)) {
                Point point = nextTurn.getKey();
                DirectionType direction = nextTurn.getValue();
                Map<DirectionType, ChemicalCell.ChemicalType> chemicalDirections = getChemicalDirections();
                ChemicalCell.ChemicalType chemicalType = chemicalDirections.get(direction);
                cp.location = point;
                cp.chemicals.add(chemicalType);
            }
        }
        return cp;
    }

    private boolean chemicalIsRequiredForTurn(Point currentLocation, ChemicalCell[][] grid) {
        return true;
    }

    private Map<DirectionType, ChemicalCell.ChemicalType> getChemicalDirections() {
        Map<DirectionType, ChemicalCell.ChemicalType> chemicalDirs = new HashMap<>();
        // order of elements chemicalTypes and directionTypes is critical to making sure
        // values map correctly for both agent and controller
        ChemicalCell.ChemicalType[] chemicalTypes = {
                ChemicalCell.ChemicalType.RED,
                ChemicalCell.ChemicalType.GREEN,
                ChemicalCell.ChemicalType.BLUE
        };

        DirectionType[] directionTypes = {
                DirectionType.NORTH,
                DirectionType.EAST,
                DirectionType.SOUTH,
                DirectionType.WEST
        };

        int dirIndex = 0;
        for (int i = 0; i < chemicalTypes.length; i++) {
            if (directionTypes[dirIndex] == prevDir) {
                dirIndex++;
            }
            chemicalDirs.put(directionTypes[dirIndex], chemicalTypes[i]);
            dirIndex++;
        }
        return chemicalDirs;
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
