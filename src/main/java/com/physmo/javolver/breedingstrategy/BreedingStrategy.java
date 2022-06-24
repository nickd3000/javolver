package com.physmo.javolver.breedingstrategy;

import com.physmo.javolver.Individual;

import java.util.List;


public interface BreedingStrategy {
    List<Individual> breed(Individual parent1, Individual parent2);
}
