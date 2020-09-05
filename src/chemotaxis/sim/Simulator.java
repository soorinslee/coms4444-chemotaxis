/*
    Project: Chemotaxis
    Course: COMS 4444 Programming & Problem Solving (Fall 2020)
    Instructor: Prof. Kenneth Ross
    URL: http://www.cs.columbia.edu/~kar/4444f20
    Author: Aditya Sridhar
    Simulator Version: 1.0
*/

package chemotaxis.sim;

import java.awt.Desktop;
import java.awt.Point;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.json.JSONException;
import org.json.JSONObject;

public class Simulator {
	
	// Simulator structures
	private static String teamName, mapName;
	private static ControllerWrapper controllerWrapper;
	private static Gradient[][] grid;
	private static Random random;
	
	// Simulator inputs
	private static int seed = 10;
	private static int turns = 100;
	private static int budget = 50;
	private static double fpm = 15;
	private static boolean showGUI = false;
	private static boolean verifyMap = false;
	
	// Defaults
	private static boolean enableControllerPrints = false;
	private static boolean enableAgentPrints = false;
	private static int chemicalsRemaining = budget;
	private static Point start, target, agentLocation;
	private static List<Point> blockedLocations;
	private static int mapSize = 100;
	private static long timeout = 1000;
	private static int currentTurn = 0;
	private static String version = "1.0";
	private static String projectPath, sourcePath, staticsPath;
    

	private static void setup() {
		random = new Random(seed);
		projectPath = new File(".").getAbsolutePath().substring(0, 
				new File(".").getAbsolutePath().indexOf("coms4444-chemotaxis") + "coms4444-chemotaxis".length());
		sourcePath = projectPath + File.separator + "src";
		staticsPath = projectPath + File.separator + "statics";
	}
	
