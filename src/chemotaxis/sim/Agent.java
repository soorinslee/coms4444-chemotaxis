package chemotaxis.sim;

import java.util.Map;


public abstract class Agent {

    public SimPrinter simPrinter;

    /**
     * Agent constructor
     *
     * @param simPrinter  simulation printer
     *
     */
    public Agent(SimPrinter simPrinter) {
        this.simPrinter = simPrinter;
	}

    /**
     * Move agent
     *
     * @param randomNum        random number available for agents
     * @param previousState    byte of previous state
     * @param currentGradient  current cell's gradient
     * @param neighborMap      map of cell's neighbors
     * @return                 agent move
     *
     */
    public abstract Move makeMove(Integer randomNum, Byte previousState, Gradient currentGradient, Map<DirectionType, Gradient> neighborMap);
}