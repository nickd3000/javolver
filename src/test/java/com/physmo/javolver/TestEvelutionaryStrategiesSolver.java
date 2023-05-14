package com.physmo.javolver;

import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.solver.Optimizer;
import com.physmo.javolver.solver.OptimizerES;
import com.physmo.javolver.solver.Solver;
import org.junit.Test;

public class TestEvelutionaryStrategiesSolver {

    @Test
    public void testOptimizer() {
        Solver optimizer = new OptimizerES();
        optimizer.setDnaSize(10);
        optimizer.setScoreFunction(i -> calculateScore(i));
        optimizer.init();

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
