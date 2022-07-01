package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public class MutationStrategySwap implements MutationStrategy {

    // Number of swaps to perform.
    private final double chance;
    private final int count;

    public MutationStrategySwap(double chance, int count) {
        this.chance = chance;
        this.count = count;
    }

    @Override
    public void mutate(Individual individual, double temperature) {
        if (Math.random() > chance) return;
        int c = ((int) (Math.random() * 12345) % count);
        for (int i = 0; i < c; i++) {
            int index1 = MutationUtils.getRandomDnaIndexForIndividual(individual);
            int index2 = MutationUtils.getRandomDnaIndexForIndividual(individual);
            individual.dna.swap(index1, index2);
        }
    }

}
