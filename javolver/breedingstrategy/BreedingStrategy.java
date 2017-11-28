package javolver.breedingstrategy;

import java.util.List;

import javolver.Individual;
import javolver.JavolverConfig;

public interface BreedingStrategy {
	public List<Individual> breed(JavolverConfig config, Individual parent1, Individual parent2); 
}
