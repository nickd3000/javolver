package javolver;


/**
 * @author nick
 *	Settings structure that can be passed around the helper functions.
 */
// TODO: consider converting to a fluid interface...
public class JavolverConfig {
	
	/**
	 * Keep the best individual alive between generations.
	 */
	public boolean keepBestIndividualAlive = false;

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

	
	// Mutation settings.
	//public double mutationAmount = 0.10;
	//public int mutationCount = 2;
	//public boolean allowSwapMutation = false;

	/**
	 * Use multi-threading for the scoring process.
	 * Set this to true if your {@link Individual#calculateScore()} method
	 * is expensive to run and may benefit from parallelization.
	 */
	public boolean parallelScoring = false;
	
	public JavolverConfig SetKeepBestIndividualAlive(boolean val) {
		keepBestIndividualAlive  = val;
		return this;
	}

	
	/**
	 * Used in tournament selection.  
	 * The percentage of the pool to include in the tournament. (1.0 = 100%)
	 */
	public JavolverConfig SetSelectionRange(double val) {
		selectionRange  = val;
		return this;
	}
	
	/**
	 * Use the individuals rank instead of the score during selection.
	 */
	public JavolverConfig SetSelectionUseScoreRank(boolean val) {
		selectionUseScoreRank   = val;
		return this;
	}
	
	/**
	 * Use the individuals diversity rank during selection.
	 * selectionUseScoreRank must also be true for this to have an effect.
	 */
	public JavolverConfig SetSelectionUseDiversityRank(boolean val) {
		selectionUseDiversityRank   = val;
		return this;
	}
	

//	// Mutation settings.
//	public JavolverConfig SetMutationAmount(double val) {
//		mutationAmount = val;
//		return this;
//	}
//
//	public JavolverConfig SetMutationCount(int val) {
//		mutationCount = val;
//		return this;
//	}
//
//	public JavolverConfig SetAllowSwapMutation (boolean val) {
//		allowSwapMutation = val;
//		return this;
//	}

	/**
	 * Use multi-threading for the scoring process.
	 * Set this to true if your {@link Individual#calculateScore()} method
	 * is expensive to run and may benefit from parallelization.
	 */
	public JavolverConfig SetParallelScoring  (boolean val) {
		parallelScoring = val;
		return this;
	}

	
}
