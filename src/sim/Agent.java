package sim;

import java.util.Map;


public abstract class Agent {
    
    /**
     * Move agent
     *
     * @param randomNum        random number available for agents
     * @param currentGradient  current cell's gradient
     * @param neighborMap      map of cell's neighbors
     * @return                 agent move
     *
     */
    public abstract Move makeMove(Integer randomNum, Gradient currentGradient, Map<DirectionType, Gradient> neighborMap);
}