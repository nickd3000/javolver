package com.physmo.javolver.examples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;

/*
 * GeneWord - A very simple example where the goal is to match the output to
 * a supplied string.
 */
public class WordFind {

    static int populationSize = 100;
    static String targetWord = "ABCDEFGHIJK";

    public static void main(String[] args) {


        Javolver testEvolver = new Javolver(new GeneWord(targetWord), populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.1, 0.1))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setBreedingStrategy(new BreedingStrategyUniform());

        // Perform a few iterations of evolution.
        for (int j = 0; j < 50; j++) {

            // Call the evolver class to perform one evolution step.
            testEvolver.doOneCycle();

            // Print output every so often.
            System.out.println("Iteration " + j + "  " + testEvolver.report());
        }

        System.out.print("END ");
    }

}
