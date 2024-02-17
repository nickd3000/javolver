package com.physmo.javolver.experimental;


import com.physmo.javolver.Individual;
import com.physmo.javolver.Scoring;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorGeneBased;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;

// Testing out evolutionary strategy mutator
// (Mutation params are stored in gene)
public class WordFinderES {
    public static final String targetWord = "EVOLUTION OF A WORD";
    public static final int geneSkip = 2;

    public static void main(String[] args) {

        Solver solver = Javolver.builder()
                .dnaSize(targetWord.length() + geneSkip)
                .populationTargetSize(500)
                .keepBestIndividualAlive(false)
                .addMutationOperator(new MutationOperatorGeneBased(0, 1))
                .setSelectionOperator(new SelectionOperatorTournament(0.3))
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(WordFinderES::calculateScore)
                .build();

        // Run evolution until we get exact solution.
        for (int j = 0; j < 5000; j++) {
            // Perform one evolution step.
            solver.doOneCycle();

            Individual best = solver.getBestScoringIndividual();
            boolean exactMatch = toString(best).trim().equals(targetWord);

            // Print output every so often.
            if (j % 5 == 0 || exactMatch) {
                String controls = best.getDna().getDouble(0) + ", " + best.getDna().getDouble(1);
                System.out.printf("[%s] Iteration %d Score %6.2f  "+controls+"%n", toString(best), j, solver.getBestScoringIndividual().getScore());
            }

            // Stop if we have an exact match.
            if (exactMatch) break;
        }

        System.out.print("END");
    }

    // Compare each character in the string to the target string and return a score.
    // Each character gets a higher score the closer it is to the target character.
    public static double calculateScore(Individual individual) {
        double total = 0.0;
        for (int i = 0; i < targetWord.length(); i++) {
            total += Scoring.getScoreForCharacter(individual.getDna().getChar(i + geneSkip), targetWord.charAt(i));
        }
        return total;
    }


    public static String toString(Individual individual) {
        String str = "";
        for (int i = geneSkip; i < individual.getDna().getData().length; i++) {
            str = str + individual.getDna().getChar(i);
        }
        return str;
    }
}
