package com.physmo.reference;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;

/**
 * Demo class that uses a genetic algorithm to solve a simple 0/1 Knapsack problem.
 * Uses the project's genetic algorithm system (Javolver).
 */
public class KnapsackDemo {

    // Example items: {weight, value}
    static final int[] weights = {3, 2, 1, 4, 5, 7, 8, 9, 6, 5};
    static final int[] values  = {25,20,15,40,50,55,60,75,35,25};
    static final int MAX_WEIGHT = 35;
    static final int NUM_ITEMS = weights.length;

    public static void main(String[] args) {
        int populationSize = 500;

        Solver solver = Javolver.builder()
                .dnaSize(NUM_ITEMS)
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(false)
                .addMutationOperator(new MutationOperatorSimple(1, 0.5))
                .setSelectionOperator(new SelectionOperatorTournament(0.3))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(KnapsackDemo::calculateScore)
                .build();

        // Run the evolution loop for a defined number of generations
        for (int generation = 0; generation < 1000; generation++) {

            Individual best = solver.getBestScoringIndividual();

            if (generation % 100 == 0 || generation == 99) {
                KnapsackResult result = evaluateIndividual(best);

                System.out.printf("Generation %3d | Value: %3d | Weight: %3d | Items: %s%n",
                        generation, result.totalValue(), result.totalWeight(), genomeToString(result.items()));
            }
            solver.doOneCycle();

        }

        // Print the best solution found
        Individual best = solver.getBestScoringIndividual();
        KnapsackResult result = evaluateIndividual(best);
        System.out.println("Best solution found:");
        System.out.printf("Value: %d, Weight: %d, Items: %s%n",
                result.totalValue(), result.totalWeight(), genomeToString(result.items()));
    }

    private record KnapsackResult(boolean[] items, int totalValue, int totalWeight) {
    }

    private static KnapsackResult evaluateIndividual(Individual individual) {
        boolean[] items = genomeToItems(individual);
        int totalValue = 0;
        int totalWeight = 0;

        for (int i = 0; i < NUM_ITEMS; i++) {
            if (items[i]) {
                totalValue += values[i];
                totalWeight += weights[i];
            }
        }

        return new KnapsackResult(items, totalValue, totalWeight);
    }

    // Converts Individual's genome to boolean[] denoting which items are chosen
    public static boolean[] genomeToItems(Individual individual) {
        boolean[] items = new boolean[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++) {
            // Any nonzero DNA value means "include"
            items[i] = (individual.getDna().getDouble(i) > 0.5);
        }
        return items;
    }

    // Returns fitness: value if total weight is under MAX_WEIGHT, else 0
    public static double calculateScore(Individual individual) {
        KnapsackResult result = evaluateIndividual(individual);

        // Penalize overweight genomes
        return (result.totalWeight() <= MAX_WEIGHT) ? result.totalValue() : 0.0;
    }

    // Pretty-prints selected items as a string of 0/1
    public static String genomeToString(boolean[] items) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (boolean b : items) {
            sb.append(b ? "1" : "0");
        }
        sb.append("]");
        return sb.toString();
    }
}