
# Project 2: Chemotaxis

## Course Summary

Course: COMS 4444 Programming and Problem Solving (Fall 2020)  
Website: http://www.cs.columbia.edu/~kar/4444f20  
University: Columbia University  
Instructor: Prof. Kenneth Ross  
TA: Aditya Sridhar

## Project Description


## Implementation

You will be creating your own controller and agent that extend the simulator's abstract controller and agent, respectively. Please follow these steps to begin your implementation:
1.  Enter the `coms4444-chemotaxis/src/chemotaxis` directory, and create a folder called "g*x*" (where *x* is the number of your team). For example, if you are team "g5," please create a folder called "g5" in the `chemotaxis` directory.
2.  Create Java files called `Controller.java` and `Agent.java` inside your newly-created folder.
3.  Copy the following code into `Controller` (the TODOs indicate all changes you need to make):
```
package chemotaxis.gx; // TODO modify the package name to reflect your team

import java.awt.Point;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {

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
      // TODO add your code here to apply chemicals

      return null; // TODO modify the return statement to return your chemical placement
   }
}
```
4.  Copy the following code into `Agent` (the TODOs indicate all changes you need to make):
```
package chemotaxis.gx; // TODO modify the package name to reflect your team

import java.util.Map;

import chemotaxis.sim.DirectionType;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.Move;
import chemotaxis.sim.SimPrinter;

public class Agent extends chemotaxis.sim.Agent {

   /**
    * Agent constructor
    *
    * @param simPrinter  simulation printer
    *
    */
   public Agent(SimPrinter simPrinter) {
      super(simPrinter);
   }

   /**
    * Move agent
    *
    * @param randomNum        random number available for agents
    * @param previousState    byte of previous state
    * @param currentCell      current cell
    * @param neighborMap      map of cell's neighbors
    * @return                 agent move
    *
    */
   @Override
   public Move makeMove(Integer randomNum, Byte previousState, ChemicalCell currentCell, Map<DirectionType, ChemicalCell> neighborMap) {
      // TODO add your code here to move the agent

      return null; // TODO modify the return statement to return your agent move
   }
}
```


## Submission
You will be submitting your created team folder, which includes the implemented `Controller` class, `Agent` class, and any other helper classes you create. We ask that you please do not modify any code in the `sim` or `dummy` directories, especially the simulator, when you submit your code. This makes it easier for us to merge in your code.

To submit your code for each class and for the final deliverable of the project, you will create a pull request to merge your forked repository's *master* branch into the TA's base repository's *master* branch. The TA will merge the commits from the pull request after the deliverable deadline has passed. The base repository will be updated before the start of the next class meeting.

In order to improve performance and readability of code during simulations, we would like to prevent flooding the console with print statements. Therefore, we have provided a printer called `SimPrinter` to allow for toggled printing to the console. When adding print statements for testing/debugging in your code, please make sure to use the methods in `SimPrinter` (instance available in `Player`) rather than use `System.out` statements directly. Additionally, please set the `enablePrints` default variable in `Simulator` to *true* in order to enable printing. This also allows us to not require that you comment out any print statements in your code submissions.


## Simulator

#### Steps to run the simulator:
1.  On your command line, *fork* the Git repository, and then clone the forked version. Do NOT clone the original repository.
2.  Enter `cd coms4444-chemotaxis/src` to enter the source folder of the repository.
3.  Run `make clean` and `make compile` to clean and compile the code.
4.  Run one of the following:
    * `make report`: report simulation results to the console/log file using command-line simulation arguments
    * `make verify`: verify that a map configuration is valid
    * `make gui`: run simulations from the GUI with live modifications to simulation arguments

#### Simulator arguments:
> **[-r | --turns]**: total number of turns (default = 100)

> **[-t | --team]**: team/player

> **[-b | --budget]**: chemical budget

> **[-c | --check]**: verify map validity when a map is specified

> **[-m PATH | --map PATH]**: path to the simulation map, specifying the map size and locations of blocked cells

> **[-s | --seed]**: seed value for random player (default = 10)

> **[-l PATH | --log PATH]**: enable logging and output log to both console and log file

> **[-v | --verbose]**: record verbose log when logging is enabled (default = false)

> **[-g | --gui]**: enable GUI (default = false)

> **[-f | --fpm]**: speed (frames per minute) of GUI when continuous GUI is enabled (default = 15)


## Map Configuration


## API Description

The following provides the API available for students to use:

Classes that are used by the simulator include:

## Piazza
If you have any questions about the project, please post them in the [Piazza forum](https://piazza.com/class/kdjd7v2b8925zz?cid=8) for the course, and an instructor will reply to them as soon as possible. Any updates to the project itself will be available in Piazza.


## Disclaimer
This project belongs to Columbia University. It may be freely used for educational purposes.