	private static void parseCommandLineArguments(String[] args){
		blockedLocations = new ArrayList<>();
		for(int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if(args[i].equals("-t") || args[i].equals("--team")) {
                        i++;
                    	if(i == args.length) 
                            throw new IllegalArgumentException("The team name is missing!");
                        teamName = args[i];
                        try {
                        	controllerWrapper = loadControllerWrapper();
        				} catch (Exception e) {
        					Log.writeToLogFile("Unable to load controller: " + e.getMessage());
        				}
                    }
                    else if(args[i].equals("-g") || args[i].equals("--gui"))
                        showGUI = true;
                    else if(args[i].equals("-c") || args[i].equals("--check"))
                    	verifyMap = true;
                    else if(args[i].equals("-l") || args[i].equals("--log")) {
                        i++;
                    	if(i == args.length) 
                            throw new IllegalArgumentException("The log file path is missing!");
                        Log.setLogFile(args[i]);
                        Log.assignLoggingStatus(true);
                    }
                    else if(args[i].equals("-v") || args[i].equals("--verbose"))
                        Log.assignVerbosityStatus(true);
                    else if(args[i].equals("-f") || args[i].equals("--fpm")) {
                    	i++;
                        if(i == args.length)
                            throw new IllegalArgumentException("The GUI frames per minute is missing!");
                        fpm = Double.parseDouble(args[i]);
                    }
                    else if(args[i].equals("-b") || args[i].equals("--budget")) {
                    	i++;
                        if(i == args.length) 
                            throw new IllegalArgumentException("The chemical budget is missing!");
                        budget = Integer.parseInt(args[i]);
                        chemicalsRemaining = budget;
                    }
                    else if(args[i].equals("-m") || args[i].equals("--map")) {
                    	i++;
                        if(i == args.length) 
                            throw new IllegalArgumentException("The map is missing!");
                        mapName = args[i];
                    }
                    else if(args[i].equals("-s") || args[i].equals("--seed")) {
                    	i++;
                        if(i == args.length) 
                            throw new IllegalArgumentException("The seed number is missing!");
                        seed = Integer.parseInt(args[i]);
                    }
                    else if(args[i].equals("-r") || args[i].equals("--turns")) {
                    	i++;
                        if (i == args.length)
                            throw new IllegalArgumentException("The total number of turns is not specified!");
                        turns = Integer.parseInt(args[i]);
                    }
                    else 
                        throw new IllegalArgumentException("Unknown argument \"" + args[i] + "\"!");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument \"" + args[i] + "\"!");
            }
        }		
	}
	
	private static void readMap() throws FileNotFoundException, IOException {
		if(mapName != null) {
			File mapFile;
			Scanner scanner;
			try {
				mapFile = new File(sourcePath + File.separator + "maps" + File.separator + teamName + File.separator + mapName);
				scanner = new Scanner(mapFile);
			} catch(FileNotFoundException e) {
                throw new FileNotFoundException("Map file was not found!");
			}
			
			try {
				mapSize = Integer.parseInt(scanner.nextLine().strip());
				grid = new Gradient[mapSize][mapSize];
				for(int i = 0; i < grid.length; i++)
					for(int j = 0; j < grid[0].length; j++)
						grid[i][j] = new Gradient(true);
			} catch(Exception e) {
				scanner.close();
                throw new IOException("Unable to determine map size!");
			}

			try {
				String[] startAndTargetElements = scanner.nextLine().strip().split(" ");
				int startX = Integer.parseInt(startAndTargetElements[0]);
				int startY = Integer.parseInt(startAndTargetElements[1]);
				int targetX = Integer.parseInt(startAndTargetElements[2]);
				int targetY = Integer.parseInt(startAndTargetElements[3]);	
				start = new Point(startX, startY);
				target = new Point(targetX, targetY);
			} catch(Exception e) {
				scanner.close();
                throw new IOException("Unable to identify start and target locations");
			}

			try {
				while(scanner.hasNextLine()) {
					String[] blockedLocationElements = scanner.nextLine().strip().split(" ");
					int blockedX = Integer.parseInt(blockedLocationElements[0]);
					int blockedY = Integer.parseInt(blockedLocationElements[1]);
					blockedLocations.add(new Point(blockedX, blockedY));
				}
			} catch(Exception e) {
				scanner.close();
                throw new IOException("Cannot interpret one or more blocked cells!");
			}
			
			scanner.close();
		}
	}
	
	private static boolean checkMap() throws IOException {
		
		if(start.x < 1 || start.y > mapSize)
			throw new IOException("Start location (" + start.x + ", " + start.y + ") is out of bounds!");

		if(target.x < 1 || target.y > mapSize)
			throw new IOException("Target location (" + target.x + ", " + target.y + ") is out of bounds!");
		
		for(Point location : blockedLocations)
			if(location.x < 1 || location.y > mapSize)
				throw new IOException("Blocked location (" + location.x + ", " + location.y + ") is out of bounds!");
		
		List<Point> unvisitedLocations = new ArrayList<>();
		for(int i = 1; i <= mapSize; i++) {
			for(int j = 1; j <= mapSize; j++) {
				Point location = new Point(i, j);
				if(!blockedLocations.contains(location)) {
					unvisitedLocations.add(location);
				}
			}
		}
		visitLocations(unvisitedLocations, new ArrayList<>(), 1, 1);

		return unvisitedLocations.isEmpty();
	}
	
	private static void visitLocations(List<Point> unvisitedLocations, List<Point> visitedLocations, int row, int column) {
		if(row < 1 || row > mapSize || column < 1 || column > mapSize)
			return;
		
		Point location = new Point(row, column);
		if(visitedLocations.contains(location) || !unvisitedLocations.contains(location))
			return;
		if(unvisitedLocations.contains(location)) {
			visitedLocations.add(location);
			unvisitedLocations.remove(location);
		}
		
		visitLocations(unvisitedLocations, visitedLocations, row + 1, column);
		visitLocations(unvisitedLocations, visitedLocations, row, column + 1);
	}
		
	private static void placeChemicals(ChemicalPlacement chemicalPlacement) {
		Point location = chemicalPlacement.location;
		List<ChemicalType> chemicals = chemicalPlacement.chemicals;
		
		if(location == null || (location != null && (location.x < 1 || location.y > mapSize))) {
			Log.writeToLogFile("Warning: location (" + location.x + ", " + location.y + ") for chemical placement is invalid. No chemicals placed.");
			return;
		}
		
		if(chemicals.size() > chemicalsRemaining) {
			Log.writeToLogFile("Warning: not enough chemicals remaining (" + chemicalsRemaining + 
					" chemicals) to complete placement request (" + chemicals.size() + " chemicals). No chemicals placed.");
			return;
		}
		
		for(ChemicalType chemical : chemicals) {
			grid[location.x][location.y].applyConcentration(chemical);
			chemicalsRemaining--;
		}
	}

	private static void moveAgent(DirectionType directionType) {		
		switch(directionType) {
		case NORTH:
			if(agentLocation.x > 1)
				agentLocation.setLocation(agentLocation.x - 1, agentLocation.y);
			break;
		case SOUTH:
			if(agentLocation.x < mapSize)
				agentLocation.setLocation(agentLocation.x + 1, agentLocation.y);
			break;
		case EAST:
			if(agentLocation.y < mapSize)
				agentLocation.setLocation(agentLocation.x, agentLocation.y + 1);
			break;
		case WEST:
			if(agentLocation.y > 1)
				agentLocation.setLocation(agentLocation.x, agentLocation.y - 1);
			break;
		case CURRENT: break;
		default: break;
		}
	}

	private static boolean agentAtTarget() {
		return agentLocation.equals(target);
	}
	
	private static void diffuseCells() {
		Gradient[][] newGrid = grid.clone();
		for(int i = 0; i < newGrid.length; i++) {
			for(int j = 0; j < newGrid[0].length; j++) {
				Gradient gradient = newGrid[i][j];

				if(gradient.isBlocked())
					continue;
				
				for(ChemicalType chemicalType : ChemicalType.values()) {
					double concentrationSum = gradient.getConcentration(chemicalType);
					int numUnblockedCells = 1;
					
					if(i > 1 && !newGrid[i - 1][j].isBlocked()) {
						concentrationSum += newGrid[i - 1][j].getConcentration(chemicalType);
						numUnblockedCells++;
					}
					if(i < mapSize && !newGrid[i + 1][j].isBlocked()) {
						concentrationSum += newGrid[i + 1][j].getConcentration(chemicalType);
						numUnblockedCells++;
					}
					if(j > 1 && !newGrid[i][j - 1].isBlocked()) {
						concentrationSum += newGrid[i][j - 1].getConcentration(chemicalType);
						numUnblockedCells++;
					}
					if(j < mapSize && !newGrid[i][j + 1].isBlocked()) {
						concentrationSum += newGrid[i][j + 1].getConcentration(chemicalType);
						numUnblockedCells++;
					}
					
					double averageConcentration = concentrationSum / numUnblockedCells;									
					gradient.setConcentration(chemicalType, averageConcentration);
				}				
			}
		}		
		grid = newGrid;
	}
	
	private static void runSimulation() throws IOException, JSONException {
		
		HTTPServer server = null;
		
		Log.writeToLogFile("\n");
        Log.writeToLogFile("Project: Chemotaxis");
        Log.writeToLogFile("Simulator Version: " + version);
        Log.writeToLogFile("Team: " + teamName);
        Log.writeToLogFile("GUI: " + (showGUI ? "enabled" : "disabled"));
        Log.writeToLogFile("\n");

		boolean mapIsValid = checkMap();
        
		if(showGUI) {
            server = new HTTPServer();
            Log.writeToLogFile("Hosting the HTTP Server on " + server.addr());
            if(!Desktop.isDesktopSupported())
                Log.writeToLogFile("Desktop operations not supported!");
            else if(!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
                Log.writeToLogFile("Desktop browse operation not supported!");
            else {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:" + server.port()));
                } catch(URISyntaxException e) {}
            }
        }
		else if(verifyMap) {
			if(mapIsValid)
				Log.writeToLogFile("The map is valid!");
			else
				Log.writeToLogFile("The map is not valid!");
		}			
		else {
			boolean agentReached = false;
			
			Byte previousState = 0;
			currentTurn = 0;
			for(int i = 1; i <= turns; i++) {
				if(agentAtTarget()) {
					agentReached = true;
					break;
				}
				
				currentTurn++;

				ChemicalPlacement chemicalPlacement = controllerWrapper.applyChemicals(currentTurn, chemicalsRemaining, deepClone(agentLocation), deepClone(grid));
				placeChemicals(chemicalPlacement);
				
				try {
					AgentWrapper agentWrapper = loadAgentWrapper();
					
					Map<DirectionType, Gradient> neighborMap = new HashMap<>();

					if(agentLocation.x == 1)
						neighborMap.put(DirectionType.NORTH, new Gradient(false));
					else
						neighborMap.put(DirectionType.NORTH, grid[agentLocation.x - 1][agentLocation.y]);

					if(agentLocation.x == mapSize)
						neighborMap.put(DirectionType.SOUTH, new Gradient(false));
					else
						neighborMap.put(DirectionType.SOUTH, grid[agentLocation.x + 1][agentLocation.y]);

					if(agentLocation.y == mapSize)
						neighborMap.put(DirectionType.EAST, new Gradient(false));
					else
						neighborMap.put(DirectionType.EAST, grid[agentLocation.x][agentLocation.y + 1]);

					if(agentLocation.y == 1)
						neighborMap.put(DirectionType.WEST, new Gradient(false));
					else
						neighborMap.put(DirectionType.WEST, grid[agentLocation.x][agentLocation.y - 1]);
					
					Move move = agentWrapper.makeMove(random.nextInt(), previousState, deepClone(grid[agentLocation.x][agentLocation.y]), deepClone(neighborMap));
					moveAgent(move.directionType);
					previousState = move.currentState;
										
				} catch (Exception e) {
					Log.writeToLogFile("Unable to load or run agent: " + e.getMessage());
				}
								
				diffuseCells();
			}
			
			if(agentReached || agentAtTarget())
				Log.writeToLogFile("The agent successfully reached the target (" + target.x + ", " + target.y + 
						") from (" + start.x + ", " + start.y + ") in " + currentTurn + " turns!");
			else
				Log.writeToLogFile("The agent failed to reach the target (" + target.x + ", " + target.y + 
						") from (" + start.x + ", " + start.y + ") in the allotted time.");
			Log.writeToLogFile("Final time: " + currentTurn);
		}
		
		if(!showGUI)
			System.exit(1);
	}
	
	private static <T extends Object> T deepClone(T obj) {
        if(obj == null)
            return null;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            
            return (T) objectInputStream.readObject();
        }
        catch(Exception e) {
            return null;
        }
	}
	
	private static ControllerWrapper loadControllerWrapper() throws Exception {
		Log.writeToLogFile("Loading team " + teamName + "'s controller...");

		Controller controller = loadController();
        if(controller == null) {
            Log.writeToLogFile("Cannot load team " + teamName + "'s controller!");
            System.exit(1);
        }

        return new ControllerWrapper(controller, teamName, timeout);
    }
	
	private static Controller loadController() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String controllerPackagePath = sourcePath + File.separator + "chemotaxis" + File.separator + teamName;
        Set<File> controllerFiles = getFilesInDirectory(controllerPackagePath, ".java");
		String simPath = sourcePath + File.separator + "chemotaxis" + File.separator + "sim";
        Set<File> simFiles = getFilesInDirectory(simPath, ".java");

        File classFile = new File(controllerPackagePath + File.separator + "Controller.class");

        long classModified = classFile.exists() ? classFile.lastModified() : -1;
        if(classModified < 0 || classModified < lastModified(controllerFiles) || classModified < lastModified(simFiles)) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if(compiler == null)
                throw new IOException("Cannot find the Java compiler!");

            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Log.writeToLogFile("Compiling for team " + teamName + "'s controller...");

            if(!compiler.getTask(null, manager, null, null, null, manager.getJavaFileObjectsFromFiles(controllerFiles)).call())
                throw new IOException("The compilation failed!");
            
            classFile = new File(controllerPackagePath + File.separator + "Controller.class");
            if(!classFile.exists())
                throw new FileNotFoundException("The class file is missing!");
        }

        ClassLoader loader = Simulator.class.getClassLoader();
        if(loader == null)
            throw new IOException("Cannot find the Java class loader!");

        @SuppressWarnings("rawtypes")
        Class rawClass = loader.loadClass("chemotaxis." + teamName + ".Controller");
        Class[] classArgs = new Class[]{Point.class, Point.class, Integer.class, Integer.class, Integer.class, SimPrinter.class};

        return (Controller) rawClass.getDeclaredConstructor(classArgs).newInstance(start, target, turns, budget, seed, new SimPrinter(enableControllerPrints));
    }

	private static AgentWrapper loadAgentWrapper() throws Exception {
		Log.writeToLogFile("Loading team " + teamName + "'s agent...");

		Agent agent = loadAgent();
        if(agent == null) {
            Log.writeToLogFile("Cannot load team " + teamName + "'s agent!");
            System.exit(1);
        }

        return new AgentWrapper(agent, teamName, timeout);
    }
	
	private static Agent loadAgent() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String agentPackagePath = sourcePath + File.separator + "chemotaxis" + File.separator + teamName;
        Set<File> agentFiles = getFilesInDirectory(agentPackagePath, ".java");
		String simPath = sourcePath + File.separator + "chemotaxis" + File.separator + "sim";
        Set<File> simFiles = getFilesInDirectory(simPath, ".java");

        File classFile = new File(agentPackagePath + File.separator + "Agent.class");

        long classModified = classFile.exists() ? classFile.lastModified() : -1;
        if(classModified < 0 || classModified < lastModified(agentFiles) || classModified < lastModified(simFiles)) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if(compiler == null)
                throw new IOException("Cannot find the Java compiler!");

            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Log.writeToLogFile("Compiling for team " + teamName + "'s agent...");

            if(!compiler.getTask(null, manager, null, null, null, manager.getJavaFileObjectsFromFiles(agentFiles)).call())
                throw new IOException("The compilation failed!");
            
            classFile = new File(agentPackagePath + File.separator + "Agent.class");
            if(!classFile.exists())
                throw new FileNotFoundException("The class file is missing!");
        }

        ClassLoader loader = Simulator.class.getClassLoader();
        if(loader == null)
            throw new IOException("Cannot find the Java class loader!");

        @SuppressWarnings("rawtypes")
        Class rawClass = loader.loadClass("chemotaxis." + teamName + ".Agent");
        Class[] classArgs = new Class[]{SimPrinter.class};

        return (Agent) rawClass.getDeclaredConstructor(classArgs).newInstance(new SimPrinter(enableAgentPrints));
    }
	
	private static long lastModified(Iterable<File> files) {
        long lastDate = 0;
        for(File file : files) {
            long date = file.lastModified();
            if(lastDate < date)
                lastDate = date;
        }
        return lastDate;
    }
	
	private static Set<File> getFilesInDirectory(String path, String extension) {
		Set<File> files = new HashSet<File>();
        Set<File> previousDirectories = new HashSet<File>();
        previousDirectories.add(new File(path));
        do {
        	Set<File> nextDirectories = new HashSet<File>();
            for(File previousDirectory : previousDirectories)
                for(File file : previousDirectory.listFiles()) {
                    if(!file.canRead())
                    	continue;
                    
                    if(file.isDirectory())
                        nextDirectories.add(file);
                    else if(file.getPath().endsWith(extension))
                        files.add(file);
                }
            previousDirectories = nextDirectories;
        } while(!previousDirectories.isEmpty());
        
        return files;
	}
	
	private static void updateGUI(HTTPServer server, String content) {
		if(server == null)
			return;
		
        String guiPath = null;
        while(true) {
            while(true) {
                try {
                	guiPath = server.request();
                    break;
                } catch(IOException e) {
                    Log.writeToVerboseLogFile("HTTP request error: " + e.getMessage());
                }
            }
            
            if(guiPath.equals("data.txt")) {
                try {
                    server.reply(content);
                } catch(IOException e) {
                    Log.writeToVerboseLogFile("HTTP dynamic reply error: " + e.getMessage());
                }
                return;
            }
            
            if(guiPath.equals(""))
            	guiPath = "webpage.html";
            else if(!Character.isLetter(guiPath.charAt(0))) {
                Log.writeToVerboseLogFile("Potentially malicious HTTP request: \"" + guiPath + "\"");
                break;
            }

            try {
                File file = new File(staticsPath + File.separator + guiPath);
                server.reply(file);
            } catch(IOException e) {
                Log.writeToVerboseLogFile("HTTP static reply error: " + e.getMessage());
            }
        }		
	}
	
	private static String getGUIState(int turn) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("refresh", 60000.0 / fpm);
		jsonObj.put("totalTurns", turns);
		jsonObj.put("currentTurn", turn);
		
        return jsonObj.toString();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, JSONException {
		setup();
		parseCommandLineArguments(args);
		readMap();		
		runSimulation();
	}
}