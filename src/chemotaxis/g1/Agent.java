package chemotaxis.g1; // TODO modify the package name to reflect your team

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.Move;
import chemotaxis.sim.SimPrinter;
import chemotaxis.sim.ChemicalCell.ChemicalType;
//import jdk.internal.agent.resources.agent;

public class Agent extends chemotaxis.sim.Agent {

    /**
     * Agent constructor
     *
     * @param simPrinter simulation printer
     *
     */
    public Agent(SimPrinter simPrinter) {
        super(simPrinter);
    }

    /**
     * Move agent
     *
     * @param randomNum     random number available for agents
     * @param previousState byte of previous state
     * @param currentCell   current cell
     * @param neighborMap   map of cell's neighbors
     * @return agent move
     *
     */
    @Override
    public Move makeMove(Integer randomNum, Byte previousState, ChemicalCell currentCell,
                         Map<DirectionType, ChemicalCell> neighborMap) {
        Move agentMove = new Move();
        Map<DirectionType, ChemicalType> move = getMostConcentratedChemical(neighborMap);
        for(DirectionType key: move.keySet()){
            if(move.get(key) == ChemicalType.BLUE){
                agentMove.directionType = key;
            } else if (move.get(key) == ChemicalType.RED){
                agentMove.directionType = getOppositeDirection(key);
            }
        }
        return agentMove;
    }

    private Map<DirectionType, ChemicalType> getMostConcentratedChemical(Map<DirectionType, ChemicalCell> neighborCellMap) {
        DirectionType max = null;
        ChemicalType maxType = null;
        double maxConc = -10.0;
        for (DirectionType neighborDir : neighborCellMap.keySet()) {
            ChemicalCell neighbor = neighborCellMap.get(neighborDir);
            Map<ChemicalType, Double> neighborMap = neighbor.getConcentrations();

            Double maxValueInMap = (Collections.max(neighborMap.values())); // This will return max value in the Hashmap
            for (Entry<ChemicalType, Double> entry : neighborMap.entrySet()) { // Itrate through hashmap
                if (entry.getValue() == maxValueInMap) {
                    if(maxConc < maxValueInMap){
                        maxConc = maxValueInMap;
                        maxType = entry.getKey();
                        max = neighborDir;
                    }
                }
            }
        }
        Map<DirectionType, ChemicalType> move = new HashMap<>();
        move.put(max, maxType);
        return move;
    }
    private DirectionType getOppositeDirection(DirectionType inDirection){
        if(inDirection.equals(DirectionType.EAST)){
            return DirectionType.WEST;
        } else if (inDirection.equals(DirectionType.WEST)){
            return DirectionType.EAST;
        } else if (inDirection.equals(DirectionType.NORTH)){
            return DirectionType.SOUTH;
        } else {
            return DirectionType.NORTH;
        }
    }
}