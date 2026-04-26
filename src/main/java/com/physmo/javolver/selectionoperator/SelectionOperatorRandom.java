package com.physmo.javolver.selectionoperator;

import com.physmo.javolver.Individual;

import java.util.List;
import java.util.Random;

/*
	Note, this selection strategy has no real use except in testing.
 */
public class SelectionOperatorRandom implements SelectionOperator {
    private final Random random = new Random();

    @Override
    public Individual select(List<Individual> pool) {
        int numIndividuals = pool.size();

        int randomOne = random.nextInt(numIndividuals);

        return pool.get(randomOne);

    }

}
