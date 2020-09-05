package chemotaxis.sim;

import java.awt.Point;

public class ControllerWrapper {

	private Timer timer;
    private Controller controller;
    private String controllerName;
    private long timeout;

    public ControllerWrapper(Controller controller, String controllerName, long timeout) {
        this.controller = controller;
        this.controllerName = controllerName;
        this.timeout = timeout;
        this.timer = new Timer();
    }

    public ChemicalPlacement applyChemicals(Integer currentTurn, Integer chemicalsRemaining, Point currentLocation, Gradient[][] grid) {

    	Log.writeToVerboseLogFile("Team " + this.controllerName + "'s controller applying chemicals on turn " + currentTurn + "...");
        
    	ChemicalPlacement chemicalPlacement = new ChemicalPlacement();

        try {
            if(!timer.isAlive())
            	timer.start();
            timer.callStart(() -> { return controller.applyChemicals(currentTurn, chemicalsRemaining, currentLocation, grid); });
            chemicalPlacement = timer.callWait(timeout);
        }
        catch(Exception e) {
            Log.writeToVerboseLogFile("Team " + this.controllerName + "'s controller has possibly timed out.");
            Log.writeToVerboseLogFile("Exception for team " + this.controllerName + "'s controller: " + e);
        }

        return chemicalPlacement;
    }
    
    public Controller getController() {
    	return controller;
    }

    public String getControllerName() {
        return controllerName;
    }
}