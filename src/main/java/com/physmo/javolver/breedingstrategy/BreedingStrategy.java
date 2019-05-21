package com.physmo.javolver.breedingstrategy;

import java.util.List;

import com.physmo.javolver.Individual;

// TODO: describe this interface
public interface BreedingStrategy {
	List<Individual> breed(Individual parent1, Individual parent2);
}
