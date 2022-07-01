package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

import java.util.Random;

public class MutationStrategySimple implements MutationStrategy {

    private final int changeCount;
    private final double amount;
    private Random random = new Random();

    public MutationStrategySimple(int changeCount, double amount) {
        this.changeCount = changeCount;
        this.amount = amount;
    }

    @Override
    public void mutate(Individual individual, double temperature) {
        double jiggle, value;
        int index;

        int changes = random.nextInt( changeCount);
        if (changes==0) changes=1;

        for (int i = 0; i < changes; i++) {
            index = MutationUtils.getRandomDnaIndexForIndividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0 * temperature;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
