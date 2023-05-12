package com.physmo.javolver;

@FunctionalInterface
public interface SpeciesCheck {
    boolean isSameSpecies(Individual i1, Individual i2);
}

