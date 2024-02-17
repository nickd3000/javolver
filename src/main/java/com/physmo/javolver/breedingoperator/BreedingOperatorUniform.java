package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;

/***
 * Select a pair of genes, mate them and return the new child.
 * Using uniform method, each element is taken from a random parent.
 */
public class BreedingOperatorUniform implements BreedingOperator {

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        ArrayList<Individual> childList = new ArrayList<>();
        Individual child = new Individual(parent1);
        int dnaSize = parent1.dna.getData().length;

        double[] parent1Data = parent1.getDna().getData();
        double[] parent2Data = parent1.getDna().getData();
        double[] childData = child.getDna().getData();

        for (int i = 0; i < dnaSize; i++) {

            if (Math.random() < 0.5)
                childData[i] = parent1Data[i];
            else
                childData[i] = parent2Data[i];

        }

        childList.add(child);
        return childList;
    }

}
