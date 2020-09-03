package sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gradient {
	
	private Map<ChemicalType, Double> concentrations = new HashMap<>();
	private boolean isBlocked = false;
	
	public Gradient() {
		new Gradient(false);
	}
	
	public Gradient(boolean isBlocked) {
		ChemicalType[] chemicalTypes = ChemicalType.values();
		for(ChemicalType chemicalType : chemicalTypes)
			concentrations.put(chemicalType, 0.0);
	}
	
	public void applyConcentration(ChemicalType chemicalType) {
		if(!isBlocked)
			concentrations.put(chemicalType, 1.0);
	}
	
	public Double getConcentration(ChemicalType chemicalType) {
		return concentrations.get(chemicalType);
	}
	
	public void setConcentration(ChemicalType chemicalType, Double concentration) {
		if(!isBlocked)
			concentrations.put(chemicalType, concentration);
	}
	
	public Map<ChemicalType, Double> getConcentrations() {
		return concentrations;
	}
	
	public void setConcentrations(Map<ChemicalType, Double> concentrations) {
		if(!isBlocked)
			this.concentrations = concentrations;
	}
	
	public boolean isBlocked() {
		return isBlocked;
	}
	
	public boolean isOpen() {
		return !isBlocked;
	}
	
	@Override
	public String toString() {
		List<String> stringFormatElements = new ArrayList<>();
		
		ChemicalType[] chemicalTypes = ChemicalType.values();
		for(ChemicalType chemicalType : chemicalTypes)
			stringFormatElements.add(String.format("%.2f", concentrations.get(chemicalType)));
		
		return "(" + String.join(", ", stringFormatElements) + ")";
	}
}

// Create a Gradient that contains concentrations (double type) for each ChemicalType object
//* “applyConcentration” method (pass in ChemicalType) - sets concentration of chemical type to 1.0, void
//* “getConcentration” method (pass in ChemicalType) - returns concentration
//* “toString” method - returns an (r, g, b) coordinate string for the different concentrations of each ChemicalType respectively
//* All chemicals getter
//* All chemicals setter (pass in map of ChemicalType to concentration)
