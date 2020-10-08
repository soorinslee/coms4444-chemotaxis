package chemotaxis.g2;

import java.util.Map;
import java.util.Random;

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
        // TODO: indepdent moves

        DirectionType prevDir = getPrevDirection(previousState);
        Move move = new Move();
        return move;
    }

    // returns null if it is not an independent case
    private DirectionType getPrevDirection(Byte prevState) {
        byte b = prevState.byteValue();
        String prevStateStr = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                                    .replace(' ', '0');
        String prevDirectionBits = prevStateStr.substring(6);
        int prevDirection = Integer.parseInt(prevDirectionBits, 2);
        simPrinter.println("prevDir num: " + prevDirection);
        switch (prevDirection) {
            case 0: return DirectionType.NORTH;
            case 1: return DirectionType.EAST;
            case 2: return DirectionType.SOUTH;
            case 3: return DirectionType.WEST;
            default: return DirectionType.CURRENT;
        }
    }
}