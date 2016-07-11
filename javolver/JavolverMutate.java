package javolver;

/**
 * @author nick
 *	Various mutation functionalities. 
 */
public class JavolverMutate {

	
	/**
	 * Top level mutate function that calls the required mutation 
	 * actions depending on the config structure.
	 * @param config	Program settings.
	 * @param ind		Individual to mutate.
	 */
	public static void mutate(JavolverConfig config, Individual ind) {
		
		mutate(ind,config.mutationCount,config.mutationAmount);
		
		// If swap mutations are allowed, call the swap function.	
		if (config.allowSwapMutation) {
			mutateSwap(ind, 1);
		}
	}		
			
	/**
	 * @param ind		The individual to mutate.
	 * @param count		Number of DNA elements to mutate. 
	 * @param amount	Amount to mutate by.
	 */
	private static void mutate(Individual ind, int count, double amount) {
		double jiggle = 0, value = 0;
		int index = 0;
		int randomisedCount = (int)(Math.random()*(double)count)+1;
		for (int i=0;i<randomisedCount;i++) {
			index = getRandomDnaIndexForIindividual(ind);
			jiggle = (Math.random()-0.5) * amount * 2.0;
			value = ind.dna.getDouble(index);
			ind.dna.set(index, value+jiggle);
		}
	}
	
	
	/**
	 * Return an random integer index into the individuals
	 * dna structure.
	 * @param ind	The individual
	 * @return		Random number between 0 and DNA size.
	 */
	public static int getRandomDnaIndexForIindividual(Individual ind) {
		return (int)((double)ind.dna.data.size()*Math.random());
	}
	
	
	/**
	 * Swap two DNA elements at random.
	 * NOTE: seems quite destructive so it's been limited to only
	 * run 10% of the time for now.
	 * @param ind	The Individual
	 * @param count	Number of swaps to perform.
	 */
	public static void mutateSwap(Individual ind, int count) {
		if (Math.random()<0.9) return; // heavily limit this.
		for (int i=0;i<count;i++) {
			int index1 = getRandomDnaIndexForIindividual(ind);
			int index2 = getRandomDnaIndexForIindividual(ind);
			ind.dna.swap(index1, index2);
		}
	}
	
	
}
