package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

/**
 * A gene-based mutation operator that uses specific genes within an individual's genome to control
 * the frequency and magnitude of mutations. The mutation process is modulated by two parameters:
 * one that determines the frequency of mutations and another that specifies the mutation magnitude.
 * These parameters are extracted from the individual's DNA at the given gene indices.
 *
 * This operator ensures a dynamic and adaptable mutation process where the mutation behavior is
 * influenced by the individual's genetic composition. The mutation is further adjusted by an external
 * temperature parameter, which scales the mutation intensity.
 *
 * The mutation process selects random DNA segments for modification, and their values are adjusted
 * based on the calculated jiggle (a randomized factor influenced by the mutation amount, frequency,
 * and temperature).
 */
public class MutationOperatorGeneBased implements MutationOperator {

    private final int geneIndexAmount;
    private final int geneIndexFrequency;


    public MutationOperatorGeneBased(int geneIndexFrequency, int geneIndexAmount) {
        this.geneIndexAmount = geneIndexAmount;
        this.geneIndexFrequency = geneIndexFrequency;
    }

    @Override
    public void mutate(Individual individual, double temperature) {

        double amount = individual.dna.getDouble(geneIndexAmount);
        double frequency = individual.dna.getDouble(geneIndexFrequency);

        if (amount < 0.001) amount = 0.001;
        if (frequency < 0.001) frequency = 0.001;

//        amount = 0.001;
//        frequency = 0.001;

        double jiggle = 0, value = 0;
        int index = 0;
        int dnaSize = individual.dna.getSize();
        int randomisedCount = (int) (Math.random() * frequency * (double) dnaSize);//+1;


        for (int i = 0; i < randomisedCount; i++) {
            index = MutationUtils.getRandomDnaIndexForIndividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0 * temperature;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
