package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;
import java.util.List;

/**
 * BreedingOperator defines how two parent individuals are combined to produce offspring.
 * <p>
 * Implement this interface to create custom breeding (crossover) logic for genetic algorithms.
 */
public interface BreedingOperator {
    /**
     * Create one or more new individuals (children) from two parent individuals.
     *
     * @param parent1 The first parent individual.
     * @param parent2 The second parent individual.
     * @return A list of new individuals created by breeding the parents.
     */
    List<Individual> breed(Individual parent1, Individual parent2);
}