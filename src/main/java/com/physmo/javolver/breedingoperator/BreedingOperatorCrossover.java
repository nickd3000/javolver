package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// Create two children, each get half of each parent's DNA with one crossover point.
public class BreedingOperatorCrossover implements BreedingOperator {

    Random random = new Random();

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        List<Individual> returnList = new ArrayList<>();

        Individual child1 = new Individual(parent1);
        Individual child2 = new Individual(parent1);
        double[] child1Data = child1.getDna().getData();
        double[] child2Data = child2.getDna().getData();
        int dnaSize = parent1.dna.getData().length;
        double d1, d2;

        int crossoverPoint = random.nextInt(dnaSize);
        if (crossoverPoint==0) crossoverPoint++;
        if (crossoverPoint>=dnaSize) crossoverPoint--;

        for (int i = 0; i < dnaSize; i++) {
            d1 = parent1.dna.getDouble(i);
            d2 = parent2.dna.getDouble(i);

            if (i < crossoverPoint) {
                child1Data[i] = d1;
                child2Data[i] = d2;
            } else {
                child1Data[i] = d2;
                child2Data[i] = d1;
            }

        }
        returnList.add(child1);
        returnList.add(child2);
        return returnList;
    }

}
