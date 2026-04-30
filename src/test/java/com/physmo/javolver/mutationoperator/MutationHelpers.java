package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

public class MutationHelpers {
    public static Individual createIndividual() {
        Individual individual = new Individual(20);
        double[] dna = individual.getDna().getData();
        for (int i = 0; i < dna.length; i++) {
            dna[i] = i / 10.0;
        }
        return individual;
    }

    public static int countDifferences(Individual i1, Individual i2) {
        int count = 0;
        double[] data1 = i1.getDna().getData();
        double[] data2 = i2.getDna().getData();
        for (int i = 0; i < data1.length; i++) {
            if (data1[i] != data2[i]) count++;
        }
        return count;
    }
}
