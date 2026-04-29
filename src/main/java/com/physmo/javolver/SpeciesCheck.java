package com.physmo.javolver;

/**
 * A functional interface used to determine if two individuals belong to the same species.
 * This can be used by the solver to maintain diversity by controlling breeding between
 * different species.
 */
@FunctionalInterface
public interface SpeciesCheck {
    /**
     * Checks if two individuals are of the same species.
     *
     * @param i1 The first individual.
     * @param i2 The second individual.
     * @return True if they are the same species, false otherwise.
     */
    boolean isSameSpecies(Individual i1, Individual i2);
}

