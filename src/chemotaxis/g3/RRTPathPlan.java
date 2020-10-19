package chemotaxis.g3;

// import javax.vecmath.Point;
import java.util.ArrayList;
import java.util.List;

public class RRTPathPlan {
    private double STEP_SZIE = 0.3;
    private double SAMPLE_SIZE = 9;
    private int PATHS_LIMIT = 1;
    private int MAX_ITERATIONS = 50000;


    private List<List<Point>> obstacles;
    private Point startPos;
    private Point endPos;
    private List<Double> boundaries;
    private RRT rrt;

    private double getEuclideanDistance(Point source, Point dest){
        Point temp = new Point();
        temp.sub(dest,source);
        return temp.length();
    }


    public RRTPathPlan(List<List<Point>> obstacles, Point startPos, Point endPos, List<Double> boundaries){
  
        this.obstacles = obstacles;
        this.startPos = startPos;
        this.endPos = endPos;
        this.boundaries = boundaries;

        rrt = new RRT(startPos.x, startPos.y);
    }

    boolean checkNodeToGoal(Point goalPos, Point nodePos){
        return goalPos.epsilonEquals(nodePos, 0.2);
    }

    boolean checkOutsideObstacles(Point pos){
        double AB, AD, AMAB, AMAD;
        for(List<Point> obstacle:obstacles){
            AB = Math.pow((obstacle.get(1).x - obstacle.get(0).x),2) + Math.pow((obstacle.get(1).y - obstacle.get(0).y),2);
            AD = Math.pow((obstacle.get(3).x - obstacle.get(0).x),2) + Math.pow((obstacle.get(3).y - obstacle.get(0).y),2);
            AMAB = (pos.x - obstacle.get(0).x)*(obstacle.get(1).x - obstacle.get(0).x) +
                    (pos.y - obstacle.get(0).y)*(obstacle.get(1).y - obstacle.get(0).y);
            AMAD = (pos.x - obstacle.get(0).x)*(obstacle.get(3).x - obstacle.get(0).x) +
                    (pos.y - obstacle.get(0).y)*(obstacle.get(3).y - obstacle.get(0).y);

            if((0 < AMAB) && (AMAB < AB) && (0 < AMAD) && (AMAD < AD))
            {
                return false;
            }
        }
        return true;
    }

    boolean checkInsideBoundary(Point pos){
        if(pos.x < boundaries.get(0) || pos.x > boundaries.get(1) || pos.y < boundaries.get(2) || pos.y > boundaries.get(3)){
            return false;
        }
        else {
            return true;
        }
    }

    RRTNode generateNode(){
        RRTNode node = new RRTNode();
        node.pos.x = SAMPLE_SIZE * (Math.random()-0.5) * 2;
        node.pos.y = SAMPLE_SIZE * (Math.random()-0.5) * 2;
        return node;
    }

    boolean addNewNodeToRRT(RRTNode newNode){
        boolean success = false;
        int nearestNodeID = rrt.getNearestNodeID(newNode.pos);
        RRTNode nearestNode = rrt.getNode(nearestNodeID);
        double theta = Math.atan2(newNode.pos.y - nearestNode.pos.y, newNode.pos.x - nearestNode.pos.x); // 求角度

        newNode.pos.x = nearestNode.pos.x + STEP_SZIE * Math.cos(theta);
        newNode.pos.y = nearestNode.pos.y + STEP_SZIE * Math.sin(theta);

        if(checkInsideBoundary(newNode.pos) && checkOutsideObstacles(newNode.pos)){
            rrt.addNode(newNode, nearestNodeID);
            success = true;
        }

        return success;
    }

    List<Point> plan(){
        int iter = 0;
        List<List<Integer>> paths = new ArrayList<>();
        boolean addNodeResult = false;
        boolean nodeToGoal = false;
        List<Integer> path = null;
        List<Point> result = new ArrayList<>();

        while (iter < MAX_ITERATIONS){
            if(paths.size() < PATHS_LIMIT){
                RRTNode node  =generateNode();
                addNodeResult = addNewNodeToRRT(node);
                if(addNodeResult){
                    nodeToGoal = checkNodeToGoal(endPos, node.pos);
                    if(nodeToGoal){
                        path = rrt.getRootToEndPath(node.nodeID);
                        paths.add(path);
                        System.out.println("Path size is : "+ paths.size()+", iter:"+iter);
                    }
                }
            }
            else {
                break; 
            }
            iter ++;
        }

        if(paths.size() == 0){
            result = null; 
        }

        int minSize = 999999;

        for (List<Integer> p: paths) {
            if(p.size() < minSize){
                path = p;
                minSize = p.size();
            }
        }

        for(int i:path){
            result.add(rrt.getNode(i).pos);
        }

        return result;

    }

}



public class RRTNode {
    public int nodeID; 
    public int parentID; 
    public List<Integer> children; 
    public Point pos; 
    public double costToRoot; 

    public RRTNode(){
        children = new ArrayList<>();
        pos = new Point();
    }
}
