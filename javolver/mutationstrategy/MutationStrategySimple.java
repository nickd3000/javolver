package javolver.mutationstrategy;

import javolver.Individual;

public class MutationStrategySimple implements MutationStrategy {

	double amount;
	int count;
	
	public MutationStrategySimple(int count, double amount) {
		this.count = count;
		this.amount = amount;
	}
	
	@Override
	public void mutate(Individual individual) {
		double jiggle = 0, value = 0;
		int index = 0;
		int randomisedCount = (int)(Math.random()*(double)count)+1;
		for (int i=0;i<randomisedCount;i++) {
			index = MutationUtils.getRandomDnaIndexForIindividual(individual);
			jiggle = (Math.random()-0.5) * amount * 2.0;
			value = individual.dna.getDouble(index);
			individual.dna.set(index, value+jiggle);
		}
	}

}
