package javolver.breedingstrategy;

import java.util.ArrayList;
import java.util.List;

import javolver.Individual;

/***
 * Select a pair of genes, mate them and return the new child.
 * Using uniform method, each element is taken from a random parent.
 */
public class BreedingStrategyUniform implements BreedingStrategy {

	@Override
	public List<Individual> breed(Individual parent1, Individual parent2) {
		ArrayList<Individual> childList = new ArrayList<>(); 
		Individual child = parent1.clone();
		int dnaSize = parent1.dna.getData().size();
		double d1=0,d2=0;

		for (int i=0;i<dnaSize;i++)
		{
			d1 = parent1.dna.getDouble(i);
			d2 = parent2.dna.getDouble(i);
			
			if (Math.random()<0.5)
				child.dna.getData().set(i,d1);
			else
				child.dna.getData().set(i,d2);

		}
		
		childList.add(child);
		return childList;
	}

}
