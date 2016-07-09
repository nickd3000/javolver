package javolver;

public class JavolverMutate {

	
	public static void mutate(JavolverConfig config, Individual ind) {
	
		//mutate(child,mutationCount,mutationAmount);
		//if (allowSwapMutation) mutateSwap(child, 1);
		mutate(ind,config.mutationCount,config.mutationAmount);
	}		
			
	/**
	 * @param ind		The individual to mutate.
	 * @param count		Number of DNA elements to mutate. 
	 * @param amount	Amount to mutate by.
	 */
	private static void mutate(Individual ind, int count, double amount) {
		double jiggle = 0, value = 0;
		int index = 0;
		for (int i=0;i<count;i++) {
			index = getRandomDnaIndexForIindividual(ind);
			jiggle = (Math.random()-0.5) * amount * 2.0;
			value = ind.dna.getDouble(index);
			ind.dna.set(index, value+jiggle);
		}
	}
	
	
	public static int getRandomDnaIndexForIindividual(Individual ind) {
		return (int)((double)ind.dna.data.size()*Math.random());
	}
	
	
	//
	public static void mutateSwap(Individual ind, int count) {
		if (Math.random()<0.9) return; // heavily limit this.
		for (int i=0;i<count;i++) {
			int index1 = getRandomDnaIndexForIindividual(ind);
			int index2 = getRandomDnaIndexForIindividual(ind);
			ind.dna.swap(index1, index2);
		}
	}
	
	
}
