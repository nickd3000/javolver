package javolver.breedingstrategy;

import java.util.List;

import javolver.Individual;

public interface BreedingStrategy {
	public List<Individual> breed(Individual parent1, Individual parent2); 
}
