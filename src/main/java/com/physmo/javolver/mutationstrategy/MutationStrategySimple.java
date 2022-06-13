package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public class MutationStrategySimple implements MutationStrategy {

    private final int changeCount;
    private double amount;

    public MutationStrategySimple(int changeCount, double amount) {
        this.changeCount = changeCount;
        this.amount = amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void mutate(Individual individual) {
        double jiggle, value;
        int index;

        for (int i = 0; i < changeCount; i++) {
            index = MutationUtils.getRandomDnaIndexForIindividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
