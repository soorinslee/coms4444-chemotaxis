report:
	java -cp .:chemotaxis/org.json.jar chemotaxis.sim.Simulator --team g2 -r 1 -b 500 -m test.map -v -s 10 -l log.txt

verify:
	java -cp .:chemotaxis/org.json.jar chemotaxis.sim.Simulator --team dummy -m 50x50CenterBarricade.map -c -l log.txt

gui:
	java -cp .:chemotaxis/org.json.jar chemotaxis.sim.Simulator --team g2 --gui -l log.txt

compile:
	javac -cp .:chemotaxis/org.json.jar chemotaxis/*/*.java

clean:
	rm chemotaxis/*/*.class
