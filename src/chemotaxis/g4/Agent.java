package chemotaxis.g4; // TODO modify the package name to reflect your team

import java.util.Map;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.Move;
import chemotaxis.sim.SimPrinter;
import java.util.Random;

public class Agent extends chemotaxis.sim.Agent {
    private DirectionType[] directions = new DirectionType[]{DirectionType.CURRENT, DirectionType.NORTH, DirectionType.SOUTH, DirectionType.EAST, DirectionType.WEST};
   
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
      Move move = new Move();
      move.currentState = previousState;

    
      ChemicalType chosenChemicalType = ChemicalType.BLUE;
      
      //detect if any chemical is placed by controller
      int blockedNeighbors = 0;
      for(DirectionType directionType : neighborMap.keySet()) {
         if (neighborMap.get(directionType).isBlocked()) {
          blockedNeighbors += 1;
         }
         if (neighborMap.get(directionType).getConcentration(ChemicalType.BLUE) == 1) {
           
            move.currentState = (byte)directionType.ordinal();
            move.currentState = (byte)(move.currentState.byteValue() - 13);
         }
         else if (neighborMap.get(directionType).getConcentration(ChemicalType.RED) == 1) {
           // NE-N = 5
           // NE-E = 6
           // SE-N = 7
           // SE-E = 8
           // NW-N = 9
           // NW-W = 10
           // SW-S = 11
           // SW-S = 12
           if (directionType == DirectionType.NORTH) {
             move.currentState = (byte)(5-13);
           }
           if (directionType == DirectionType.SOUTH) {
            move.currentState = (byte)(7-13);
          }
         }
         else if (neighborMap.get(directionType).getConcentration(ChemicalType.GREEN) == 1) {
          if (directionType == DirectionType.NORTH) {
            move.currentState = (byte)(9-13);
          }
          if (directionType == DirectionType.SOUTH) {
           move.currentState = (byte)(11-13);
         }
        }
       }

      if (move.currentState < 0){ //following path

        byte state = (byte)(move.currentState + 13);
        // if state > 4 then we're moving on a diagonal
        if (state > 4) {
          if (state == 5) {
            move.directionType = DirectionType.NORTH;
            move.currentState = (byte)(state + 1 - 13);
          }
          else if (state == 6) {
            move.directionType = DirectionType.EAST;
            move.currentState = (byte)(state - 1 - 13);
          }
          else if (state == 7) {
            move.directionType = DirectionType.SOUTH;
            move.currentState = (byte)(state + 1 - 13);
          }
          else if (state == 8) {
            move.directionType = DirectionType.EAST;
            move.currentState = (byte)(state - 1 - 13);
          }
          else if (state == 9) {
            move.directionType = DirectionType.NORTH;
            move.currentState = (byte)(state + 1 - 13);
          }
          else if (state == 10) {

            move.directionType = DirectionType.WEST;
            move.currentState = (byte)(state - 1 - 13);
          }
          else if (state == 11) {
            move.directionType = DirectionType.SOUTH;
            move.currentState = (byte)(state + 1 - 13);
          }
          else if (state == 12) {
            move.directionType = DirectionType.WEST;
            move.currentState = (byte)(state - 1 - 13);
          }
        }
        else {
          move.directionType = DirectionType.values()[move.currentState + 13];

        }

        return move;
      } 

      
      ////no chemical placed so far, agent navigates itself
      int[] pairs = new int[] {1, 0, 3, 2};
      if (previousState < 5) {
        DirectionType previous_direction = DirectionType.values()[previousState];
        if (neighborMap.get(previous_direction).isBlocked()) { //wall encountered, randomly make a turn
          //System.out.println("wall");
          Random rand = new Random();
          boolean makeTurn = false;
          for (int i = 0; i < 20; i++){
            int randomDir = rand.nextInt(4);
            DirectionType dir = DirectionType.values()[randomDir];
            //System.out.println(dir);
            //System.out.println((int)pairs[previousState]);
            //System.out.println(randomDir);
            if (neighborMap.get(dir).isOpen() && ((int)pairs[previousState]!=randomDir)){ //turn left or turn right
              move.directionType = dir;
              int curState = randomDir;
              move.currentState = (byte)curState;
              makeTurn = true;
              break;
            }
            if (!makeTurn) { //deadend, turn around 180 deg
              //System.out.println("turning 180");
              move.currentState = (byte)pairs[previousState];
              move.directionType = DirectionType.values()[move.currentState];
            }
            
          }
          return move;
        } else { //no wall ahead, continue to move in same direction
          move.currentState = previousState;
          move.directionType = DirectionType.values()[previousState];
          return move;
        }
      }
      
      /*
      if (previousState > 5) {
        DirectionType previous_direction = DirectionType.values()[previousState - 5];
        if (neighborMap.get(previous_direction).isBlocked()) { //wall encountered, randomly make a turn
          System.out.println("wall");
          Random rand = new Random();
          boolean makeTurn = false;
          for (int i = 0; i < 15; i++){
            int randomDir = rand.nextInt(4);
            DirectionType dir = DirectionType.values()[randomDir];
            System.out.println(dir);
            System.out.println((int)pairs[previousState - 5]);
            System.out.println(randomDir);
            if (neighborMap.get(dir).isOpen() && ((int)pairs[previousState - 5]!=randomDir)){ //turn left or turn right
              move.directionType = dir;
              int curState = randomDir + 5;
              move.currentState = (byte)curState;
              makeTurn = true;
              break;
            }
            if (!makeTurn) { //deadend, turn around 180 deg
              System.out.println("turning 180");
              move.currentState = (byte)pairs[previousState - 5];
              move.directionType = DirectionType.values()[move.currentState];
            }
            
          }
          return move;
        } else { //no wall ahead, continue to move in same direction
          move.currentState = previousState;
          move.directionType = DirectionType.values()[previousState - 5];
          return move;
        }
      } */

      //move.directionType = DirectionType.values()[move.currentState];
      return null;
      //if no chemical placed, agent navigates itself and explore around
      //case I: not following a wall, continue to head 
      //if (blockedNeighbors == 0) {
      //  if () {
      //
      //  }
      //}
      
      //randomDirection = randomNum % 4;

      //neighborMap.keySet()


   }
}
