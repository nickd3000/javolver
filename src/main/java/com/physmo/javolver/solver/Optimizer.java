package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.mutationoperator.MutationOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple hill-climbing optimizer.
 * It maintains a single best individual and tries to improve it by applying
 * mutation strategies and keeping the result if it yields a better score.
 * It also includes a mechanism to "unstick" if no progress is made for a while.
 */
public class Optimizer extends Solver {

    private final List<MutationOperator> mutationStrategies = new ArrayList<>();
    Individual bestIndividual;
    int dnaSize = 10;
    int stuckCounter = 0;
    double stuckScore = 0;
    double changeAmount = 1;
    int iteration = 0;
    private ScoreFunction scoreFunction;

    /**
     * Default constructor.
     */
    public Optimizer() {

    }

    public static OptimizerBuilder builder() {
        return new OptimizerBuilder();
    }

    @Override
    public void init() {
        bestIndividual = new Individual(dnaSize);
        bestIndividual.setScoreFunction(scoreFunction);
    }

    @Override
    public void resetPopulation() {
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
    public void runOneGeneration() {
        algorithm();
    }

    /**
     * Core optimization logic. Clones the best individual, mutates it,
     * and updates the best individual if the clone is better.
     */
    public void algorithm() {

        Individual clone = bestIndividual.cloneFully();

        for (MutationOperator mutationStrategy : mutationStrategies) {
            mutationStrategy.mutate(clone, changeAmount);
        }

        double originalScore = bestIndividual.getScore();
        double newScore = clone.getScore();

        if (newScore > originalScore) {
            bestIndividual = clone;
        } else if (stuckCounter > 20) {
            // Apply a mutation even if it doesn't improve the score to get out of local optima.
            bestIndividual = clone;
            stuckCounter = 0;
            bestIndividual.setProcessed(false);
        }

        if (originalScore != stuckScore) {
            stuckCounter = 0;
            stuckScore = originalScore;
        } else {
            stuckCounter++;
        }
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

    /**
     * Adds a mutation operator to be used during the optimization process.
     *
     * @param strategy The mutation strategy to add.
     */
    public void addMutationStrategy(MutationOperator strategy) {
        mutationStrategies.add(strategy);
    }
}
