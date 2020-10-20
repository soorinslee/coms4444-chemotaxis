package chemotaxis.g4; // TODO modify the package name to reflect your team

import java.awt.Point;
import java.util.PriorityQueue;
import java.io.*;
import java.util.HashMap;
import java.lang.Math; 
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.SimPrinter;


public class Controller extends chemotaxis.sim.Controller {
    HashMap<Point, Point> came_from;
    HashMap<Point, Integer> cost_so_far;
    List<Point> path;

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

      // A* path planning in first round
      if (currentTurn == 1) {
        int idxX = this.start.x;
        int idxY = this.start.y;

        PriorityQueue<PriorityNode> frontier = new PriorityQueue<>();
        frontier.add(new PriorityNode(grid[idxX-1][idxY-1], idxX, idxY, 0));
        came_from = new HashMap<Point, Point>();
        cost_so_far = new HashMap<Point, Integer>();

        came_from.put(this.start, null);
        cost_so_far.put(this.start, 0);

        while (frontier.size() != 0) {
          PriorityNode current = frontier.remove();

          //target arrived
          if (current.getX() == this.target.x && current.getY() == this.target.y) {
            PriorityNode c = current;
            int cX = c.getX();
            int cY = c.getY();
            Point curPt = new Point(cX, cY);
            path = new ArrayList<>();
            path.add(curPt);
            //System.out.println(cX + ", " + cY);
            while(came_from.get(curPt) != null) {
              curPt = came_from.get(curPt);
              path.add(curPt);
              //cX = curPt.x;
              //cY = curPt.y;
              //System.out.println(cX + ", " + cY);
            }
            
            Collections.reverse(path);
            System.out.println("------path-----");
            for (Point waypoint : path) {
              System.out.println(waypoint);
            }
          }

          //for all neighbors of current cell
          int[][] dir = { {-1, 0}, {1, 0}, {0, -1}, {0, 1}};
          Integer newX, newY;
          for (int i = 0; i < 4; i++){
            newX = current.getX() + dir[i][0];
            newY = current.getY() + dir[i][1];

            //neighbor out of bound
            if (newX < 1 || newY < 1 || newX > size || newY > size) {
              continue;
            }

            ChemicalCell neighbor = grid[newX-1][newY-1];
            if (neighbor.isBlocked()) {
              continue;
            }

            Point neighborPos = new Point(newX, newY);
            Point currentPos = new Point(current.getX(), current.getY());
            int new_cost = cost_so_far.get(currentPos) + 1;
            if (!cost_so_far.containsKey(neighborPos) || new_cost < cost_so_far.get(neighborPos)) {
              cost_so_far.put(neighborPos, new_cost);
              int h = Math.abs(this.target.x -current.getX()) + Math.abs(this.target.y - current.getY());
              int priority = new_cost + h;
              frontier.add(new PriorityNode(neighbor, newX, newY, priority));
              came_from.put(neighborPos, currentPos);
            }
          }

        }
      
      }

      ChemicalPlacement chemicalPlacement = new ChemicalPlacement();
      List<ChemicalType> chemicals = new ArrayList<>();
      chemicals.add(ChemicalType.BLUE);
      chemicalPlacement.location = path.get(currentTurn);
      chemicalPlacement.chemicals = chemicals;
      return chemicalPlacement;
   }

}

class PriorityNode implements Comparable<PriorityNode> {
    private ChemicalCell node;
    private Integer priority;
    private int x;
    private int y;

    public PriorityNode(ChemicalCell node, int idX, int idY, int priority) {
        this.node = node;
        this.x = idX;
        this.y = idY;
        this.priority = priority;
    }

    public Integer getPriority() {
      return priority;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public ChemicalCell getCell() {
      return node;
    }

    @Override
    public int compareTo(PriorityNode other) {
        return this.getPriority().compareTo(other.getPriority());
    }
}