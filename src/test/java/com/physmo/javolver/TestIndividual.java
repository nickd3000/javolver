package com.physmo.javolver;

import org.junit.Assert;
import org.junit.Test;

public class TestIndividual {

    @Test
    public void t1() {
        Individual individual1 = new Individual(2);
        individual1.getDna().set(0, 0);
        individual1.getDna().set(1, 1);
        individual1.setProcessed(false);
    }

    @Test
    public void testGetScore() {
        Individual individual = createTestIndividual();
        Assert.assertEquals(5, individual.getScore(), 0.01);
    }

    public Individual createTestIndividual() {
        int dnaSize = 10;
        Individual individual = new Individual(10);
        for (int i = 0; i < dnaSize; i++) {
            individual.getDna().set(i, i);
        }
        individual.setScoreFunction(this::scoreFunction);
        return individual;
    }

    public double scoreFunction(Individual individual) {
        return individual.getDna().getDouble(5);
    }

    @Test
    public void testClone() {
        Individual individual = createTestIndividual();
        Individual clone = new Individual(individual);
        Individual fullClone = individual.cloneFully();

        double score1 = clone.getScore();
        double score2 = fullClone.getScore();

        Assert.assertNotEquals(5.0, score1);
        Assert.assertEquals(5.0, score2, 0.001);

    }

    @Test
    public void testDifference() {
        Individual individual1 = createTestIndividual();
        Individual individual2 = createTestIndividual();
        double[] data = individual2.getDna().getData();
        data[0] = 5.0;
        double difference = individual1.getDifference(individual2);
        Assert.assertEquals(0.5, difference, 0.001);
    }

    @Test
    public void testHash() {
        Individual individual1 = createTestIndividual();
        Individual individual2 = createTestIndividual();
        double[] data = individual2.getDna().getData();
        data[0] = 5.0;
        int hash1 = individual1.getHash();
        int hash2 = individual2.getHash();

        Assert.assertNotEquals(hash1,hash2);
    }

}
