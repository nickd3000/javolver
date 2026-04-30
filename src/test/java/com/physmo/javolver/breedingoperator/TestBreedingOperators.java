package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestBreedingOperators {

    @Test
    public void testBreedingOperatorCrossover() {
        int dnaSize = 10;
        Individual parent1 = new Individual(dnaSize);
        Individual parent2 = new Individual(dnaSize);
        
        for (int i = 0; i < dnaSize; i++) {
            parent1.getDna().set(i, 0.0);
            parent2.getDna().set(i, 1.0);
        }
        
        BreedingOperatorCrossover bop = new BreedingOperatorCrossover();
        List<Individual> children = bop.breed(parent1, parent2);
        
        Assert.assertEquals(2, children.size());
        
        Individual child1 = children.get(0);
        Individual child2 = children.get(1);
        
        // Check that children have some of parent1 and some of parent2
        // Because crossover point is random, it could be 0, but usually it's in between.
        // However, child1[i] + child2[i] should always be 1.0 (since parent1 is 0 and parent2 is 1)
        for (int i = 0; i < dnaSize; i++) {
            Assert.assertEquals(1.0, child1.getDna().getDouble(i) + child2.getDna().getDouble(i), 0.0001);
        }
    }

    @Test
    public void testBreedingOperatorAverage() {
        int dnaSize = 10;
        Individual parent1 = new Individual(dnaSize);
        Individual parent2 = new Individual(dnaSize);

        for (int i = 0; i < dnaSize; i++) {
            parent1.getDna().set(i, 0.0);
            parent2.getDna().set(i, 1.0);
        }

        BreedingOperatorAverage bop = new BreedingOperatorAverage();
        List<Individual> children = bop.breed(parent1, parent2);

        Assert.assertEquals(1, children.size());

        Individual child = children.get(0);

        for (int i = 0; i < dnaSize; i++) {
            Assert.assertEquals(0.5, child.getDna().getDouble(i), 0.0001);
        }
    }
}
