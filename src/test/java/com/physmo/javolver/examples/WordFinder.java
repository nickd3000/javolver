package com.physmo.javolver.examples;


import com.physmo.javolver.Individual;
import com.physmo.javolver.Scoring;
import com.physmo.javolver.Spreader;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;
import org.junit.Test;

public class WordFinder {
    public static final String targetWord = "EVOLUTION OF A WORD";

    @Test
    public void runTest() {

        Solver solver = Javolver.builder()
                .dnaSize(targetWord.length())
                .populationTargetSize(50)
                .keepBestIndividualAlive(true)
                .addMutationOperator(new MutationOperatorSimple(1, 0.55))
                .setSelectionOperator(new SelectionOperatorTournament(0.3))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(this::calculateScore)
                .build();

        solver.doOneCycle();
        Spreader spreader=new Spreader(0.001,0,1);
        for (int i=0;i<100;i++) spreader.spread(((Javolver)solver).getPool());

        // Run evolution until we get exact solution.
        for (int j = 0; j < 5000; j++) {
            // Perform one evolution step.
            solver.doOneCycle();

            Individual best = solver.getBestScoringIndividual();
            boolean exactMatch = toString(best).trim().equals(targetWord);

            // Print output every so often.
            if (j % 5 == 0 || exactMatch) {
                System.out.printf("[%s] Iteration %d Score %6.2f %n", toString(best), j, solver.getBestScoringIndividual().getScore());
            }

            // Stop if we have an exact match.
            if (exactMatch) break;
        }

        System.out.print("END");
    }

    // Compare each character in the string to the target string and return a score.
    // Each character gets a higher score the closer it is to the target character.
    public double calculateScore(Individual individual) {
        double total = 0.0;
        for (int i = 0; i < targetWord.length(); i++) {
            total += Scoring.getScoreForCharacter(individual.getDna().getChar(i), targetWord.charAt(i));
        }
        return total;
    }


    public String toString(Individual individual) {
        String str = "";
        for (int i = 0; i < individual.getDna().getData().length; i++) {
            str = str + individual.getDna().getChar(i);
        }
        return str;
    }
}
