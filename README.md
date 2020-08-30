
# Project 2: Chemotaxis

## Course Summary

Course: COMS 4444 Programming and Problem Solving (Fall 2020)  
Website: http://www.cs.columbia.edu/~kar/4444f20  
University: Columbia University  
Instructor: Prof. Kenneth Ross  
TAs: Aditya Sridhar, *<TA 2>*

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


## Submission
You will be submitting your created team folder, which includes the implemented `Player` class and any other helper classes you create. We ask that you please do not modify any code in the `sim` or `random` directories, especially the simulator, when you submit your code. This makes it easier for us to merge in your code.

To submit your code for each class and for the final deliverable of the project, you will create a pull request to merge your forked repository's *master* branch into the TA's base repository's *master* branch. The TA will merge the commits from the pull request after the deliverable deadline has passed. The base repository will be updated before the start of the next class meeting.

Additionally, please comment out or remove any print statements you may have included, as flooding the console with print statements during the simulation will weaken performance and readability.

## Simulator

#### Steps to run the simulator:
1.  On your command line, *fork* the Git repository, and then clone the forked version. Do NOT clone the original repository.
2.  Enter `cd coms4444-chemotaxis/src` to enter the source folder of the repository.
3.  Run `make clean` and `make compile` to clean and compile the code.
4.  Update the make file (file called `Makefile`) with the teams participating in the game, as well as with any simulator arguments.
5.  Run one of the following:
    * `make run`: view results from the command line
    * `make gui`: view results from the GUI

#### Simulator arguments:

## GUI Features


## API Description

The following provides the API available for students to use:

Classes that are used by the simulator include:

## Piazza
If you have any questions about the project, please post them in the [Piazza forum](https://piazza.com/class/kdjd7v2b8925zz?cid=8) for the course, and an instructor will reply to them as soon as possible. Any updates to the project itself will be available on Piazza.


## Disclaimer
This project belongs to Columbia University. It may be freely used for educational purposes.
