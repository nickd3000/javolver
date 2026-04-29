package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;


/**
 * A breeding operator that produces an offspring by averaging the DNA of two parents.
 * <p>
 * This operator is experimental and may be more suitable for continuous optimization problems
 * where an intermediate value between parents might represent a better solution.
 */
public class BreedingOperatorAverage implements BreedingOperator {

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        List<Individual> childList = new ArrayList<>();
        Individual child = new Individual(parent1);
        int dnaSize = parent1.getDna().getSize();

        double[] parent1Data = parent1.getDna().getData();
        double[] parent2Data = parent2.getDna().getData();
        double[] childData = child.getDna().getData();

        for (int i = 0; i < dnaSize; i++) {
            childData[i] = (parent1Data[i] + parent2Data[i]) / 2.0;
        }

        childList.add(child);
        return childList;
    }

}
