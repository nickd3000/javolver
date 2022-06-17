package com.physmo.javolver.breedingstrategy;

import com.physmo.javolver.Individual;

import java.util.List;

// TODO: describe this interface
public interface BreedingStrategy {
    List<Individual> breed(Individual parent1, Individual parent2);
}
