package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;
import org.junit.Assert;
import org.junit.Test;

public class TestMutationOperatorSwap {
    @Test
    public void testMutationOperatorShouldNotChangeIndividualWhenChanceIsZero() {
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(0.0, 1);
        Individual individual = MutationHelpers.createIndividual();
        Individual individualBaseline = individual.cloneFully();

        mutationOperatorSwap.mutate(individual, 1.0);

        Assert.assertEquals(0, MutationHelpers.countDifferences(individual, individualBaseline));
    }

    @Test
    public void testMutationOperatorShouldChangeIndividualWhenChanceIs100Percent() {
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(1.0, 1);
        Individual individual = MutationHelpers.createIndividual();
        Individual individualBaseline = individual.cloneFully();

        mutationOperatorSwap.mutate(individual, 1.0);

        Assert.assertTrue(MutationHelpers.countDifferences(individual, individualBaseline) > 0);
    }

    @Test
    public void testMutationShouldRespectTheMaximumSwapCount() {
        int maxSwaps = 5;
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(1.0, maxSwaps);

        int numRuns = 1000;
        boolean countInRange = true;
        for (int i = 0; i < numRuns; i++) {
            Individual individual = MutationHelpers.createIndividual();
            Individual individualBaseline = individual.cloneFully();
            mutationOperatorSwap.mutate(individual, 1.0);
            int diffs = MutationHelpers.countDifferences(individual, individualBaseline);
            // Each swap changes 2 positions (if indices are different, which mutate() checks)
            // So diffs should be at most maxSwaps * 2
            if (diffs > maxSwaps * 2) countInRange = false;
        }

        Assert.assertTrue(countInRange);
    }

    @Test
    public void testAverageDifferencesShouldBePositiveForNonZeroChance() {
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(0.5, 1);

        double avg = averageDifferences(1000, 1.0, mutationOperatorSwap);

        Assert.assertTrue(avg > 0);
    }

    private double averageDifferences(int numRuns, double temperature, MutationOperatorSwap mutationStrategySwap) {
        int totalDifferences = 0;

        for (int i = 0; i < numRuns; i++) {
            Individual individual = MutationHelpers.createIndividual();
            Individual individualBaseline = MutationHelpers.createIndividual();
            mutationStrategySwap.mutate(individual, 1);
            totalDifferences += MutationHelpers.countDifferences(individual, individualBaseline);
        }

        return totalDifferences / (double) numRuns;
    }
}
