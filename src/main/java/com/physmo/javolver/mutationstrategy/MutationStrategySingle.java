package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public class MutationStrategySingle implements MutationStrategy {

    private final double amount;

    public MutationStrategySingle(double amount) {
        this.amount = amount;
    }

    @Override
    public void mutate(Individual individual, double temperature) {
        double jiggle, value;
        int index;

        for (int i = 0; i < 1; i++) {
            index = MutationUtils.getRandomDnaIndexForIndividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0 * temperature;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
