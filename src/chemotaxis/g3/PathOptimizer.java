package chemotaxis.g3;

import java.awt.Point;
import java.util.*; 
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.SimPrinter;
import java.lang.Math;

public class PathOptimizer {

    private static SimPrinter simPrinter = new SimPrinter(true);

    public static List<Point> getPath(List<Point> path) {
        List<Point> newPath = new ArrayList<Point>();
        Point turnPt = null;
        Point curPt = path.get(0);

        while (!curPt.equals(path.get(path.size()-1))) {

        }

        return newPath;
    }

    public static checkVertical(Point curPt, Point futPt) {

    }

    public static checkHorizontal(Point curPt, Point futPt) {

    }

    public static checkDiagonal(Point curPt, Point futPt) {
        
    }
}