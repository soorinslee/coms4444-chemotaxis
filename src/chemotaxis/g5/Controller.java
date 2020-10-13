package chemotaxis.g5;

import java.awt.GridBagConstraints;
import java.util.Queue;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {

	List<Point> routeList = new ArrayList<Point>();
	int colorIndex = 0;
	int lastSpotIndex = 0;
	boolean endReached = false;
	/**
	 * Controller constructor
	 *
	 * @param start       start cell coordinates
	 * @param target      target cell coordinates
	 * @param size        grid/map size
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

		Point nextCorner, nextPlacement;

		List<ChemicalType> chemicals = new ArrayList<>();

		ChemicalPlacement chemicalPlacement = new ChemicalPlacement();

		if (currentTurn == 1) {
 			routeList = getShortestPath(start, target, grid);
 			//simPrinter.println(routeList);
 			for (Point p: routeList) {
 				simPrinter.println(p.x + " " + p.y);
 			}
 		}

		else if (currentTurn % 5 == 0 && !(routeList.get(lastSpotIndex).equals(target))) {
			nextCorner = getNextCorner();
			simPrinter.println("nextCorner: " + nextCorner.x + " " + nextCorner.y);
			setNextPlacement(nextCorner, chemicalPlacement, chemicals);
		}

		else if (endReached)
			simPrinter.println("Turn: " + currentTurn + "  at: " + currentLocation.x + " " + currentLocation.y);

		return chemicalPlacement;
	}

	public void populateRouteList(){
		for (int j = 1; j < 5; j++){
			Point k = new Point();
			k.x = 1;
			k.y = j;
			routeList.add(k);
		}
		for (int j = 1; j < 13; j++){
			Point k = new Point();
			k.x = 1 + j;
			k.y = 4;
			routeList.add(k);
		}
		for (int j = 1; j < 6; j++){
			Point k = new Point();
			k.x = 13;
			k.y = 4 + j;
			routeList.add(k);
		}
		for (int j = 1; j < 9; j++){
			Point k = new Point();
			k.x = 13 + j;
			k.y = 9;
			routeList.add(k);
		}
		for (int j = 1; j < 17; j++){
			Point k = new Point();
			k.x = 21;
			k.y = 9+j;
			routeList.add(k);
		}
		for (int j = 1; j < 5; j++){
			Point k = new Point();
			k.x = 21 + j;
			k.y = 25;
			routeList.add(k);
		}
	}

	public List<Point> getShortestPath(Point start, Point target, ChemicalCell[][] grid) {
		
		boolean[][] visited = new boolean[grid.length][grid[0].length];
		
		Node source = new Node(start.x, start.y);
		Queue<Node> queue = new LinkedList<Node>(); 
		queue.add(source);
		//simPrinter.println(start.x);
		//simPrinter.println(start.y);
		Node solution = null; 
		while (!queue.isEmpty()) {
			Node popped = queue.poll(); 
			if (popped.x == target.x && popped.y == target.y) {
				//simPrinter.println("reached target: ");
				solution = popped;
				break;
			}
			else if (!visited[popped.x-1][popped.y-1] && !grid[popped.x-1][popped.y-1].isBlocked()) {
				visited[popped.x-1][popped.y-1] = true;
				List<Node> neighborList = addNeighbors(popped, grid, visited);

				queue.addAll(neighborList);
			}
		}
		//simPrinter.println("PRE");
		List<Point> path = new LinkedList<Point>(); 
		while (solution != null) {
			path.add(new Point(solution.x, solution.y));
			//simPrinter.println(solution.x + " " + solution.y);
			solution = solution.parent;
			
		}
		//simPrinter.println("POST");
		Collections.reverse(path);
		for (Point p: path) {
 				//simPrinter.println(p.x + " " + p.y);
 		}
		return path;
	}
	
	private List<Node> addNeighbors(Node current, ChemicalCell[][] grid, boolean[][] visited) {
		List<Node> list = new LinkedList<Node>();
		//////simPrinter.println("entered method");
		////simPrinter.println(current.x);
		////simPrinter.println(current.y);
		
		if((current.x - 1 > 0) && !visited[current.x - 2][current.y - 1]) {
			Node currNode = new Node(current.x-1, current.y);
			currNode.parent = current;
			list.add(currNode);
			//simPrinter.println("added 1: " + currNode.x + " " + currNode.y);
		}
		if((current.x + 1 <= grid.length) && !visited[current.x][current.y - 1]) {
			Node currNode = new Node(current.x+1, current.y);
			currNode.parent = current;
			list.add(currNode);
			//simPrinter.println("added 2: " + currNode.x + " " + currNode.y);
		}
		if((current.y - 1 > 0) && !visited[current.x - 1][current.y - 2]) {
			Node currNode = new Node(current.x, current.y - 1);
			currNode.parent = current;
			list.add(currNode);
			//simPrinter.println("added 3: " + currNode.x + " " + currNode.y);
		}
		if((current.y + 1 <= grid.length) && !visited[current.x - 1][current.y]) {
			Node currNode = new Node(current.x, current.y + 1);
			currNode.parent = current;
			list.add(currNode);
			//simPrinter.println("added 4: " + currNode.x + " " + currNode.y);

		}		
		return list;
	}
	
	class Node {
	    int x;
	    int y; 
	    Node parent;
	    
	    public Node(int x, int y) {
	    	this.x = x;
	    	this.y = y;
	    }
	}
	

	public void setNextPlacement(Point nextCorner, ChemicalPlacement chemicalPlacement, List<ChemicalType> chemicals){
		
		Point nextSpot = new Point();

		for (int i = 0; i < 5; i++){
			nextSpot = routeList.get(i + lastSpotIndex);
			
			if (nextSpot.equals(this.target)){
				endReached = true;
				break;
			}
			else if (nextSpot.equals(nextCorner))
				break;
		}

		chemicalPlacement.location = nextSpot;
		lastSpotIndex = routeList.indexOf(nextSpot);
		simPrinter.println("Drop Point: " +nextSpot.x + " " + nextSpot.y + " At: " + lastSpotIndex);

		switch (colorIndex) {
			case 0: chemicals.add(ChemicalType.RED);
					colorIndex = 1; 
			case 1:	chemicals.add(ChemicalType.BLUE);
					colorIndex = 2;
			case 2: chemicals.add(ChemicalType.GREEN);
					colorIndex = 0;			
		}
		chemicalPlacement.chemicals = chemicals;
	}

	public Point getNextCorner(){
		int i = 0;
		Point lastSpot = routeList.get(lastSpotIndex);
		simPrinter.println("Point: " +lastSpot.x + " " + lastSpot.y + " At: " + lastSpotIndex);
		if (!lastSpot.equals(target))
			while (lastSpot.x == routeList.get(lastSpotIndex + i).x || lastSpot.y == routeList.get(lastSpotIndex + i).y){
				if (routeList.get(lastSpotIndex + i).equals(target)){
					break;
				}
				i++;
			}
		return routeList.get(i + lastSpotIndex);
	}
}