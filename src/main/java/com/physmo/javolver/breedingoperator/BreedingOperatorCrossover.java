package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A breeding operator that performs a single-point crossover to produce two children.
 * <p>
 * A random crossover point is selected. The first child receives DNA from the first parent before the point
 * and from the second parent after the point. The second child receives the complement.
 */
public class BreedingOperatorCrossover implements BreedingOperator {

    private final Random random = new Random();

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        List<Individual> returnList = new ArrayList<>();

        Individual child1 = new Individual(parent1);
        Individual child2 = new Individual(parent1);
        double[] child1Data = child1.getDna().getData();
        double[] child2Data = child2.getDna().getData();
        int dnaSize = parent1.getDna().getSize();

        double[] parent1Data = parent1.getDna().getData();
        double[] parent2Data = parent2.getDna().getData();

        int crossoverPoint = random.nextInt(dnaSize);

        for (int i = 0; i < dnaSize; i++) {
            if (i < crossoverPoint) {
                child1Data[i] = parent1Data[i];
                child2Data[i] = parent2Data[i];
            } else {
                child1Data[i] = parent2Data[i];
                child2Data[i] = parent1Data[i];
            }
        }

        returnList.add(child1);
        returnList.add(child2);
        return returnList;
    }

}
