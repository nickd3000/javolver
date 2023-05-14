package com.physmo.javolver;

import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.mutationstrategy.MutationStrategySingle;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.javolver.solver.Javolver;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestJavolver {
    final double scoreTarget = 100;

    @Test
    public void testJavolver() throws Exception {

        // Create the evolver:
        Javolver javolver = Javolver.builder()
                .dnaSize(5)
                .populationTargetSize(50)
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySingle(0.2))
                .setSelectionStrategy(new SelectionStrategyTournament(0.1))
                .setBreedingStrategy(new BreedingStrategyCrossover())
                .scoreFunction(this::calculateScore)
                .build();

        for (int i = 0; i < 1000; i++) {
            javolver.doOneCycle();

            Individual topScorer = javolver.getBestScoringIndividual();

            if ((i % 250) == 0) {
                System.out.println("Score: " + topScorer.getScore() + " Target: " + 100);
            }
        }

        double bestScore = javolver.getBestScore();

        double solutionTolerance = 0.03;
        boolean solutionFound = false;

        // Check that we evolved a solution.
        Individual topScorer = javolver.getBestScoringIndividual();
        double solutionDelta = scoreTarget - topScorer.getScore();
        if (Math.abs(solutionDelta) < solutionTolerance) solutionFound = true;
        if (solutionFound) System.out.println("Solution found.");

        assertTrue(solutionFound);

    }

    public double calculateScore(Individual individual) {
        // The score for this is represented by how
        // close the sum of all genes is to a certain number.

        double sum = 0;

        for (int i = 0; i < individual.getDna().getSize(); i++) {
            sum += individual.getDna().getDouble(i);
        }

        double delta = Math.abs(scoreTarget - sum);
        if (delta > scoreTarget) delta = scoreTarget;

        return scoreTarget - delta;
    }
}


