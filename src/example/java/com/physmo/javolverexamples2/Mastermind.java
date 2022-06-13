package com.physmo.javolverexamples2;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.Optimizer;
import com.physmo.javolver.Solver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;

public class Mastermind {

    int[] solution = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    public static void main(String[] args) {
        Mastermind mastermind = new Mastermind();
        mastermind.go();

    }


    private void go() {

        Solver testEvolver = Javolver.builder()
                .populationTargetSize(50).dnaSize(solution.length)
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySimple(1, 0.5))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setBreedingStrategy(new BreedingStrategyUniform())
                .scoreFunction(i -> calculateScore(i)).build();

        Solver testOptimizer = Optimizer.builder()
                .dnaSize(solution.length)
                .addMutationStrategy(new MutationStrategySimple(1, 0.5))
                .scoreFunction(i -> calculateScore(i)).build();

        testOptimizer.setTemperature(0.1);

        for (int i = 0; i < 1000; i++) {

            testEvolver.doOneCycle();
            testOptimizer.doOneCycle();

            Individual bestA = testEvolver.findBestScoringIndividual();
            Individual bestB = testOptimizer.findBestScoringIndividual();

            if (i % 10 == 0) {
                System.out.println("Iteration " + i);
                System.out.println("Javolver:  " + bestA.getScore() + " " + toString(bestA));
                System.out.println("Optimizer: " + bestB.getScore() + " " + toString(bestB));
            }
        }


    }

    public double calculateScore(Individual individual) {
        int score = 0;

        // Exact match.
        for (int i = 0; i < solution.length; i++) {
            if (getGuess(individual.getDna().getData(), i) == solution[i]) score += 20;
        }

        // Exists match
        for (int i = 0; i < solution.length; i++) {
            if (isNumberInSolution(getGuess(individual.getDna().getData(), i))) score += 10;
        }

        return score;
    }

    public int getGuess(double[] data, int index) {
        return (int) (data[index] * (solution.length + 1));
    }

    public boolean isNumberInSolution(int number) {
        for (int i = 0; i < solution.length; i++) {
            if (number == solution[i]) return true;
        }
        return false;
    }

    public String toString(Individual individual) {
        String str = "";
        for (int i = 0; i < solution.length; i++) {
            str += getGuess(individual.getDna().getData(), i) + ", ";
        }
        return str;
    }

}
