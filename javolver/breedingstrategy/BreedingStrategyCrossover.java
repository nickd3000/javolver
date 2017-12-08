package javolver.breedingstrategy;

import java.util.ArrayList;
import java.util.List;

import javolver.Individual;



/* Select a pair of genes, mate them and return the new child.
* Using crossover method, there is one crossover point.
* */
// Create two children, each get half of each parents DNA with one crossover point.
public class BreedingStrategyCrossover implements BreedingStrategy {

	@Override
	public List<Individual> breed(Individual parent1, Individual parent2) {
		List<Individual> returnList = new ArrayList<>();
		
		Individual child1 = parent1.clone(); //proto.clone();
		Individual child2 = parent1.clone(); //proto.clone();
		int dnaSize = parent1.dna.getData().size();
		double d1=0,d2=0;
				
		int crossover=(int)(Math.random()*(double)dnaSize);
		
		for (int i=0;i<dnaSize;i++)
		{
			d1 = parent1.dna.getDouble(i);
			d2 = parent2.dna.getDouble(i);
			
			if (i<crossover) {
				child1.dna.getData().set(i,d1);
				child2.dna.getData().set(i,d2);
			}
			else {
				child1.dna.getData().set(i,d2);
				child2.dna.getData().set(i,d1);
			}

		}
		returnList.add(child1);
		returnList.add(child2);
		return returnList;
	}

}
