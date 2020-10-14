package chemotaxis.g2;

import java.util.HashMap;
import java.util.Map;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
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
        DirectionType prevDir = getPrevDirection(previousState);
        Map<ChemicalCell.ChemicalType, DirectionType> chemicalDirections = getChemicalDirections(prevDir);
        Map<ChemicalCell.ChemicalType, Double> concentrations = currentCell.getConcentrations();

        for (Map.Entry<ChemicalCell.ChemicalType, Double> concentration: concentrations.entrySet()) {
            if (concentration.getValue() == 1.0) {
                ChemicalCell.ChemicalType chemicalType = concentration.getKey();
                DirectionType newDir = chemicalDirections.get(chemicalType);
                simPrinter.println("Agent going " + newDir.toString());
                return buildMove(newDir, previousState);
            }
        }

        DirectionType independentDir = getIndependentDir(neighborMap, prevDir);
        if (independentDir != null) {
            simPrinter.println("Agent going " + independentDir.toString());
            return buildMove(independentDir, previousState);
        }

        Move move = new Move();
        move.directionType = prevDir;
        move.currentState = previousState;
        simPrinter.println("Agent going " + prevDir.toString());
        return move;
    }


    private Move buildMove(DirectionType dir, Byte previousState) {
        Move move = new Move();
        move.directionType = dir;

        // can add more changes to the state byte here
        Byte newState = updatePrevDirBits(previousState, dir);

        move.currentState = newState;
        return move;
    }

    private Map<ChemicalCell.ChemicalType, DirectionType> getChemicalDirections(DirectionType prevDir) {
        Map<ChemicalCell.ChemicalType, DirectionType> chemicalDirs = new HashMap<>();
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
            chemicalDirs.put(chemicalTypes[i], directionTypes[dirIndex]);
            dirIndex++;
        }
        return chemicalDirs;
    }

    private Byte updatePrevDirBits(Byte previousState, DirectionType dir) {
        Byte dirByte = directionToByte(dir);
        int newStateInt = previousState.intValue();

        // set last 2 bits to 0
        int mask = -4; // -4 => 11111100
        newStateInt &= mask;

        // set last 2 bits to direction bits
        newStateInt |= dirByte.intValue();

        return (byte) newStateInt;
    }

    // WARNING: returns null if it is not an independent case
    private DirectionType getIndependentDir(Map<DirectionType, ChemicalCell> neighborMap, DirectionType prevDir) {
        DirectionType[] orthDirs = getOrthogonalDirections(prevDir);
        DirectionType firstSideDir = orthDirs[0];
        DirectionType secondSideDir = orthDirs[1];
        ChemicalCell firstSideCell = neighborMap.get(firstSideDir);
        ChemicalCell secondSideCell = neighborMap.get(secondSideDir);
        ChemicalCell cellAhead = neighborMap.get(prevDir);
        ChemicalCell cellBehind = neighborMap.get(getOppositeDirection(prevDir));

        if (cellAhead.isBlocked()) {
            if (firstSideCell.isBlocked() && secondSideCell.isBlocked()) {
                // if all sides are blocked: turn back
                // logged because this case should ideally never occur
                return getOppositeDirection(prevDir);
            }
            // if hit a corner where 1 side is blocked: go the direction that is not blocked
            else if (firstSideCell.isBlocked()) {
                return secondSideDir;
            }
            else if (secondSideCell.isBlocked()) {
                return firstSideDir;
            }
        }
        else {
            if (neighborMap.get(orthDirs[0]).isBlocked() && neighborMap.get(orthDirs[1]).isBlocked()) {
                return prevDir;
            }
        }
        return null;
    }

    private DirectionType getPrevDirection(Byte prevState) {
        byte b = prevState.byteValue();
        String prevStateStr = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                                    .replace(' ', '0');
        String prevDirectionBits = prevStateStr.substring(6);
        int prevDirection = Integer.parseInt(prevDirectionBits, 2);
        switch (prevDirection) {
            case 0: return DirectionType.NORTH;
            case 1: return DirectionType.EAST;
            case 2: return DirectionType.SOUTH;
            case 3: return DirectionType.WEST;
            default: return DirectionType.CURRENT;
        }
    }

    private byte directionToByte(DirectionType dir) {
        switch (dir) {
            case NORTH: return (byte) 0;
            case EAST: return (byte) 1;
            case SOUTH: return (byte) 2;
            case WEST: return (byte) 3;
        }
        return (byte) 0;
    }

    private DirectionType[] getOrthogonalDirections(DirectionType dir) {
        DirectionType[] horizontal = {DirectionType.WEST, DirectionType.EAST};
        DirectionType[] vertical = {DirectionType.NORTH, DirectionType.SOUTH};

        switch (dir) {
            case NORTH:
            case SOUTH:
                return horizontal;
            default:
                return vertical;
        }
    }

    private DirectionType getOppositeDirection(DirectionType dir) {
        switch (dir) {
            case NORTH:
                return DirectionType.SOUTH;
            case SOUTH:
                return DirectionType.NORTH;
            case WEST:
                return DirectionType.EAST;
            case EAST:
                return DirectionType.WEST;
            default:
                return DirectionType.CURRENT;
        }
    }

    private ChemicalCell[] directionsToChemCell(DirectionType[] dirs, Map<DirectionType, ChemicalCell> neighborMap) {
        ChemicalCell[] cells = new ChemicalCell[dirs.length];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = neighborMap.get(dirs[i]);
        }
        return cells;
    }
}
