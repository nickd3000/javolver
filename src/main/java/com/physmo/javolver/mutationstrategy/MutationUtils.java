package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public class MutationUtils {
	/**
	 * Return an random integer index into the individuals
	 * dna structure.
	 * @param ind	The individual
	 * @return		Random number between 0 and DNA size.
	 */
	public static int getRandomDnaIndexForIindividual(Individual ind) {
		//return (int)((double)ind.dna.data.size()*Math.random());
		return (int)((double)ind.dna.getSize()*Math.random());
	}
	
	/**
	 * Swap two DNA elements at random.
	 * NOTE: seems quite destructive so it's been limited to only
	 * run 10% of the time for now.
	 * @param ind	The Individual
	 * @param count	Number of swaps to perform.
	 */
	public static void randomSwap(Individual ind, int count) {
		if (Math.random()<0.9) return; // heavily limit this.
		for (int i=0;i<count;i++) {
			int index1 = getRandomDnaIndexForIindividual(ind);
			int index2 = getRandomDnaIndexForIindividual(ind);
			ind.dna.swap(index1, index2);
		}
	}
}
