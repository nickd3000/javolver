package com.physmo.reference.travellingsalesman;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * BreedingStrategyTS is a specific implementation of the BreedingOperator interface,
 * designed to produce a single offspring from two parent individuals.
 * <p>
 * This class follows a strategy of combining the parents' DNA while ensuring that no
 * duplicate genes are present in the resulting offspring's DNA. If conflicts arise
 * (i.e., both parents propose duplicate genes at a position), the method defers resolving
 * these conflicts using a random unused gene assignment. This assignment ensures all genes
 * are represented exactly once in the offspring.
 * <p>
 * The primary functionality is defined in the {@code breed()} method, which:
 * - Iteratively attempts to fill the child's DNA by checking for unused genes contributed by each parent.
 * - Tracks skipped positions caused by conflicts in gene assignment.
 * - Resolves skipped positions by randomly selecting unused genes.
 * <p>
 * This breeding strategy is especially suited for problems where uniqueness of DNA elements
 * is a critical constraint, such as the Traveling Salesman Problem or similar combinatorial
 * optimization problems.
 */
public class BreedingStrategyTS implements BreedingOperator {
    @Override
    public List<Individual> breed(Individual parent1, Individual parent2) {
        Individual child1 = new Individual(parent1);
        int dnaSize = parent1.dna.getData().length;

        boolean[] usedList = new boolean[dnaSize];
        for (int i = 0; i < dnaSize; i++) {
            usedList[i] = false;
        }

        List<Integer> skipList = new ArrayList<>();

        for (int i = 0; i < dnaSize; i++) {
            int c1 = (int) parent1.getDna().getDouble(i);
            int c2 = (int) parent2.getDna().getDouble(i);

            if (!usedList[c1]) {
                child1.getDna().set(i, c1);
                usedList[c1] = true;
            } else if (!usedList[c2]) {
                child1.getDna().set(i, c2);
                usedList[c2] = true;
            } else {
                skipList.add(i);
            }
        }

        for (Integer skipId : skipList) {
            int c1 = findRandomUnusedOne(usedList);
            child1.getDna().set(skipId, c1);
        }

        List<Individual> returnList = new ArrayList<>();
        returnList.add(child1);
        return returnList;
    }

    int findRandomUnusedOne(boolean[] usedList) {
        for (int j = 0; j < 1000; j++) {
            int i = (int) (Math.random() * usedList.length);
            if (!usedList[i]) return i;
        }

        System.out.println("Timed out BreedingStrategy");

        return 0;
    }


}
