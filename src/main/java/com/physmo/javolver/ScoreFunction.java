package com.physmo.javolver;

/**
 * A functional interface for a score function used to evaluate the fitness of an individual.
 * The score function takes an {@link Individual} as input and returns a double value
 * representing its fitness. Higher values typically indicate better fitness.
 */
@FunctionalInterface
public interface ScoreFunction {
    /**
     * Calculates the score (fitness) of the given individual.
     *
     * @param i The individual to evaluate.
     * @return The calculated score.
     */
    double score(Individual i);
}
