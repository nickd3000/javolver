package javolver;

import javolver.JavolverBreed.BreedMethod;
import javolver.JavolverSelection.SelectionType;

public class JavolverConfig {
	
	public boolean keepBestIndividualAlive = false;
	
	// Selection settings.
	public SelectionType selectionType = SelectionType.TOURNAMENT;
	public double selectionRange = 0.2;
	public boolean selectionUseScoreRank = false;
	public boolean selectionUseDiversityRank = false;
	
	
	// Breed settings.
	public BreedMethod breedMethod = BreedMethod.UNIFORM;
	
	
	// Mutation settings.
	public double mutationAmount = 0.10;
	public int mutationCount = 2;
	public boolean allowSwapMutation = false;
	
	
	
}
