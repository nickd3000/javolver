package com.physmo.javolver;

import com.physmo.javolver.mutationstrategy.MutationStrategy;

import java.util.ArrayList;
import java.util.List;

public class Optimizer implements Solver {

    private final List<MutationStrategy> mutationStrategies = new ArrayList<MutationStrategy>();
    Individual bestIndividual;
    int dnaSize = 10;
    int stuckCounter = 0;
    double stuckScore = 0;
    private ScoreFunction scoreFunction;

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

    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    @Override
    public void doOneCycle() {
        algorithm1();
    }

    public void algorithm1() {

        Individual clone = bestIndividual.cloneFully();

        for (MutationStrategy mutationStrategy : mutationStrategies) {
            mutationStrategy.mutate(clone);
        }

        double originalScore = bestIndividual.getScore();
        double newScore = clone.getScore();

        if (newScore > originalScore) {
            if (newScore < 0) {
                int njd = 2;
                njd++;
            }
            bestIndividual = clone;

        } else if (stuckCounter > 20) {
            bestIndividual = clone;
            stuckCounter = 0;
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

    public void addMutationStrategy(MutationStrategy strategy) {
        mutationStrategies.add(strategy);
    }
}
