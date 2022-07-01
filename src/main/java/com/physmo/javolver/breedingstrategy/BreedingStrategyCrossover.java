package com.physmo.javolver.breedingstrategy;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;


// Create two children, each get half of each parent's DNA with one crossover point.
public class BreedingStrategyCrossover implements BreedingStrategy {

    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        List<Individual> returnList = new ArrayList<>();

        Individual child1 = new Individual(parent1); //proto.clone();
        Individual child2 = new Individual(parent1); //proto.clone();
        int dnaSize = parent1.dna.getData().length;
        double d1 = 0, d2 = 0;

        int crossoverPoint = (int) (Math.random() * (double) dnaSize);

        for (int i = 0; i < dnaSize; i++) {
            d1 = parent1.dna.getDouble(i);
            d2 = parent2.dna.getDouble(i);

            if (i < crossoverPoint) {
                child1.dna.getData()[i] = d1;
                child2.dna.getData()[i] = d2;
            } else {
                child1.dna.getData()[i] = d2;
                child2.dna.getData()[i] = d1;
            }

        }
        returnList.add(child1);
        returnList.add(child2);
        return returnList;
    }

}
