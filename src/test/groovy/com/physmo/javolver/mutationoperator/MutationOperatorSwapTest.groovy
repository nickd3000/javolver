package com.physmo.javolver.mutationoperator


import com.physmo.javolver.Individual
import spock.lang.Specification

class MutationOperatorSwapTest extends Specification {
    def "d"() {

        given: ""
        MutationOperatorSwap mutationOperatorSwap = new MutationOperatorSwap(0.5, 1)


        when: ""
        double d = averageDifferences(10000, 1, mutationOperatorSwap)

        then:
        print(d)
        d > 0

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
