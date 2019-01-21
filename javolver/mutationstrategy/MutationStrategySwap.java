package javolver.mutationstrategy;

import javolver.Individual;

public class MutationStrategySwap implements MutationStrategy {

	// Number of swaps to perform.
	double chance;
	int count;
	
	public MutationStrategySwap(double chance, int count) {
		this.chance = chance;
		this.count = (int)(count*Math.random());
	}
	
	@Override
	public void mutate(Individual individual) {
		if (Math.random()>chance) return; // heavily limit this.
		for (int i=0;i<count;i++) {
			int index1 = MutationUtils.getRandomDnaIndexForIindividual(individual);
			int index2 = MutationUtils.getRandomDnaIndexForIindividual(individual);
			individual.dna.swap(index1, index2);
		}
	}

}
