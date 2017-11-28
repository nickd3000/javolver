package javolver.breedingstrategy;

import java.util.ArrayList;
import java.util.List;

import javolver.Individual;
import javolver.JavolverConfig;

/**
 * A breed function that returns the average of the parents - experimental.
 * Elements are taken as the average of both parents.
 */
public class BreedingStrategyAverage implements BreedingStrategy {

	@Override
	public List<Individual> breed(JavolverConfig config, Individual parent1, Individual parent2) {
		ArrayList<Individual> childList = new ArrayList<>(); 
		Individual child = parent1.clone();//proto.clone();
		int dnaSize = parent1.dna.getData().size();
		double d1=0,d2=0;
		double blend = Math.random();
		for (int i=0;i<dnaSize;i++)
		{
			d1 = parent1.dna.getDouble(i);
			d2 = parent2.dna.getDouble(i);
			child.dna.getData().set(i,((d1*blend)+(d2*(1.0-blend))));
		}
		
		childList.add(child);
		return childList;
	}

}
