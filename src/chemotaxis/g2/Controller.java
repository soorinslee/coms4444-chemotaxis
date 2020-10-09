package chemotaxis.g2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.Queue;

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
    // source: https://www.techiedelight.com/lee-algorithm-shortest-path-in-a-maze/
    private ArrayList<Point> getShortestPath(ChemicalCell[][] grid) {
        // after finding shortest path:
        // if (pathCost > budget) => continue to next shortest path

        int[] rowPosMovs = { -1, 0, 0, 1 };
        int[] colPosMovs = { 0, -1, 1, 0 };

        // construct a matrix to keep track of visited cells
        boolean[][] visited = new boolean[size][size];
        Point[][] prevCell = new Point[size][size];

        // create an empty queue
        Queue<Node> q = new ArrayDeque<>();

        int i = (int) start.getX()-1;
        int j = (int) start.getY()-1;


        // mark source cell as visited and enqueue the source node
        visited[i][j] = true;
        prevCell[i][j] = null;
        q.add(new Node(i, j , 0));

        // stores length of longest path from source to destination
        int min_dist = Integer.MAX_VALUE;

        // loop till queue is empty
        while (!q.isEmpty())
        {
            // pop front node from queue and process it
            Node node = q.poll();

            // (i, j) represents current cell and dist stores its
            // minimum distance from the source
            i = node.x;
            j = node.y;
            int dist = node.dist;

            // if destination is found, update min_dist and stop
            if (i == (int) target.getX()-1 && j == (int) target.getY()-1)
            {
                min_dist = dist;
                break;
            }

            // check for all 4 possible movements from current cell
            // and enqueue each valid movement
            for (int k = 0; k < 4; k++)
            {
                // check if it is possible to go to position
                // (i + row[k], j + col[k]) from current position
                if (isValid(grid, visited, i + rowPosMovs[k], j + colPosMovs[k]))
                {
                    // mark next cell as visited and enqueue it
                    visited[i + rowPosMovs[k]][j + colPosMovs[k]] = true;
                    prevCell[i + rowPosMovs[k]][j + colPosMovs[k]] = new Point(i + 1, j + 1);
                    q.add(new Node(i + rowPosMovs[k], j + colPosMovs[k], dist + 1));
                }
            }
        }

        if (min_dist != Integer.MAX_VALUE) {
            System.out.println("The shortest path from source to destination " +
                                     "has length " + min_dist);

            ArrayList<Point> sp = new ArrayList<Point>();
            sp.add(target);
            Point cur = prevCell[(int) target.getX()-1][(int) target.getY()-1];    
            while (cur != null){
                sp.add(0, cur);
                cur = prevCell[(int) cur.getX()-1][(int) cur.getY()-1];
            }
            return sp;
        }
        else {
            System.out.println("Destination can't be reached from given source");
        }

        return null;
    }

    private boolean isValid(ChemicalCell grid[][], boolean visited[][], int i, int j) {
        return (i >= 0) && (i < size) && (j >= 0) && (j < size) && grid[i][j].isOpen() && !visited[i][j];
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

    private class Node {

        // (x, y) represents matrix cell coordinates
        // dist represent its minimum distance from the source
        private int x;
        private int y; 
        private int dist;

        Node(int x, int y, int dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }
    }

    /*
    * TODOS:
    *  Longterm diagnol
    * */
}
