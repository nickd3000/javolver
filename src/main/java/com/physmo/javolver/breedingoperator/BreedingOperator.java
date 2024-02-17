package com.physmo.javolver.breedingoperator;

import com.physmo.javolver.Individual;

import java.util.List;


public interface BreedingOperator {
    List<Individual> breed(Individual parent1, Individual parent2);
}
