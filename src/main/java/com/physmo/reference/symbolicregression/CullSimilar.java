package com.physmo.reference.symbolicregression;

import com.physmo.javolver.Individual;
import com.physmo.javolver.solver.Javolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CullSimilar {
    public void run(Javolver solver) {
        List<Individual> pool = solver.getPool();
        Map<Integer, Individual> uniqueIndividuals = new HashMap<>();

        for (Individual individual : pool) {
            uniqueIndividuals.put(getHashForIndividual(individual), individual);
        }

        // Empty the pool
        pool.clear();

        // Add only unique individuals
        for (Integer key : uniqueIndividuals.keySet()) {
            if (Math.random() < 0.2) {
                pool.add(uniqueIndividuals.get(key));
            }
        }

    }

    public int getHashForIndividual(Individual individual) {
        double[] data = individual.getDna().getData();
        int total = 0;
        for (int i = 0; i < data.length; i++) {
            total += (int) (data[i] * 10);
        }
        return total;
    }

}
