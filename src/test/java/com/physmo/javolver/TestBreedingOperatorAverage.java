package com.physmo.javolver;

import com.physmo.javolver.breedingoperator.BreedingOperatorAverage;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestBreedingOperatorAverage {

    @Test
    public void testBreedingOperatorAverageBreeding() {
        // Given: Two individuals
        Individual i1 = new Individual(2);
        Individual i2 = new Individual(2);

        // And: The individuals are initialized to known values
        i1.getDna().init(10);
        setAllDnaValuesTo(i1, 0);
        i2.getDna().init(10);
        setAllDnaValuesTo(i2, 1);

        // And: BreedingOperatorAverage is created
        BreedingOperatorAverage breedingStrategyAverage = new BreedingOperatorAverage();

        // When: The two individuals are bred
        List<Individual> children = breedingStrategyAverage.breed(i1, i2);

        // Then: One individual is created
        Assert.assertEquals(1, children.size());

        // And: The dna value is the average of the two individuals
        Assert.assertTrue(Math.abs(children.get(0).getDna().getDouble(0) - 0.5) < 0.01);
    }

    private void setAllDnaValuesTo(Individual individual, double val) {
        for (int i = 0; i < individual.getDna().getSize(); i++) {
            individual.getDna().set(i, val);
        }
    }
}
