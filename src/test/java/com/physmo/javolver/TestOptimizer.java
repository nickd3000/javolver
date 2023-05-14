package com.physmo.javolver;

import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.solver.Optimizer;
import com.physmo.javolver.solver.Solver;
import org.junit.Test;

public class TestOptimizer {

    @Test
    public void testOptimizer() {
        Solver optimizer = Optimizer.builder()
                .dnaSize(10)
                .addMutationStrategy(new MutationStrategySimple(2, 0.01))
                .scoreFunction(this::calculateScore)
                .build();

        for (int i = 0; i < 10; i++) {
            optimizer.doOneCycle();
        }

        Individual bestScoringIndividual = optimizer.getBestScoringIndividual();
        int iteration = optimizer.getIteration();
        optimizer.setTemperature(1);
    }

    public double calculateScore(Individual idv) {
        double total = 0;
        for (double val : idv.getDna().getData()) {
            total += val;
        }
        return total;
    }
}
