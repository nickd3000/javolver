package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public class MutationStrategySimple implements MutationStrategy {

	double amount;
	double frequency;
	
	public MutationStrategySimple(double frequency, double amount) {
		this.frequency = frequency;
		this.amount = amount;
	}
	
	@Override
	public void mutate(Individual individual) {
		double jiggle = 0, value = 0;
		int index = 0;
		int dnaSize = individual.dna.getSize();
		int randomisedCount = (int)(Math.random()*(double)frequency*(double)dnaSize);//+1;

		if (randomisedCount<1) randomisedCount=1;
		
		for (int i=0;i<randomisedCount;i++) {
			index = MutationUtils.getRandomDnaIndexForIindividual(individual);
			jiggle = (Math.random()-0.5) * amount * 2.0;
			value = individual.dna.getDouble(index);
			individual.dna.set(index, value+jiggle);
		}
	}

}
