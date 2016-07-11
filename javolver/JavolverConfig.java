package javolver;

import javolver.JavolverBreed.BreedMethod;
import javolver.JavolverSelection.SelectionType;

/**
 * @author nick
 *	Settings structure that can be passed around the helper functions.
 */
public class JavolverConfig {
	
	/**
	 * Keep the best individual alive between generations.
	 */
	public boolean keepBestIndividualAlive = false;
	
	// Selection settings.
	public SelectionType selectionType = SelectionType.TOURNAMENT;
	
	/**
	 * Used in tournament selection.  
	 * The percentage of the pool to include in the tournament. (1.0 = 100%)
	 */
	public double selectionRange = 0.2;
	/**
	 * Use the individuals rank instead of the score during selection.
	 */
	public boolean selectionUseScoreRank = false;
	
	/**
	 * Use the individuals diversity rank during selection.
	 * selectionUseScoreRank must also be true for this to have an effect.
	 */
	public boolean selectionUseDiversityRank = false;
	
	
	// Breed settings.
	public BreedMethod breedMethod = BreedMethod.UNIFORM;
	
	
	// Mutation settings.
	public double mutationAmount = 0.10;
	public int mutationCount = 2;
	public boolean allowSwapMutation = false;
	
	
	
}
