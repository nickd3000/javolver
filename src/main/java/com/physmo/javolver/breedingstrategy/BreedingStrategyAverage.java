package com.physmo.javolver.breedingstrategy;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;


/**
 * A breed function that returns the average of the parents - experimental.
 * Elements are taken as the average of both parents.
 * This may have no practical use.
 */
public class BreedingStrategyAverage implements BreedingStrategy {

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        ArrayList<Individual> childList = new ArrayList<>();
        Individual child = parent1.clone();
        int dnaSize = parent1.dna.getData().length;
        double d1, d2;
        for (int i = 0; i < dnaSize; i++) {
            d1 = parent1.dna.getDouble(i);
            d2 = parent2.dna.getDouble(i);
            child.dna.getData()[i] = (d1 + d2) / 2.0;
        }

        childList.add(child);
        return childList;
    }

}
