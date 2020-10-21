package chemotaxis.g1; // TODO modify the package name to reflect your team

import java.util.HashMap;
import java.util.Map;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.Move;
import chemotaxis.sim.SimPrinter;

public class Agent extends chemotaxis.sim.Agent {
    private SimPrinter sp;
    /**
     * Agent constructor
     *
     * @param simPrinter  simulation printer
     *
     */
    public Agent(SimPrinter simPrinter) {
        super(simPrinter);
        sp = simPrinter;
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
        Move agentMove = new Move();
        DirectionType dir = getBlueDirection(neighborMap, 0.99);
        //sp.println(dir.toString());
        if(dir != DirectionType.CURRENT) {
            agentMove.currentState = getDirectionByte(dir);
            agentMove.directionType = dir;
        }
        else {
            if(neighborMap.get(getDirectionFromByte(previousState)).isBlocked()) {
                System.out.println("hehehe");
                agentMove.directionType = getRandomDirection(getDirectionFromByte(previousState), randomNum);
            }
            else {
                agentMove.directionType = getDirectionFromByte(previousState);
            }
            agentMove.currentState = getDirectionByte(agentMove.directionType);
        }
        return agentMove;
    }

    private DirectionType getRandomDirection(DirectionType prev, Integer random) {
        if(getDirectionByte(prev).intValue()%2 == 0) {
            if(random%2==0) return DirectionType.WEST;
            else return DirectionType.EAST;
        }
        else {
            if(random%2==0) return DirectionType.SOUTH;
            else return DirectionType.NORTH;
        }
    }

    private DirectionType getBlueDirection(Map<DirectionType, ChemicalCell> neighborMap, Double blueThreshold) {
        Map<DirectionType, Map<ChemicalCell.ChemicalType, Double>> concentrationMap = getConcentrations(neighborMap);
        DirectionType absoluteBlue = DirectionType.CURRENT;

        for(DirectionType dir : concentrationMap.keySet()) {
            if(concentrationMap.get(dir).get(ChemicalCell.ChemicalType.BLUE) >= blueThreshold) absoluteBlue = dir;
        }

        return absoluteBlue;
    }

    private DirectionType getGreenDirection(Map<DirectionType, ChemicalCell> neighborMap, Double greenThreshold) {
        Map<DirectionType, Map<ChemicalCell.ChemicalType, Double>> concentrationMap = getConcentrations(neighborMap);
        DirectionType absoluteGreen = DirectionType.CURRENT;

        for(DirectionType dir : concentrationMap.keySet()) {
            if(concentrationMap.get(dir).get(ChemicalCell.ChemicalType.BLUE) >= greenThreshold) absoluteGreen = dir;
        }

        return absoluteGreen;
    }

    private DirectionType getRedDirection(Map<DirectionType, ChemicalCell> neighborMap, Double redThreshold) {
        Map<DirectionType, Map<ChemicalCell.ChemicalType, Double>> concentrationMap = getConcentrations(neighborMap);
        DirectionType absoluteRed = DirectionType.CURRENT;

        for(DirectionType dir : concentrationMap.keySet()) {
            if(concentrationMap.get(dir).get(ChemicalCell.ChemicalType.RED) == redThreshold) absoluteRed = dir;
        }

        return absoluteRed;
    }

    private Byte getDirectionByte(DirectionType dir) {
        switch (dir) {
            case SOUTH:
                return (byte) 4;
            case WEST:
                return (byte) 3;
            case NORTH:
                return (byte) 2;
            case EAST:
                return (byte) 1;
            default:
                return (byte) 0;
        }
    }

    private DirectionType getDirectionFromByte(Byte b) {
        switch (b) {
            // 12 S -> W
            // 11 S -> W
            // 10 N -> E
            // 9 N -> W
            // 8 E -> N
            // 7 W -> S
            // 6 E -> N
            // 5 W -> N
            case (byte) 12:
                return DirectionType.SOUTH;
            case (byte) 11:
                return DirectionType.SOUTH;
            case (byte) 10:
                return DirectionType.NORTH;
            case (byte) 9:
                return DirectionType.NORTH;
            case (byte) 8:
                return DirectionType.EAST;
            case (byte) 7:
                return DirectionType.WEST;                
            case (byte) 6:
                return DirectionType.EAST;
            case (byte) 5:
                return DirectionType.WEST;
            case (byte) 4:
                return DirectionType.SOUTH;
            case (byte) 3:
                return DirectionType.WEST;
            case (byte) 2:
                return DirectionType.NORTH;
            case (byte) 1:
                return DirectionType.EAST;
            default:
                return DirectionType.CURRENT;
        }
    }

    private Map<DirectionType, Map<ChemicalCell.ChemicalType, Double>> getConcentrations(Map<DirectionType, ChemicalCell> neighborMap) {
        Map<DirectionType, Map<ChemicalCell.ChemicalType, Double>> concentrationMap = new HashMap<>();
        //sp.println(neighborMap.keySet().toString());
        for(DirectionType dir : neighborMap.keySet()) {
            concentrationMap.put(dir, neighborMap.get(dir).getConcentrations());
        }
        //System.out.println(concentrationMap.toString());
        return concentrationMap;
    }
}