package com.physmo.javolverexamples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRouletteRanked;

/*
 * com.physmo.javolverexamples.GeneWord - A very simple example where the goal is to match the output to
 * a supplied string.
 */
public class WordFind {

    static int populationSize = 100;
    static String targetWord = "ABCDEFGHIJK";

    public static void main(String[] args) {


        Javolver javolver = new Javolver(new GeneWord(targetWord), populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.01, 0.25))
                //.setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setSelectionStrategy(new SelectionStrategyRouletteRanked())
                .setBreedingStrategy(new BreedingStrategyUniform());

        // Run evolution until we get exact solution.
        for (int j = 0; j < 250; j++) {

            // Call the evolver class to perform one evolution step.
            javolver.doOneCycle();

            GeneWord gene = (GeneWord)javolver.findBestScoringIndividual();

            // Print output every so often.
            System.out.println("Iteration " + j + "  " + javolver.report() + "  score:" + javolver.getBestScore(null));
            System.out.println("["+gene.toString()+"]");

            // Check if we have arrived at the target string.
            if (gene.toString().trim().equals(targetWord)) break;
        }

        System.out.print("END");
    }

}
