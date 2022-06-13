package com.physmo.javolver;

import com.physmo.javolver.mutationstrategy.MutationStrategy;

import java.util.ArrayList;
import java.util.List;

public class Optimizer implements Solver {

    private final List<MutationStrategy> mutationStrategies = new ArrayList<MutationStrategy>();
    Individual bestIndividual;
    int dnaSize = 10;
    private ScoreFunction scoreFunction;

    private double temperature = 0;

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

    int stuckCounter = 0;
    double stuckScore = 0;

    public void algorithm1() {

        Individual clone = cloneIndividualFully(bestIndividual);

        for (MutationStrategy mutationStrategy : mutationStrategies) {
            mutationStrategy.mutate(clone);
        }

        double originalScore = bestIndividual.getScore();
        double newScore = clone.getScore();

        if (newScore > originalScore) {
            if (newScore<0) {
                int njd =2;
                njd++;
            }
            bestIndividual = clone;

        }
        else if (Math.random() < temperature && newScore > originalScore*0.999) {

            if (newScore<0) {
                int njd =2;
                njd++;
            }
            bestIndividual = clone;
        }
        else if (stuckCounter>20) {
            bestIndividual = clone;
            stuckCounter=0;
        }


        if (originalScore!=stuckScore) {
            stuckCounter=0;
            stuckScore=originalScore;
        } else {
            stuckCounter++;
        }
    }

    public Individual cloneIndividualFully(Individual i1) {
        Individual i2 = new Individual(i1.getDna().getSize());
        for (int i = 0; i < dnaSize; i++) {
            i2.getDna().set(i, i1.getDna().getDouble(i));
        }
        i2.setScoreFunction(i1.getScoreFunction());
        i2.setUnprocessed();
        return i2;
    }

    @Override
    public Individual findBestScoringIndividual() {
        return bestIndividual;
    }

    @Override
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void addMutationStrategy(MutationStrategy strategy) {
        mutationStrategies.add(strategy);
    }
}
