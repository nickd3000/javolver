package javolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * @author nick
 *	Functionality to rank each individual in a pool by both score and 
 *	by deviation from the average individual.
 */
public class JavolverRanking {

	
	public static void calculateFitnessRank(ArrayList<Individual> pool) {
		// use a copy of pool, so that the calculation can be done safely in parallel
		pool = new ArrayList<>(pool);
		
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
		Chromosome averageChromosome = calculateAverageChromosome(pool);
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
		
		// parallel processing for the deviation will only be more efficient
		// for large population sizes. 2000 seems to be a good size to switch
		// to parallel processing
		if (pool.size() >= 2000) {
			// calculates the diversity for each Individual in the pool,
			// and attempts to do so in parallel threads
			pool.parallelStream().forEach(ind -> calculateDeviation(averageChromosome, ind));
		} else {
			for (Individual i : pool) {
				calculateDeviation(averageChromosome, i);
			}
		}
		
	}
	
	/**
	 * Create a chromosome that represents the average chromosome of the pool.
	 * @param pool	The pool of individuals we want to calculate from.
	 * @return		Chromosome containing average of whole pool.
	 */
	public static Chromosome calculateAverageChromosome(ArrayList<Individual> pool) {
		int numElements = pool.get(0).dna.getData().size();
		Chromosome average = new Chromosome();
		average.init(numElements);
		
		// Clear average structure.
		for (int i=0;i<average.data.size();i++) {
			average.set(i, 0.0);
		}
		
		// parallel calculation of averages will only be more efficient
		// for large population sizes. 2000 seems to be a good size to switch
		// to parallel processing
		if (pool.size() >= 2000) {
			// creates a new parallel stream of integers, in this case the indices
			// of the chromosome data list
			IntStream.range(0, numElements).parallel().forEach(j -> {
				// for each chromosome index
				double val = pool.parallelStream() // create a stream of the individuals
						.mapToDouble(ind -> ind.dna.getData().get(j)) // map it to a stream of their chromosome values
						.average() // average all the values
						.orElse(0.0); // if the stream did not contain any elements, use 0.0 as average
				average.data.set(j, val);
			});
		} else {
			for (Individual i : pool) {
				for (int j = 0; j < numElements; j++) {
					double val = i.dna.getData().get(j) / (double) numElements;
					val = val + average.data.get(j);
					average.data.set(j, val);
				}
			}
		}
		
		return average;
	}
	
	public static void calculateDeviation(Chromosome average, Individual ind) {
		int numElements = average.getData().size();
		int numIndElements = ind.dna.getData().size();
		int count = 0;
		double totalDiff = 0;
		for (int i=0;i<numElements;i++) {
			if (i>numIndElements) continue;
			totalDiff = Math.abs(average.getDouble(i)-ind.dna.getDouble(i));
			count++;
		}
		
		// ternary operation (?:) equivalent to:
		// if (count != 0) {
		//     ind.diversity = totalDiff/count;
		// } else {
		//     ind.diversity = 0;
		// }
		ind.diversity = count != 0 ? totalDiff/count : 0;
	}
	
	
}
