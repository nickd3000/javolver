package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

import java.util.Random;

public class MutationOperatorSwap implements MutationOperator {

    // Number of swaps to perform.
    private final double chance;
    private final int count;
    private final Random random = new Random();

    public MutationOperatorSwap(double chance, int count) {
        this.chance = chance;
        this.count = count;
    }

    @Override
    public void mutate(Individual individual, double temperature) {
        if (Math.random() > chance) return;

        int c = random.nextInt(count);
        if (c==0) c = 1;
        int modifiedCount=0;
        while (modifiedCount<c) {
            int index1 = MutationUtils.getRandomDnaIndexForIndividual(individual);
            int index2 = MutationUtils.getRandomDnaIndexForIndividual(individual);
            if (index1==index2) continue;
            individual.dna.swap(index1, index2);
            modifiedCount++;
        }
    }

}
