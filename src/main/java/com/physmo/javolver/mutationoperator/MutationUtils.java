package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

import java.util.Random;

/**
 * Utility class providing helper methods for mutation operations.
 */
class MutationUtils {
    private static final Random random = new Random();

    /**
     * Swaps two DNA elements at random.
     *
     * @param individual The individual to modify.
     * @param count      The number of swap operations to attempt.
     */
    public static void randomSwap(Individual individual, int count) {
        for (int i = 0; i < count; i++) {
            int index1 = getRandomDnaIndexForIndividual(individual);
            int index2 = getRandomDnaIndexForIndividual(individual);
            individual.dna.swap(index1, index2);
        }
    }

    /**
     * Return a random integer index into the individuals
     * dna structure.
     *
     * @param individual The individual
     * @return Random number between 0 and DNA size.
     */
    public static int getRandomDnaIndexForIndividual(Individual individual) {
        return random.nextInt(individual.dna.getSize());
    }
}
