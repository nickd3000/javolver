package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

import java.util.Random;

/**
 * A simple mutation operator that modifies an individual's DNA according to specified parameters
 * such as mutation intensity, the number of changes, and the probability of mutation.
 * This operator is commonly used in evolutionary algorithms to introduce variability
 * and create diversity in the genetic data of individuals.
 */
public class MutationOperatorSimple implements MutationOperator {

    private final int changeCount;
    private final double amount;
    private final double chance; // probability (0..1) that a change will be made.
    private final Random random = new Random();

    /**
     * Constructs a MutationOperatorSimple instance with specified mutation parameters.
     *
     * @param changeCount The maximum number of changes to be applied during a mutation. Determines
     *                    the upper limit for the number of DNA elements that can be modified in
     *                    a single mutation operation.
     * @param amount      The intensity of the mutation. Represents the maximum value by which
     *                    a DNA element can be altered, influencing the magnitude of the mutation.
     */
    public MutationOperatorSimple(int changeCount, double amount) {
        this.changeCount = changeCount;
        this.amount = amount;
        this.chance=1.0;
    }

    /**
     * Constructs a MutationOperatorSimple instance with specified mutation parameters.
     *
     * @param changeCount The maximum number of changes to be applied during a mutation. Determines
     *                    the upper limit for the number of DNA elements that can be modified in
     *                    a single mutation operation.
     * @param amount      The intensity of the mutation. Represents the magnitude of the mutation,
     *                    defining the maximum value by which a DNA element can be altered.
     * @param chance      The probability (0.0 to 1.0) that a mutation operation will occur.
     *                    A higher value increases the likelihood of mutation.
     */
    public MutationOperatorSimple(int changeCount, double amount, double chance) {
        this.changeCount = changeCount;
        this.amount = amount;
        this.chance=chance;
    }

    @Override
    public void mutate(Individual individual, double temperature) {
        if (random.nextDouble()>chance) return;

        double jiggle, value;
        int index;

        int changes = random.nextInt(changeCount) + 1;

        for (int i = 0; i < changes; i++) {
            index = MutationUtils.getRandomDnaIndexForIndividual(individual);
            jiggle = (Math.random() - 0.5) * amount * 2.0 * temperature;
            value = individual.dna.getDouble(index);
            individual.dna.set(index, value + jiggle);
        }
    }

}
