package com.physmo.javolver;

import java.util.ArrayList;

public class Descent {

    // Method: create a number of clones, one for each DNA element,
    // mutate one dna element per clone and keep the best result into the next round.
    // Only one mutation per clone.
    public static void descent2(Javolver javolver,int poolSize, double mutationAmount) {
        ArrayList<Individual> pool = javolver.getPool();

        javolver.scoreGenes(null);

        Individual bestGuy = javolver.findBestScoringIndividual(pool);
        Individual child = bestGuy.clone();

        int dnaSize = pool.get(0).dna.getSize();

        ArrayList<Individual> newPool = new ArrayList<>();

        double bestScore = 0;
        Individual bestScoringIndividual=null;

        bestScore = bestGuy.getScore();

        // Create clone list and mutate specific bits..
        for (int i = 0; i < poolSize; i++) {
            int childId = (int)(dnaSize*Math.random());
            child = bestGuy.clone();

            // Copy each dna element and mutate the one specific to this clone.
            for (int j = 0; j < dnaSize; j++) {
                double dnaBit = bestGuy.dna.getDouble(j);
                if (childId==j) {
                    dnaBit+=(Math.random()-0.5)*mutationAmount;
                }
                child.dna.set(j, dnaBit);

            }

            // Score the new child and check if it's the new best one.
            double score = child.getScore();
            if (score>bestScore) {
                bestScore=score;
                bestScoringIndividual=child;
            }

            // Add new child to list so we don't lose track of it.
            newPool.add(child);
        }

        if (bestScoringIndividual!=null) {
            pool.set(0,bestScoringIndividual);
        }

    }

    // Same as 2 but every dna element is mutated.
    public static void descent3(Javolver javolver,int poolSize, double mutationAmount) {
        ArrayList<Individual> pool = javolver.getPool();

        javolver.scoreGenes(null);

        Individual bestGuy = javolver.findBestScoringIndividual(pool);
        Individual child = bestGuy.clone();

        int dnaSize = pool.get(0).dna.getSize();

        ArrayList<Individual> newPool = new ArrayList<>();
        double mutationChance = 0.2;
        double bestScore = 0;
        Individual bestScoringIndividual=null;

        bestScore = bestGuy.getScore();

        // Create clone list and mutate specific bits..
        for (int i = 0; i < poolSize; i++) {
            int childId = (int)(dnaSize*Math.random());
            child = bestGuy.clone();

            // Copy each dna element and mutate the one specific to this clone.
            for (int j = 0; j < dnaSize; j++) {
                double dnaBit = bestGuy.dna.getDouble(j);

                // Mutate every element of dna but not for the first clone.
                if (i>0 && Math.random()<mutationChance) {
                    dnaBit += (Math.random() - 0.5) * mutationAmount;
                }

                child.dna.set(j, dnaBit);

            }

            // Score the new child and check if it's the new best one.
            double score = child.getScore();
            if (score>bestScore) {
                bestScore=score;
                bestScoringIndividual=child;
            }

            // Add new child to list so we don't lose track of it.
            newPool.add(child);
        }

        if (bestScoringIndividual!=null) {
            pool.set(0,bestScoringIndividual);
        }

    }
}
