package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

/**
 * An interface representing a mutation operator used to alter an individual's genetic information (DNA).
 * Mutation operators are typically used in evolutionary algorithms to introduce genetic diversity
 * by modifying the genetic data of individuals in a population.
 * <BR>
 * Implementations of this interface define specific mutation strategies, where mutations may vary
 * based on parameters such as mutation intensity, probability, temperature, or other contextual factors.
 * The mutation process may involve modifying the existing genetic information or reshuffling the structure
 * of the individual's DNA.
 */
public interface MutationOperator {
    /**
     * Applies a mutation to the provided individual, modifying its genetic information (DNA).
     * The specific mutation behavior may depend on the implementation of the mutation operator
     * and the provided temperature parameter, which can influence mutation intensity or behavior.
     *
     * @param individual  The individual whose DNA will be mutated. This object represents
     *                    the genetic solution subject to evolution.
     * @param temperature A parameter influencing the mutation process. For example, it can
     *                    control the variation range or likelihood of changes during mutation.
     */
    void mutate(Individual individual, double temperature);
}
