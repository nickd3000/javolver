package javolver;

import java.util.ArrayList;
import java.util.Comparator;

public class JavolverRanking {

	
	public static void calculateFitnessRank(ArrayList<Individual> pool) {
		
		// Sort the pool according to fitness.
		pool.sort( new Comparator<Individual>() {
	        @Override
	        public int compare(Individual ind1, Individual ind2)
	        {
	        	double diff = ind1.getScore() - ind2.getScore();
	            if (diff==0.0) return 0;
	            if (diff<0.0) return -1;
	            else return 1;
	        }
	    });
		
		int count = 1;
		for (Individual i : pool) {
			i.rankScore = count++;
			//System.out.println("Ranking: " + count + " fitness:" + i.getScore());
		}
	}
	
	public static void calculateDiversityRank(ArrayList<Individual> pool) {
		// Calculate the average chromosome from the pool.
		Chromosome averageChromosome = CalculateAverageChromosome(pool);
		// Calculate the diversity of each individual in the pool.
		calculateDiversityForAllIndividuals(pool, averageChromosome);
		
		// Sort the pool according to fitness.
		pool.sort( new Comparator<Individual>() {
	        @Override
	        public int compare(Individual ind1, Individual ind2)
	        {
	        	double diff = ind1.getDiversity() - ind2.getDiversity();
	            if (diff==0.0) return 0;
	            if (diff<0.0) return -1;
	            else return 1;
	        }
	    });
		
		int count = 1;
		for (Individual i : pool) {
			i.rankDiversity = count++;
		}
	}

	public static void calculateDiversityForAllIndividuals(ArrayList<Individual> pool, Chromosome averageChromosome) {
		
		double deviation = 0.0;
		
		for (Individual i : pool) {
			deviation = getDeviation(averageChromosome , i);
			i.diversity = deviation;
		}
		
	}
	
	/**
	 * Create a chromosome that represents the average chromosome of the pool.
	 * @param pool
	 * @return
	 */
	public static Chromosome CalculateAverageChromosome(ArrayList<Individual> pool) {
		int numElements = pool.get(0).dna.getData().size();
		Chromosome average = new Chromosome();
		average.init(numElements);
		
		// Clear average structure.
		for (int i=0;i<average.data.size();i++) {
			average.set(i, 0.0);
		}
		
		for (Individual i : pool) {
			for (int j=0;j<numElements;j++) {
				double val = i.dna.getData().get(j) / (double)numElements;
				val = val + average.data.get(j);
				average.data.set(j, val);
			}
		}
		
		return average;
	}
	
	public static double getDeviation(Chromosome average, Individual ind) {
		int numElements = average.getData().size();
		int numIndElements = ind.dna.getData().size();
		int count = 0;
		double totalDiff = 0;
		for (int i=0;i<numElements;i++) {
			if (i>numIndElements) continue;
			totalDiff = Math.abs(average.getDouble(i)-ind.dna.getDouble(i));
			count++;
		}
		
		if (count==0) return 0;
		
		return totalDiff/(double)count;
	}
	
	
}
