package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

// Gene based mutation uses values from the genome to control
// the frequency and amount of mutation.
public class MutationStrategyGeneBased implements MutationStrategy {

    private double amount;
    private double frequency;

    private int geneAmount = 0;
    private int geneFrequency = 0;


    public MutationStrategyGeneBased(int geneFrequency, int geneAmount) {
        this.geneAmount = geneFrequency;
        this.geneFrequency = geneFrequency;
    }

    @Override
    public void mutate(Individual individual) {

        amount = individual.dna.getDouble(geneAmount);
        frequency = individual.dna.getDouble(geneFrequency);

        if (amount < 0.001) amount = 0.001;
        if (frequency < 0.001) frequency = 0.001;

        amount = 0.001;
        frequency = 0.001;

        double jiggle = 0, value = 0;
        int index = 0;
        int dnaSize = individual.dna.getSize();
        int randomisedCount = (int) (Math.random() * frequency * (double) dnaSize);//+1;


        for (int i = 0; i < randomisedCount; i++) {
            index = MutationUtils.getRandomDnaIndexForIndividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
