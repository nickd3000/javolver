package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.mutationstrategy.MutationStrategy;

import java.util.*;


/**
 * Evelutionary Strategies based solver.
 */
public class OptimizerES implements Solver {

    private final List<MutationStrategy> mutationStrategies = new ArrayList<>();
    Individual bestIndividual;
    int dnaSize = 10;
    int poolSize = 20;
    int combineSize = 5;
    int mutationCount = 2;
    int iteration = 0;
    Random random = new Random();
    double changeAmount = 1;
    private ScoreFunction scoreFunction;

    public OptimizerES() {

    }

    @Override
    public void init() {
        bestIndividual = new Individual(dnaSize);
        bestIndividual.setScoreFunction(scoreFunction);

    }

    @Override
    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    @Override
    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    @Override
    public void doOneCycle() {
        iteration++;
        algorithm();
    }

    public void algorithm() {
        List<Individual> pool = new ArrayList<>();

        // Create pool of mutated clones.
        for (int i = 0; i < poolSize; i++) {
            pool.add(createMutatedClone(bestIndividual, 5.0));
        }

        // Sort pool.
        pool.sort(Comparator.comparingDouble(Individual::getScore).reversed());

        // Combine top results.
        Individual clone = bestIndividual.cloneFully();
        clone.setProcessed(false);

        double[] cloneDnaArray = clone.getDna().getData();
        Arrays.fill(cloneDnaArray, 0);
        for (int i = 0; i < combineSize; i++) {
            double[] parentDnaArray = pool.get(i).getDna().getData();
            for (int j = 0; j < parentDnaArray.length; j++) {
                cloneDnaArray[j] += parentDnaArray[j] / (double) combineSize;
            }
        }

        bestIndividual = clone;

    }

    public Individual createMutatedClone(Individual parent, double mutationAmount) {
        Individual clone = parent.cloneFully();
        clone.setProcessed(false);

        for (int j = 0; j < mutationCount; j++) {
            int i = random.nextInt(clone.getDna().getSize());
            double dnaElement = clone.getDna().getDouble(i);
            dnaElement += (Math.random() - 0.5) * mutationAmount * changeAmount;
            clone.getDna().set(i, dnaElement);
        }
        return clone;
    }

    @Override
    public Individual getBestScoringIndividual() {
        return bestIndividual;
    }

    @Override
    public void setTemperature(double temperature) {
        this.changeAmount = temperature;
    }

    @Override
    public int getIteration() {
        return iteration;
    }

    public void addMutationStrategy(MutationStrategy strategy) {
        mutationStrategies.add(strategy);
    }
}
