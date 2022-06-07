package com.physmo.javolverexamples2;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRouletteRanked;

public class WordFinder {
    public static String targetWord = "EVOLUTION";

    public static void main(String[] args) {

        Javolver javolver = Javolver.builder()
                .dnaSize(9)
                .populationTargetSize(10)
                .keepBestIndividualAlive(true)
                .addMutationStrategy(new MutationStrategySimple(0.01, 0.25))
                .setSelectionStrategy(new SelectionStrategyRouletteRanked())
                .setBreedingStrategy(new BreedingStrategyUniform())
                .scoreFunction(i -> calculateScore(i))
                .build();

        // Run evolution until we get exact solution.
        for (int j = 0; j < 250; j++) {
            // Perform one evolution step.
            javolver.doOneCycle();

            Individual best = javolver.findBestScoringIndividual();

            // Print output every so often.
            if (j%20==0) {
                System.out.println("Iteration " + j + "  " + javolver.report() + "  score:" + javolver.getBestScore(null));
                System.out.println("[" + toString(best) + "]");
            }

            // Check if we have arrived at the target string.
            if (best.toString().trim().equals(targetWord)) break;
        }

        System.out.print("END");
    }

    // Compare each character in the string to the target string and return a score.
    // Each character gets a higher score the closer it is to the target character.
    public static double calculateScore(Individual individual) {
        double total = 0.0;
        for (int i=0;i<targetWord.length();i++) {
            total += getScoreForCharacter(individual.getDna().getChar(i), targetWord.charAt(i));
        }
        return total;
    }

    public static double getScoreForCharacter(char a, char b)
    {
        int maxDiff = 15;
        int diff = Math.abs(a-b);
        if (diff>maxDiff) return 0.0;
        return ((maxDiff-diff)/10.0);
    }

    public static String toString(Individual individual)
    {
        String str = "";
        for (int i=0;i<individual.getDna().getData().length;i++) {
            str = str + individual.getDna().getChar(i);
        }
        return str;
    }
}
