package com.physmo.reference;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;
import com.physmo.javolver.solver.Warmup;

/**
 * Example class that uses a genetic algorithm to evolve a population of strings
 * attempting to match a target phrase. Demonstrates evolutionary computation concepts
 * with the Javolver library.
 */
public class WordFinder {

    /** The phrase that the algorithm tries to evolve towards. */
    public static final String targetWord = "EVOLUTION OF A WORD";

    /**
     * Entry point for running the WordFinder example.
     * <p>
     * Sets up the genetic algorithm solver and performs iterative evolution steps,
     * printing out the best candidate at intervals. Stops early if the exact phrase
     * is matched.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        Solver solver = Javolver.builder()
                .dnaSize(targetWord.length())
                .populationTargetSize(50)
                .keepBestIndividualAlive(true)
                .addMutationOperator(new MutationOperatorSimple(1, 0.55))
                .setSelectionOperator(new SelectionOperatorTournament(0.3))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(WordFinder::calculateScore)
                .build();

        Warmup.warmup(solver, 10);

        // Run evolution until an exact solution is found or maximum cycles reached.
        for (int j = 0; j < 500; j++) {
            solver.doOneCycle();

            Individual best = solver.getBestScoringIndividual();
            boolean exactMatch = toString(best).trim().equals(targetWord);

            // Print the current state at regular intervals or on match.
            if (j % 15 == 0 || exactMatch) {
                System.out.printf("[%s] Iteration %d Score %6.2f %n",
                        toString(best), j, best.getScore());
            }

            // Stop early if a perfect match.
            if (exactMatch) break;
        }

        System.out.print("END");
    }

    /**
     * Calculate a fitness score for an individual by comparing its sequence
     * to the target phrase, favoring closeness for each character.
     *
     * @param individual The individual to score.
     * @return The fitness score.
     */
    public static double calculateScore(Individual individual) {
        double total = 0.0;
        for (int i = 0; i < targetWord.length(); i++) {
            total += getScoreForCharacter(individual.getDna().getChar(i), targetWord.charAt(i));
        }
        return total;
    }

    /**
     * Scores a guessed character against the target character. Gives a higher score
     * for closer matches, up to a certain difference.
     *
     * @param a The guessed character.
     * @param b The target character.
     * @return The score for this character pair.
     */
    public static double getScoreForCharacter(char a, char b) {
        int maxDiff = 15;
        int diff = Math.abs(a - b);
        if (diff > maxDiff) return 0.0;
        return ((maxDiff - diff) / 10.0);
    }

    /**
     * Converts an individual's DNA into a string representation.
     *
     * @param individual The individual to convert.
     * @return The constructed string from the individual's DNA.
     */
    public static String toString(Individual individual) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < individual.getDna().getData().length; i++) {
            str.append(individual.getDna().getChar(i));
        }
        return str.toString();
    }
}