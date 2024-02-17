package com.physmo.javolver.mutationoperator

import com.physmo.javolver.Individual

class MutationHelpers {
    public static Individual createIndividual() {
        Individual individual = new Individual(20)
        def dna = individual.getDna().data
        for (int i = 0; i < dna.length; i++) {
            dna[i] = i / 10.0;
        }
        return individual
    }

    static int countDifferences(Individual i1, Individual i2) {
        int count = 0;
        for (int i = 0; i < i1.dna.data.length; i++) {
            if (i1.dna.data[i] != i2.dna.data[i]) count++;
        }
        return count;
    }
}
