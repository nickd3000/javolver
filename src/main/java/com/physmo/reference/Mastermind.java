package com.physmo.reference;


import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorShuffle;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;

public class Mastermind {

    int[] solution = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    public static void main(String[] args) {
        Mastermind mastermind = new Mastermind();
        mastermind.go();
    }

    private void go() {

        Solver solver = Javolver.builder()
                .populationTargetSize(50).dnaSize(solution.length)
                .keepBestIndividualAlive(false)
                .addMutationOperator(new MutationOperatorSimple(1, 0.5))
                .addMutationOperator(new MutationOperatorShuffle(1))
                .addMutationOperator(new MutationOperatorSwap(0.1, 2))
                .setSelectionOperator(new SelectionOperatorTournament(0.15))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(this::calculateScore).build();

        for (int i = 0; i < 20; i++) {
            solver.doOneCycle();
            Individual bestA = solver.getBestScoringIndividual();

            System.out.printf("Iteration: %2d  score: %4.1f   solution: %s %n", i, bestA.getScore(), toString(bestA));
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
        for (int j : solution) {
            if (number == j) return true;
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
