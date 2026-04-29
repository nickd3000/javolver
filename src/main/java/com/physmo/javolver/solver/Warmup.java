package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;

import java.util.LinkedHashSet;
import java.util.Set;

public class Warmup {

    /**
     * Pre-populates the solver with high-quality individuals by running multiple short evolutionary cycles.
     * Each cycle runs for a specified number of iterations, then the best individual is recorded.
     * This process repeats until enough distinct top-scoring individuals are collected to fill
     * the target population size, at which point they are injected back into the solver.
     *
     * @param solver     The solver instance to warm up.
     * @param iterations The number of iterations to run in each mini-evolutionary cycle.
     */
    public static void warmup(Solver solver, int iterations) {
        if (!(solver instanceof Javolver javolver)) {
            return;
        }
        int targetPopulationSize = javolver.getConfig().getTargetPopulationSize();

        Set<Individual> distinctIndividuals = new LinkedHashSet<>();

        while (distinctIndividuals.size() < targetPopulationSize) {
            solver.resetPopulation(); // Reset and populate
            for (int i = 0; i < iterations; i++) {
                solver.doOneCycle();
            }

            Individual best = solver.getBestScoringIndividual();
            if (best != null) {
                distinctIndividuals.add(best.cloneFully());
            }
        }

        javolver.getPool().clear();
        javolver.addIndividuals(distinctIndividuals);
    }
}
