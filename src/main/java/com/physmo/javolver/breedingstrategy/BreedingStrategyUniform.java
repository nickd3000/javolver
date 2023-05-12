package com.physmo.javolver.breedingstrategy;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;

/***
 * Select a pair of genes, mate them and return the new child.
 * Using uniform method, each element is taken from a random parent.
 */
public class BreedingStrategyUniform implements BreedingStrategy {

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        ArrayList<Individual> childList = new ArrayList<>();
        Individual child = new Individual(parent1);
        int dnaSize = parent1.dna.getData().length;
        double d1 = 0, d2 = 0;

        for (int i = 0; i < dnaSize; i++) {
            d1 = parent1.dna.getDouble(i);
            d2 = parent2.dna.getDouble(i);

            if (Math.random() < 0.5)
                child.dna.getData()[i] = d1;
            else
                child.dna.getData()[i] = d2;

        }

        childList.add(child);
        return childList;
    }

}
