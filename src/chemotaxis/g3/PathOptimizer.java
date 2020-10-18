package chemotaxis.g3;

import java.awt.Point;
import java.util.*; 
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.SimPrinter;
import java.lang.Math;

public class PathOptimizer {

    private static SimPrinter simPrinter = new SimPrinter(true);

    public static List<Point> getPath(List<Point> path, ChemicalCell[][] grid) {
        List<Point> newPath = new ArrayList<Point>();
        Point turnPt = null;
        Point curPt = path.get(0);

        //add start point
        newPath.add(path.get(0));

        while (!curPt.equals(path.get(path.size()-1))) {
            newPath.add(turnPt);
            curPt.setLocation(turnPt);
        }

        //add target point
        newPath.add(path.get(path.size()-1));

        return newPath;
    }

    public static Point checkVertical(Point curPt, Point futPt, ChemicalCell[][] grid) {
        Point temp = curPt;
        int addVal = 0;

        if (Double.compare(temp.getY(), futPt.getY()) == 0) 
        {
            return new Point(0, 0);
        }
	    else {
            if (Double.compare(temp.getX(), futPt.getX()) > 0) {
                addVal = 1;
            }
            else {
                addVal = -1;
            }

            while (!temp.equals(futPt)) {
                int x = (int) temp.getX();
                int y = (int) temp.getY();
                if (grid[x - 1][y - 1].isOpen == false) {
                    return new Point(0, 0);
                }
                else {
                    temp.setLocation(x + addVal, y);
                }
            }
        }

	    return temp;
    }

    public static checkHorizontal(Point curPt, Point futPt, ChemicalCell[][] grid) {

    }

    public static checkDiagonal(Point curPt, Point futPt, ChemicalCell[][] grid) {

    }
}