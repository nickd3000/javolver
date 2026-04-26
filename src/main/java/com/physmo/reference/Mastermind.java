package com.physmo.reference;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorShuffle;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;

/**
 * An example class demonstrating the use of the Javolver genetic algorithm library to solve
 * a simple "Mastermind"-like problem, where the goal is to discover a fixed sequence of numbers.
 * <p>
 * The solver uses a population of candidate solutions (individuals) and evolves them through
 * selection, mutation, and breeding to approach the correct solution.
 */
public class Mastermind {

    // The correct solution that the algorithm is trying to guess.
    private static final int[] SOLUTION = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private static final int POPULATION_SIZE = 50;
    private static final int GENERATIONS = 200;

    /**
     * Main entry point. Creates a Mastermind instance and starts the search.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        Mastermind mastermind = new Mastermind();
        mastermind.go();
    }

    /**
     * Sets up and runs the genetic algorithm for a fixed number of iterations.
     * Prints out the best solution found on each iteration.
     */
    private void go() {
        Solver solver = Javolver.builder()
                .populationTargetSize(POPULATION_SIZE)
                .dnaSize(SOLUTION.length)
                .keepBestIndividualAlive(false)
                .addMutationOperator(new MutationOperatorSimple(1, 0.5))
                .addMutationOperator(new MutationOperatorShuffle(1))
                .addMutationOperator(new MutationOperatorSwap(0.1, 2))
                .setSelectionOperator(new SelectionOperatorTournament(0.15))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(this::calculateScore)
                .build();

        for (int i = 0; i < GENERATIONS; i++) {
            solver.doOneCycle();
            Individual bestA = solver.getBestScoringIndividual();
            System.out.printf(
                "Iteration: %2d  score: %4.1f   solution: %s %n",
                i, bestA.getScore(), formatGuess(bestA)
            );
        }
    }

    /**
     * Scores an individual by comparing its DNA to the correct solution.
     * - 20 points for each exact match in the right position.
     * - 10 points for each number guessed correctly in the wrong position.
     *
     * @param individual The individual to score.
     * @return The fitness score for the individual.
     */
    public double calculateScore(Individual individual) {
        int score = 0;
        double[] data = individual.getDna().getData();

        for (int i = 0; i < SOLUTION.length; i++) {
            int guess = getGuess(data, i);

            if (guess == SOLUTION[i]) {
                score += 20;
            } else if (isNumberInSolution(guess)) {
                score += 10;
            }
        }

        return score;
    }

    /**
     * Converts a DNA value to an integer guess.
     *
     * @param data  The DNA data array.
     * @param index The gene index to read.
     * @return The guessed integer value.
     */
    public int getGuess(double[] data, int index) {
        int guess = 1 + (int) (data[index] * SOLUTION.length);
        return Math.min(guess, SOLUTION.length);
    }

    /**
     * Checks if a number exists in the solution array.
     *
     * @param number The number to check.
     * @return True if the number is part of the solution; false otherwise.
     */
    public boolean isNumberInSolution(int number) {
        for (int j : SOLUTION) {
            if (number == j) return true;
        }
        return false;
    }

    /**
     * Converts an individual's DNA to a human-readable string of guesses.
     *
     * @param individual The individual whose DNA to display.
     * @return String representation of the individual's guesses.
     */
    public String formatGuess(Individual individual) {
        StringBuilder str = new StringBuilder();
        double[] data = individual.getDna().getData();

        for (int i = 0; i < SOLUTION.length; i++) {
            if (i > 0) str.append(", ");
            str.append(getGuess(data, i));
        }

        return str.toString();
    }
}