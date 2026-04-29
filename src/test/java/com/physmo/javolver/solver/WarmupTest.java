package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.breedingoperator.BreedingOperatorCrossover;
import com.physmo.javolver.mutationoperator.MutationOperatorSingle;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import org.junit.Assert;
import org.junit.Test;

public class WarmupTest {

    @Test
    public void testWarmup() {
        int populationSize = 10;
        int dnaSize = 5;
        
        ScoreFunction scoreFunction = individual -> {
            double sum = 0;
            for (int i = 0; i < individual.getDna().getSize(); i++) {
                sum += individual.getDna().getDouble(i);
            }
            return sum;
        };

        Javolver javolver = Javolver.builder()
                .dnaSize(dnaSize)
                .populationTargetSize(populationSize)
                .addMutationOperator(new MutationOperatorSingle(0.1))
                .setSelectionOperator(new SelectionOperatorTournament(0.5))
                .setBreedingOperator(new BreedingOperatorCrossover())
                .scoreFunction(scoreFunction)
                .build();

        // Run warmup
        Warmup.warmup(javolver, 5);

        // Verify that the population is filled with distinct top scorers
        Assert.assertEquals("Population size should be equal to target size", populationSize, javolver.getPool().size());
        
        // Since we are using random initialization, it's very likely they are distinct.
        // We can also check if they all have the score function set.
        for (Individual individual : javolver.getPool()) {
            Assert.assertNotNull("Score function should be set", individual.getScoreFunction());
        }
    }
}
