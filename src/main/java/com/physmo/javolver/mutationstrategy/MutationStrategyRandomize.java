package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

// Fully randomise the individual.
public class MutationStrategyRandomize implements MutationStrategy {

    private final double probability;

    public MutationStrategyRandomize(double probability) {
        this.probability = probability;
    }

    @Override
    public void mutate(Individual individual, double scaleChange) {
        if (Math.random() >= probability) return;

        for (int i = 0; i < individual.dna.getSize(); i++) {
            individual.dna.set(i, Math.random()*scaleChange);
        }
    }

}
