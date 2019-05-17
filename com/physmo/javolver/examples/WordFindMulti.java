package com.physmo.javolver.examples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;

/*
 * GeneWord - A very simple example where the goal is to match the output to
 * a supplied string.
 * This version runs the simulation multiple times to get an average.
 */
public class WordFindMulti {

    static int populationSize = 100;
    static String targetWord = "ABCDEFGHIJK";

    public static void main(String[] args) {

        int numCycles = 1000;
        int sumOfEvolutionCycles=0;

        Javolver javolver = null;

        for (int i=0;i<numCycles;i++) {
            javolver = new Javolver(new GeneWord(targetWord), populationSize)
                    .keepBestIndividualAlive(false)
                    //.enableCompatability(0.05,0.9)
                    .parallelScoring(false)
                    .addMutationStrategy(new MutationStrategySimple(0.1, 0.055))
                    .setSelectionStrategy(new SelectionStrategyTournament(0.05))
                    .setBreedingStrategy(new BreedingStrategyUniform());

            sumOfEvolutionCycles += evolveToSolution(javolver);
        }

        double averageCycled = (double)sumOfEvolutionCycles/(double)numCycles;

        System.out.print("Average = "+averageCycled);
    }

    public static int evolveToSolution(Javolver javolver) {
        int maxIterations = 500;
        for (int j = 0; j < 500; j++) {
            javolver.doOneCycle();
            GeneWord gene = (GeneWord)javolver.findBestScoringIndividual(javolver.getPool());
            if (gene.toString().trim().equals(targetWord)) return j;
        }
        return maxIterations;
    }


}
