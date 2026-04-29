package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;

/**
 * A breeding operator that performs uniform crossover.
 * <p>
 * In uniform crossover, each gene in the offspring's DNA is randomly selected from one of the two parents
 * with equal probability (50%).
 */
public class BreedingOperatorUniform implements BreedingOperator {

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        List<Individual> childList = new ArrayList<>();
        Individual child = new Individual(parent1);
        int dnaSize = parent1.getDna().getSize();

        double[] parent1Data = parent1.getDna().getData();
        double[] parent2Data = parent2.getDna().getData();
        double[] childData = child.getDna().getData();

        for (int i = 0; i < dnaSize; i++) {
            if (Math.random() < 0.5) {
                childData[i] = parent1Data[i];
            } else {
                childData[i] = parent2Data[i];
            }
        }

        childList.add(child);
        return childList;
    }

}
