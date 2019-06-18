package com.physmo.javolver.examples;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRouletteRanked;

// A very simple minimal example.
public class WordFinder {

    private static final int populationSize = 1000;

    private static final String targetWord = "ABCDEFGHIJK";

    public static void main(String[] args) {

        // Create single Javolver class and attach the functionality we require.
        Javolver javolver = new Javolver(new GeneWord(targetWord), populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.1, 0.25))
                //.setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setSelectionStrategy(new SelectionStrategyRouletteRanked())
                .setBreedingStrategy(new BreedingStrategyUniform());

        // Run evolution until we get exact solution.
        for (int j = 0; j < 250; j++) {

            // doOneCycle performs one full sequence of evaluation, breeding and mutation.
            javolver.doOneCycle();

            // Retrieve the best scoring individual for inspection.
            GeneWord best = (GeneWord)javolver.findBestScoringIndividual();

            // Print output every so often.
            System.out.println("Iteration " + j + "  " + javolver.report() + "  score:" + javolver.getBestScore(null));

            // Check if we have arrived at the target string.
            if (best.toString().trim().equals(targetWord)) break;
        }

        System.out.print("END");
    }

}

// GeneWord defines our individual.
class GeneWord extends Individual {

    private String targetWord = "EVOLUTION";

    public GeneWord(String target) {
        targetWord = target;
        dna.init(targetWord.length());
    }

    public Individual clone()
    {
        return (Individual)(new GeneWord(targetWord));
    }

    public String toString()
    {
        String str = "";
        for (int i=0;i<dna.getData().length;i++)
        {
            str += dna.getChar(i);
        }
        return str;
    }

    // Compare each character in the string to the target string and return a score.
    // Each character gets a higher score the closer it is to the target character.
    @Override
    public double calculateScore() {
        double total = 0.0;
        for (int i=0;i<targetWord.length();i++)
        {
            total += getScoreForCharacter(dna.getChar(i), targetWord.charAt(i));
        }
        return total;
    }

    // Returns a higher value the closer the characters to each other alphabetically.
    private double getScoreForCharacter(char a, char b)
    {
        int maxDiff = 15;
        int diff = Math.abs(a-b);
        if (diff>maxDiff) return 0.0;
        return (double)((maxDiff-diff)/10.0);
    }

}
