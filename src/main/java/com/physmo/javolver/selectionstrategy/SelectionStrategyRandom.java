package com.physmo.javolver.selectionstrategy;

import com.physmo.javolver.Individual;

import java.util.List;
import java.util.Random;

/*
	Note, this selection strategy has no real use except in testing.
 */
public class SelectionStrategyRandom implements SelectionStrategy {
    private final Random random = new Random();

    @Override
    public Individual select(List<Individual> pool) {
        int numIndividuals = pool.size();

        int randomOne = random.nextInt(numIndividuals);

        return pool.get(randomOne);

    }

}
