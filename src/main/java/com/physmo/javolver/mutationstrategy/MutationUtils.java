package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

class MutationUtils {
	/**
	 * Return an random integer index into the individuals
	 * dna structure.
	 * @param individual	The individual
	 * @return		Random number between 0 and DNA size.
	 */
	public static int getRandomDnaIndexForIindividual(Individual individual) {
		return (int)((double)individual.dna.getSize()*Math.random());
	}
	
	/**
	 * Swap two DNA elements at random.
	 * NOTE: seems quite destructive so it's been limited to only
	 * run 10% of the time for now.
	 * @param individual	The Individual
	 * @param count	Number of swaps to perform.
	 */
	public static void randomSwap(Individual individual, int count) {
		if (Math.random()<0.9) return; // heavily limit this.
		for (int i=0;i<count;i++) {
			int index1 = getRandomDnaIndexForIindividual(individual);
			int index2 = getRandomDnaIndexForIindividual(individual);
			individual.dna.swap(index1, index2);
		}
	}
}
