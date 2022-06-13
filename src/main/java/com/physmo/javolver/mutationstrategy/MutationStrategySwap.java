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
    public void mutate(Individual individual) {
        if (Math.random() > chance) return;
        for (int i = 0; i < count; i++) {
            int index1 = MutationUtils.getRandomDnaIndexForIindividual(individual);
            int index2 = MutationUtils.getRandomDnaIndexForIindividual(individual);
            individual.dna.swap(index1, index2);
        }
    }

}
