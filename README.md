
# Project 2: Chemotaxis

## Course Summary

Course: COMS 4444 Programming and Problem Solving (Fall 2020)  
Website: http://www.cs.columbia.edu/~kar/4444f20  
University: Columbia University  
Instructor: Prof. Kenneth Ross  
TA: Aditya Sridhar

## Project Description


## Required Installations
Before you can start working with the simulator and implementing your code, you will first need to set up your environment. This requires both Java and Git.

### Java
The simulator is implemented in Java, and you will be required to submit Java code for your project. To check if you have Java already installed, run `javac -version` and `java -version` for the versions of the Java Development Kit (JDK) and Java Runtime Environment (JRE), respectively.

If you do not have Java set up, you will first need to install a JDK, which provides everything that allows you to write and execute Java code inside of a runtime environment. Please download the latest release of Java JDK for your OS [here](https://www.oracle.com/java/technologies/javase-downloads.html) (currently 14.0.2).
* Under Oracle Java SE 14 > Oracle JDK, click on *JDK Download*.
* Click on the installer link corresponding to your OS.
* Check the box to accept the license agreement, and click the download button.
* Once the installer has been downloaded, start the installer and complete the steps.
* Depending on your OS, you might need to set up some environment variables to run Java. This is especially true for Windows and Linux. As a recommendation, follow the instructions [here](https://www3.ntu.edu.sg/home/ehchua/programming/howto/JDK_Howto.html) to finish the Java environment setup for your OS (note that the website also has full step-by-step instructions that you can follow to install the JDK for your OS).
* Verify now that you have your JDK and JRE set up by rerunning `javac -version` and `java -version`.

You are now ready to start writing Java code!

It is also preferable to develop your Java code in an integrated development environment (IDE) such as [Eclipse](https://www.eclipse.org/downloads/) or [IntelliJ IDEA](https://www.jetbrains.com/idea/download/).

### Git
Version control with Git will be a large aspect of team-oriented development in this course. You will be managing and submitting your projects using Git. Mac and Linux users can access Git from their terminal. For Windows users, it is preferable to use a common emulator like "Git Bash" to access Git.

Please follow these instructions for installing Git and forking repositories:

1.  Make sure you have Git installed. Instructions on installing Git for your OS can be found [here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).
2.  You will need to set up SSH keys for each machine using Git, if you haven't done so. To set up SSH keys, please refer to this [page](https://docs.github.com/en/enterprise/2.20/user/github/authenticating-to-github/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent). Note that you only need to complete the subsection titled _Generating a new SSH key_ for your OS.
3.  Add your newly-generated SSH keys to the GitHub account, as done [here](https://docs.github.com/en/enterprise/2.20/user/github/authenticating-to-github/adding-a-new-ssh-key-to-your-github-account).
4.  Fork any repository (to fork, click on "Fork" in the top-right of the page), and clone the forked repository in your local machine inside the parent directory that will house the repository (to copy the remote URL for cloning, click on the "Clone" button, make sure that "Clone with SSH" is visible - the remote URL should start with `git@github.com` - and copy the URL to the clipboard). You should now be able to stage, commit, push, and pull changes using Git.

## Implementation

You will be creating your own controller and agent that extends the simulator's abstract controller and agent, respectively. Please follow these steps to begin your implementation:
1.  Enter the `coms4444-chemotaxis/src/chemotaxis` directory, and create a folder called "g*x*" (where *x* is the number of your team). For example, if you are team "g5," please create a folder called "g5" in the `chemotaxis` directory.
2.  Create Java files called `Controller.java` and `Agent.java` inside your newly-created folder.
3.  Copy the following code into `Controller` (the TODOs indicate all changes you need to make):
```
package chemotaxis.gx; // TODO modify the package name to reflect your team

import java.awt.Point;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.Gradient;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {

   /**
    * Controller constructor
    *
    * @param start       start cell coordinates
    * @param target      target cell coordinates
    * @param simTime     simulation time
    * @param budget      chemical budget
    * @param seed        random seed
    * @param simPrinter  simulation printer
    *
    */
   public Controller(Point start, Point target, Integer simTime, Integer budget, Integer seed, SimPrinter simPrinter) {
      super(start, target, simTime, budget, seed, simPrinter);
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
   public ChemicalPlacement applyChemicals(Integer currentTurn, Integer chemicalsRemaining, Point currentLocation, Gradient[][] grid) {
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
import chemotaxis.sim.Gradient;
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
    * @param currentGradient  current cell's gradient
    * @param neighborMap      map of cell's neighbors
    * @return                 agent move
    *
    */
   @Override
   public Move makeMove(Integer randomNum, Byte previousState, Gradient currentGradient, Map<DirectionType, Gradient> neighborMap) {
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

## GUI Features


## API Description

The following provides the API available for students to use:

Classes that are used by the simulator include:

## Piazza
If you have any questions about the project, please post them in the [Piazza forum](https://piazza.com/class/kdjd7v2b8925zz?cid=8) for the course, and an instructor will reply to them as soon as possible. Any updates to the project itself will be available in Piazza.


## Disclaimer
This project belongs to Columbia University. It may be freely used for educational purposes.
