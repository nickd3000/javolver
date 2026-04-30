package com.physmo.javolver;

import org.junit.Assert;
import org.junit.Test;

public class TestIndividual {

    @Test
    public void testScoreCaching() {
        Individual individual = new Individual(5);
        final int[] scoreCallCount = {0};
        individual.setScoreFunction(ind -> {
            scoreCallCount[0]++;
            return 10.0;
        });

        Assert.assertFalse(individual.isProcessed());
        Assert.assertEquals(10.0, individual.getScore(), 0.001);
        Assert.assertTrue(individual.isProcessed());
        Assert.assertEquals(1, scoreCallCount[0]);

        // Second call should used cached value
        Assert.assertEquals(10.0, individual.getScore(), 0.001);
        Assert.assertEquals(1, scoreCallCount[0]);

        // Reset processed
        individual.setProcessed(false);
        Assert.assertFalse(individual.isProcessed());
        Assert.assertEquals(10.0, individual.getScore(), 0.001);
        Assert.assertEquals(2, scoreCallCount[0]);
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

    @Test
    public void testConstructorInitializesDnaWithCorrectSize() {
        int dnaSize = 10;
        Individual individual = new Individual(dnaSize);
        Assert.assertNotNull(individual.dna);
        Assert.assertEquals(dnaSize, individual.dna.getSize());
    }

    @Test
    public void testCopyConstructorResetsState() {
        Individual original = new Individual(5);
        original.setScoreFunction(ind -> 42.0);
        original.setScore(100.0);
        original.setProcessed(true);

        Individual clone = new Individual(original);

        Assert.assertEquals(original.getDna().getSize(), clone.getDna().getSize());
        Assert.assertEquals(original.getScoreFunction(), clone.getScoreFunction());
        Assert.assertFalse(clone.isProcessed());
        Assert.assertEquals(0.0, clone.score, 0.001);
        Assert.assertEquals(0.0, clone.diversity, 0.001);
    }

    @Test
    public void testGetScoreSquared() {
        Individual individual = new Individual(5);
        individual.setScoreFunction(ind -> 3.0);

        double scoreSquared = individual.getScoreSquared();

        Assert.assertEquals(9.0, scoreSquared, 0.001);
        Assert.assertTrue(individual.isProcessed());
    }

    @Test
    public void testCloneFully() {
        Individual original = new Individual(3);
        original.dna.set(0, 0.1);
        original.dna.set(1, 0.2);
        original.dna.set(2, 0.3);

        Individual clone = original.cloneFully();

        Assert.assertEquals(0.1, clone.dna.getDouble(0), 0.001);
        Assert.assertEquals(0.2, clone.dna.getDouble(1), 0.001);
        Assert.assertEquals(0.3, clone.dna.getDouble(2), 0.001);

        original.dna.set(0, 0.9);
        Assert.assertEquals(0.1, clone.dna.getDouble(0), 0.001);
    }
}
