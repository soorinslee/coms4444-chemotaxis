package chemotaxis.g3;

import java.awt.Point;
import java.util.*; 
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.SimPrinter;

public class PathFinder {

    static List<Point> path = new ArrayList<Point>();
    private static SimPrinter simPrinter = new SimPrinter(true);
    
    public PathFinder(Point start, Point target, ChemicalCell[][] grid, Integer size) {
        this.path = getPath(start, target, grid, size);
    }

    public static List<Point> getPath(Point start, Point target, ChemicalCell[][] grid, Integer size) {
        List<Point> path = new ArrayList<Point>();
        List<Point> temp = new ArrayList<Point>();
        Map<Point, Point> parents = new HashMap<Point, Point>();
        boolean targetReached = false;
        Point end = null;

        temp.add(start);
        parents.put(start, null);
        
        while (temp.size() > 0 && !targetReached) {
            Point currentPt = temp.remove(0);
            List<Point> children = getChildren(currentPt, grid, size);
            for (Point child : children) {
                if (!parents.containsKey(child)) {
                    parents.put(child, currentPt);

                    if (!child.equals(target)) {
                        temp.add(child);
                    } else {
                        temp.add(child);
                        targetReached = true;
                        end = child;
                        break;
                    }
                }
            }
        }

        Point pt = end;
        while (pt != null) {
            path.add(0, pt);
            pt = parents.get(pt);
        }

        return path;
    }

    public static List<Point> getChildren(Point parent, ChemicalCell[][] grid, Integer size) {
        List<Point> children = new ArrayList<Point>();
        int x = (int) parent.getX();
        int y = (int) parent.getY();

        if (0 <= x && x < size) {
            if (grid[x][y - 1].isOpen() == true) {
                children.add(new Point(x + 1, y));
            }
        }
        if (0 <= x - 2 && x - 2 < size) {
            if (grid[x - 2][y - 1].isOpen() == true) {
                children.add(new Point(x - 1, y));
            }
        }
        if (0 <= y && y < size) {
            if (grid[x - 1][y].isOpen() == true) {
                children.add(new Point(x, y + 1));
            }
        }
        if (0 < y - 2 && y - 2 < size) {
            if (grid[x - 1][y - 2].isOpen() == true) {
                children.add(new Point(x, y - 1));
            }
        }

        //simPrinter.println("Children: " + Arrays.toString(children.toArray()));

        return children;
    }

}
