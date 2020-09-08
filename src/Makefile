report:
	java -cp .:chemotaxis/org.json.jar chemotaxis.sim.Simulator --team dummy -r 100 -b 50 -m dummy_map.map -s 10 -l log.txt

verify:
	java -cp .:chemotaxis/org.json.jar chemotaxis.sim.Simulator --team dummy -m dummy_map.map -c -l log.txt

gui:
	java -cp .:chemotaxis/org.json.jar chemotaxis.sim.Simulator --team dummy --gui

compile:
	javac -cp .:chemotaxis/org.json.jar chemotaxis/*/*.java

clean:
	rm chemotaxis/*/*.class
