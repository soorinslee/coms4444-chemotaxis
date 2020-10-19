package chemotaxis.g1; // TODO modify the package name to reflect your team

import java.awt.Point;
import java.util.*;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {

    private Integer currApplication;    // keep track of where chemicals have been applied on path
    private Integer totalChemicals;     // total number of chemicals

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
        this.currApplication = 1;
        this.totalChemicals = 0;
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
            this.totalChemicals = chemicalsRemaining;             // Get total chemicals
        }
        List<Point> path = getShortestPath(currentLocation, grid);

        ChemicalPlacement chemicalPlacement = new ChemicalPlacement();

        List<ChemicalType> chemicals = new ArrayList<>();
        chemicals.add(ChemicalType.BLUE);                           // Using blue chemical

        /**
         if ((this.currApplication) < this.path.size()) {        // If not done dropping chemicals along whole path
         chemicalPlacement.chemicals = chemicals;
         if (chemicalsRemaining >= this.totalChemicals/2)    // If over half of chemicals left, use more liberally
         chemicalPlacement.location = path.get(this.currApplication++);
         else if (chemicalsRemaining < this.totalChemicals/2 && currentTurn % 2 == 1) {
         chemicalPlacement.location = path.get(this.currApplication);
         this.currApplication += 2;
         }
         }
         else                                                    // If done dropping along whole path, drop at target
         chemicalPlacement.location = this.target;
         **/

        if ((this.currApplication) < path.size()) {        // If not done dropping chemicals along whole path
            chemicalPlacement.chemicals = chemicals;
            chemicalPlacement.location = path.get(1);
        }

        return chemicalPlacement;
    }

    class Node {
        int x;
        int y;
        int turns;
        String dir;
        Node prev;

        public Node() {
            this.x = 0;
            this.y = 0;
        }

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Node(int x, int y, int turns, String dir) {
            this.x = x;
            this.y = y;
            this.turns = turns;
            this.dir = dir;
        }
    }

    /**
     * Get shortest path from given start to target using BFS
     *
     * @param s                   the start point
     * @param grid                game grid/map
     * @return                    list of points showing shortest path from start to end
     *
     */
    public List<Point> getShortestPath(Point s, ChemicalCell[][] grid) {
        Queue<Node> queue = new LinkedList<Node>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        Node start = new Node((int)s.getX(), (int)s.getY());
        queue.add(start);
        List<Point> path = new ArrayList<Point>();

        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (cur.x == target.x && cur.y == target.y) {
                while (cur != null) {
                    path.add(new Point(cur.x, cur.y));
                    cur = cur.prev;
                }
                Collections.reverse(path);
                break;
            }
            if (!visited[cur.x-1][cur.y-1]){
                for (Node neighbor:getNeighbors(cur, grid, false))
                    if (!visited[neighbor.x-1][neighbor.y-1])
                        queue.add(neighbor);
                visited[cur.x-1][cur.y-1] = true;
            }
        }

        return path;
    }

    /**
     * Get available neighbors of a cell
     *
     * @param cur                 current cell represented by Node
     * @param grid                game grid/map
     * @return                    list of points showing shortest path from start to end
     *
     */
    public List<Node> getNeighbors(Node cur, ChemicalCell[][] grid, boolean useTurns) {
        List<Node> neighbors = new ArrayList<Node>();
        int x = (int)cur.x-1;
        int y = (int)cur.y-1;
        if (x > 0 && grid[x-1][y].isOpen()) {
            Node next = new Node(x, y+1);
            if (useTurns) {
                next.dir = "UP";
                next.turns = cur.turns;
                if (!next.dir.equals(cur.dir))
                    next.turns++;
            }
            next.prev = cur;
            neighbors.add(next);
        }
        if (y > 0 && grid[x][y-1].isOpen()) {
            Node next = new Node(x+1, y);
            if (useTurns) {
                next.dir = "LEFT";
                next.turns = cur.turns;
                if (!next.dir.equals(cur.dir))
                    next.turns++;
            }
            next.prev = cur;
            neighbors.add(next);
        }
        if (x < grid.length-1 && grid[x+1][y].isOpen()) {
            Node next = new Node(x+2, y+1);
            if (useTurns) {
                next.dir = "DOWN";
                next.turns = cur.turns;
                if (!next.dir.equals(cur.dir))
                    next.turns++;
            }
            next.prev = cur;
            neighbors.add(next);
        }
        if (y < grid.length-1 && grid[x][y+1].isOpen()) {
            Node next = new Node(x+1, y+2);
            if (useTurns) {
                next.dir = "RIGHT";
                next.turns = cur.turns;
                if (!next.dir.equals(cur.dir))
                    next.turns++;
            }
            next.prev = cur;
            neighbors.add(next);
        }
        return neighbors;
    }

    /**
     * Get all paths from target to any cell within a given number of turns
     *
     * @param grid                game grid/map
     * @param turns               number of turns allowed
     * @return                    list of points showing shortest path from start to end
     *
     */
    public Map<Point, List<Point>> getAllPathsFromTarget(ChemicalCell[][] grid, Integer turns) {
        Queue<Node> queue = new LinkedList<Node>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        Node start = new Node((int)this.target.getX(), (int)this.target.getY(), 0, "");
        queue.add(start);
        Map<Point, List<Point>> allPaths = new HashMap<Point, List<Point>>();

        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            Point curPoint = new Point(cur.x, cur.y);

            if (cur.turns <= turns && !allPaths.containsKey(curPoint)) {
                List<Point> path = new ArrayList<Point>();
                Node temp = cur;
                while (cur != null) {
                    path.add(curPoint);
                    cur = cur.prev;
                }
                Collections.reverse(path);
                allPaths.put(curPoint, path);
                cur = temp;
            }

            if (!visited[cur.x-1][cur.y-1]){
                for (Node neighbor:getNeighbors(cur, grid, true))
                    if (!visited[neighbor.x-1][neighbor.y-1])
                        queue.add(neighbor);
                visited[cur.x-1][cur.y-1] = true;
            }
        }

        return allPaths;
    }

    // Testing the BFS (run with "java chemotaxis/g1/Controller" in /src)
    public static void main(String[] args) {
        Point start = new Point(1,1);
        Point target = new Point(5,5);
        int size = 5;
        int simTime = 0;
        int budget = 50;
        int seed = 0;
        SimPrinter simPrinter = new SimPrinter(true);
        Controller test = new Controller(start, target, size, simTime, budget, seed, simPrinter);
        ChemicalCell closed = new ChemicalCell(false);
        ChemicalCell open = new ChemicalCell(true);
        ChemicalCell[][] grid = new ChemicalCell[size][size];
        grid[0][0]=open;
        grid[0][1]=open;
        grid[0][2]=open;
        grid[0][3]=closed;
        grid[0][4]=open;
        grid[1][0]=open;
        grid[1][1]=closed;
        grid[1][2]=closed;
        grid[1][3]=closed;
        grid[1][4]=open;
        grid[2][0]=open;
        grid[2][1]=open;
        grid[2][2]=open;
        grid[2][3]=open;
        grid[2][4]=open;
        grid[3][0]=open;
        grid[3][1]=closed;
        grid[3][2]=open;
        grid[3][3]=closed;
        grid[3][4]=closed;
        grid[4][0]=open;
        grid[4][1]=closed;
        grid[4][2]=open;
        grid[4][3]=open;
        grid[4][4]=open;

        List<Point> path = test.getShortestPath(start, grid);
        System.out.println("START: 1, 1");
        System.out.println("END: 5, 5");
        System.out.println("SHORTEST PATH:");
        System.out.println(path);

        int turns = 3;
        Map<Point, List<Point>> allPaths = test.getAllPathsFromTarget(grid, turns);
        // System.out.println(allPaths);
        System.out.println("~~~~~~");

        int[][] grid2 = new int[size][size];
        grid2[0][0]=0;
        grid2[0][1]=0;
        grid2[0][2]=0;
        grid2[0][3]=2;
        grid2[0][4]=0;
        grid2[1][0]=0;
        grid2[1][1]=2;
        grid2[1][2]=2;
        grid2[1][3]=2;
        grid2[1][4]=0;
        grid2[2][0]=0;
        grid2[2][1]=0;
        grid2[2][2]=0;
        grid2[2][3]=0;
        grid2[2][4]=0;
        grid2[3][0]=0;
        grid2[3][1]=2;
        grid2[3][2]=0;
        grid2[3][3]=2;
        grid2[3][4]=2;
        grid2[4][0]=0;
        grid2[4][1]=2;
        grid2[4][2]=0;
        grid2[4][3]=0;
        grid2[4][4]=0;
        System.out.println("GRID BEFORE (2's are blocked cells and 0's are free cells):");
        for (int[] row:grid2) {
            for (int col:row)
                System.out.print(col +"  ");
            System.out.println();
        }
        System.out.println("~~~~~~");

        System.out.println("GRID AFTER (8's are cells that can reach end given " + turns + " turns):");
        for (Point p:allPaths.keySet()) {
            grid2[p.x-1][p.y-1]=8;
        }
        for (int[] row:grid2) {
            for (int col:row)
                System.out.print(col+"  ");
            System.out.println();
        }

    }
}
