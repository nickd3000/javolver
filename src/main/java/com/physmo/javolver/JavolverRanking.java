package com.physmo.javolver;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author nick
 * Functionality to rank each individual in a pool by both score and
 * by deviation from the average individual.
 */
public class JavolverRanking {


    public static void calculateFitnessRank(ArrayList<Individual> pool) {

        int poolSize = pool.size();
        if (pool==null || poolSize==0) {
            throw new InvalidParameterException("Pool is empty.");
        }

        // Sort the pool according to fitness.
        pool.sort((ind1, ind2) -> {
            double score1 = ind1.getScore();
            double score2 = ind2.getScore();
            double diff = score1 - score2;
            if (diff == 0.0) return 0;
            if (diff < 0.0) return -1;
            else return 1;
        });

        int count = 1;
        for (Individual i : pool) {
            i.setRankScore(count++);
            //System.out.println("Ranking: " + count + " fitness:" + i.getScore());
        }
    }

    public static void calculateDiversityRank(ArrayList<Individual> pool) {
        if (pool==null || pool.size()==0) {
            throw new InvalidParameterException("Pool is empty.");
        }

        // Calculate the average chromosome from the pool.
        Chromosome averageChromosome = CalculateAverageChromosome(pool);
        // Calculate the diversity of each individual in the pool.
        calculateDiversityForAllIndividuals(pool, averageChromosome);

        // Sort the pool according to fitness.
        pool.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual ind1, Individual ind2) {
                double diff = ind1.getDiversity() - ind2.getDiversity();
                if (diff == 0.0) return 0;
                if (diff < 0.0) return -1;
                else return 1;
            }
        });

        int count = 1;
        for (Individual i : pool) {
            i.setRankDiversity(count++);
        }
    }

    private static void calculateDiversityForAllIndividuals(ArrayList<Individual> pool, Chromosome averageChromosome) {

        double deviation = 0.0;

        for (Individual i : pool) {
            deviation = getDeviation(averageChromosome, i);
            i.diversity = deviation;
        }

    }

    /**
     * Create a chromosome that represents the average chromosome of the pool.
     *
     * @param pool The pool of individuals we want to calculate from.
     * @return Chromosome containing average of whole pool.
     */
    private static Chromosome CalculateAverageChromosome(ArrayList<Individual> pool) {
        int numElements = pool.get(0).dna.getData().length;
        Chromosome average = new Chromosome();
        average.init(numElements);

        // Clear average structure.
        for (int i = 0; i < average.data.length; i++) {
            average.set(i, 0.0);
        }

        for (Individual i : pool) {
            for (int j = 0; j < numElements; j++) {
                double val = i.dna.getData()[j] / (double) numElements;
                val = val + average.data[j];
                average.data[j] = val;
            }
        }

        return average;
    }

    private static double getDeviation(Chromosome average, Individual ind) {
        int numElements = average.getData().length;
        int numIndElements = ind.dna.getData().length;
        int count = 0;
        double totalDiff = 0;
        for (int i = 0; i < numElements; i++) {
            if (i > numIndElements) continue;
            totalDiff = Math.abs(average.getDouble(i) - ind.dna.getDouble(i));
            count++;
        }

        if (count == 0) return 0;

        return totalDiff / (double) count;
    }


}
