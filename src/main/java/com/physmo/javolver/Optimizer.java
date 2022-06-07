package com.physmo.javolver;

import com.physmo.javolver.mutationstrategy.MutationStrategy;

import java.util.ArrayList;
import java.util.List;

public class Optimizer implements Solver {

    private final List<MutationStrategy> mutationStrategies = new ArrayList<MutationStrategy>();
    Individual bestIndividual;
    int dnaSize = 10;
    private ScoreFunction scoreFunction;

    public Optimizer() {

    }

    public static OptimizerBuilder builder() {
        return new OptimizerBuilder();
    }

    @Override
    public void init() {
        bestIndividual = new Individual(dnaSize);
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

    @Override
    public Individual findBestScoringIndividual() {
        return bestIndividual;
    }

    public void algorithm1() {

        Individual clone = cloneIndividualFully(bestIndividual);

        for (MutationStrategy mutationStrategy : mutationStrategies) {
            mutationStrategy.mutate(clone);
        }

        double originalScore = scoreFunction.score(bestIndividual);
        double newScore = scoreFunction.score(clone);

        if (newScore>originalScore-0.1) {
            bestIndividual = clone;
        }

    }

    public void addMutationStrategy(MutationStrategy strategy) {
        mutationStrategies.add(strategy);
    }

    public Individual cloneIndividualFully(Individual i1) {
        Individual i2 = new Individual(i1.getDna().getSize());
        for (int i=0;i<dnaSize;i++) {
            i2.getDna().set(i,i1.getDna().getDouble(i));
        }
        return i2;
    }
}
