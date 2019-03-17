package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public class MutationStrategySingle implements MutationStrategy {

	double amount;
	
	public MutationStrategySingle(double amount) {
		this.amount = amount;
	}
	
	@Override
	public void mutate(Individual individual) {
		double jiggle = 0, value = 0;
		int index = 0;
		
		for (int i=0;i<1;i++) {
			index = MutationUtils.getRandomDnaIndexForIindividual(individual);
			jiggle = (Math.random()-0.5) * amount * 2.0;
			value = individual.dna.getDouble(index);
			individual.dna.set(index, value+jiggle);
		}
	}

}
