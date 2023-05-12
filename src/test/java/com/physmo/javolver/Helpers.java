package com.physmo.javolver;

import com.physmo.javolver.Individual;

import java.util.ArrayList;
import java.util.List;

public class Helpers {
    public static List<Individual> generateTestPool(int size) {
        List<Individual> pool = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Individual individual = new Individual(2);
            individual.getDna().set(0, i);
            individual.setScoreFunction(i1 -> i1.getDna().getDouble(0));
            pool.add(individual);
        }
        return pool;
    }
}
