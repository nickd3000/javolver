package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

import java.util.Random;

public class MutationOperatorSimple implements MutationOperator {

    private final int changeCount;
    private final double amount;
    private final double chance; // probability (0..1) that a change will be made.
    private final Random random = new Random();

    public MutationOperatorSimple(int changeCount, double amount) {
        this.changeCount = changeCount;
        this.amount = amount;
        this.chance=1.0;
    }

    public MutationOperatorSimple(int changeCount, double amount, double chance) {
        this.changeCount = changeCount;
        this.amount = amount;
        this.chance=chance;
    }

    @Override
    public void mutate(Individual individual, double temperature) {
        if (random.nextDouble()>chance) return;

        double jiggle, value;
        int index;

        int changes = random.nextInt(changeCount) + 1;

        for (int i = 0; i < changes; i++) {
            index = MutationUtils.getRandomDnaIndexForIndividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0 * temperature;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
