package com.physmo.javolver.examples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;

/*
 * GeneWord - A very simple example where the goal is to match the output to
 * a supplied string.
 * This is the meta solver version that uses a gene that attempts to optimise
 * the parameters to another javolver instance.
 */
public class WordFindMetaSolver {

    static int populationSize = 25;
    static String targetWord = "ABCDEFGHIJK";

    public static void main(String[] args) {


        Javolver javolver = new Javolver(new GeneWordMetaSolver(), populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.4, 1.5))
                .setSelectionStrategy(new SelectionStrategyTournament(0.65))
                .setBreedingStrategy(new BreedingStrategyUniform());

        // Perform a few iterations of evolution.
        for (int j = 0; j < 500; j++) {

            // Call the evolver class to perform one evolution step.
            javolver.doOneCycle();

            //GeneWord gene = (GeneWord)javolver.findBestScoringIndividual(javolver.getPool());
            GeneWordMetaSolver gene = (GeneWordMetaSolver)javolver.findBestScoringIndividual(null);

            // Print output every so often.
            System.out.println("Iteration " + j + "  " + javolver.report() + "  score:" + javolver.getBestScore());
            System.out.println("["+gene.toString()+"]");
            if (gene.toString().trim().equals(targetWord)) break;
        }

        System.out.print("END ");
    }

}
