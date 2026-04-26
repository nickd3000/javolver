package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

import java.util.Random;

/**
 * A mutation operator that performs swap mutations on an individual's DNA to introduce genetic diversity.
 * This operator randomly selects pairs of indices in an individual's DNA and swaps their values.
 * The number of swaps and the probability of mutation are configurable parameters.
 * <bR>
 * This mutation operator is typically used in evolutionary algorithms to shuffle genetic information
 * while maintaining the structure of the individual's DNA.
 */
public class MutationOperatorSwap implements MutationOperator {

    // Number of swaps to perform.
    private final double chance;
    private final int count;
    private final Random random = new Random();

    /**
     * Constructs a MutationOperatorSwap instance to perform swap-based mutations.
     *
     * @param chance The probability (0.0 to 1.0) that the mutation operation will be applied to an individual.
     * @param count  The maximum number of swap operations to perform during mutation.
     */
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
