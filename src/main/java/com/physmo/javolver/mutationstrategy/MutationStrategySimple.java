package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public class MutationStrategySimple implements MutationStrategy {

    private final int changeCount;
    private double amount;

    public MutationStrategySimple(int changeCount, double amount) {
        this.changeCount = changeCount;
        this.amount = amount;
    }

    @Override
    public void mutate(Individual individual, double scaleChange) {
        double jiggle, value;
        int index;

        for (int i = 0; i < changeCount; i++) {
            if (i > 0 && Math.random() > 0.5) continue;
            index = MutationUtils.getRandomDnaIndexForIndividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0 * scaleChange;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
