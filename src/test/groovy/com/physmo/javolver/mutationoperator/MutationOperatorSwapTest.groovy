package com.physmo.javolver.mutationoperator


import com.physmo.javolver.Individual
import spock.lang.Specification

class MutationOperatorSwapTest extends Specification {
    void "mutation operator should not change individual when chance is zero"() {
        given: "a mutation operator with zero chance"
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(0.0, 1)
        Individual individual = MutationHelpers.createIndividual()
        Individual individualBaseline = individual.cloneFully()

        when: "mutate is called"
        mutationOperatorSwap.mutate(individual, 1.0)

        then: "no differences are found"
        MutationHelpers.countDifferences(individual, individualBaseline) == 0
    }

    void "mutation operator should change individual when chance is 100 percent"() {
        given: "a mutation operator with 1.0 chance"
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(1.0, 1)
        Individual individual = MutationHelpers.createIndividual()
        Individual individualBaseline = individual.cloneFully()

        when: "mutate is called"
        mutationOperatorSwap.mutate(individual, 1.0)

        then: "differences are found"
        MutationHelpers.countDifferences(individual, individualBaseline) > 0
    }

    void "mutation should respect the maximum swap count"() {
        given: "a mutation operator with 1.0 chance and a specific count"
        int maxSwaps = 5
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(1.0, maxSwaps)

        when: "performing many mutations"
        int numRuns = 1000
        boolean countInRange = true
        for (int i = 0; i < numRuns; i++) {
            Individual individual = MutationHelpers.createIndividual()
            Individual individualBaseline = individual.cloneFully()
            mutationOperatorSwap.mutate(individual, 1.0)
            int diffs = MutationHelpers.countDifferences(individual, individualBaseline)
            // Each swap changes 2 positions (if indices are different, which mutate() checks)
            // So diffs should be at most maxSwaps * 2
            if (diffs > maxSwaps * 2) countInRange = false
        }

        then: "all mutation counts were within the expected range"
        countInRange
    }

    void "average differences should be positive for non-zero chance"() {
        given: "a mutation operator with 0.5 chance"
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(0.5, 1)

        when: "calculating average differences over many runs"
        double avg = averageDifferences(1000, 1.0, mutationOperatorSwap)

        then: "average difference is greater than 0"
        avg > 0
    }


    double averageDifferences(int numRuns, double temperature, MutationOperatorSwap mutationStrategySwap) {
        int totalDifferences = 0

        for (int i = 0; i < numRuns; i++) {
            Individual individual = MutationHelpers.createIndividual()
            Individual individualBaseline = MutationHelpers.createIndividual()
            mutationStrategySwap.mutate(individual, 1)
            totalDifferences += MutationHelpers.countDifferences(individual, individualBaseline)
        }

        return totalDifferences / (double) numRuns
    }

}
