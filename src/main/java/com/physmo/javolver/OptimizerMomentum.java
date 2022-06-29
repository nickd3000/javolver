package com.physmo.javolver;

import com.physmo.javolver.mutationstrategy.MutationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OptimizerMomentum implements Solver {

    private final List<MutationStrategy> mutationStrategies = new ArrayList<MutationStrategy>();
    Individual bestIndividual;
    int dnaSize = 10;
    int stuckCounter = 0;
    double stuckScore = 0;
    Random random = new Random();
    double[] momentum;
    private ScoreFunction scoreFunction;

    public OptimizerMomentum() {

    }

    @Override
    public void init() {
        bestIndividual = new Individual(dnaSize);
        bestIndividual.setScoreFunction(scoreFunction);
        momentum = new double[dnaSize];
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
        algorithm1();
    }

    public void algorithm1() {
        double currentScore = bestIndividual.getScore();
        double speed = 0.005;
        // Change each element of the dna to find what direction results in improvement.
        int j = random.nextInt(dnaSize);
        double dir = testDnaElement(j, 0.01);
        if (dir > 0 && momentum[j] > 0) momentum[j] *= 0.2;
        if (dir < 0 && momentum[j] < 0) momentum[j] *= 0.2;

        momentum[j] += dir * speed;

        Individual clone = bestIndividual.cloneFully();

        // Update each DNA element using momentum table.
        for (int i = 0; i < dnaSize; i++) {
            double dnaElement = clone.getDna().getDouble(i);
            clone.getDna().set(i, dnaElement + momentum[i]);
            momentum[i] *= 0.98; // friction
        }


        clone.setUnprocessed();
        double originalScore = bestIndividual.getScore();
        double newScore = clone.getScore();
        bestIndividual = clone;

        if (newScore > originalScore) {
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

    public double testDnaElement(int index, double changeSize) {
        Individual clone = bestIndividual.cloneFully();

        double dnaElement = clone.getDna().getDouble(index);
        clone.getDna().set(index, dnaElement - changeSize);
        clone.setUnprocessed();
        double scoreNeg = clone.getScore();

        clone.getDna().set(index, dnaElement + changeSize);
        clone.setUnprocessed();
        double scorePos = clone.getScore();

        if (Math.random() < 0.5) {
            if (scoreNeg > bestIndividual.getScore()) return -1;
            else if (scorePos > bestIndividual.getScore()) return 1;
            else return 0;
        } else {
            if (scorePos > bestIndividual.getScore()) return 1;
            else if (scoreNeg > bestIndividual.getScore()) return -1;
            else return 0;
        }

    }

    @Override
    public Individual getBestScoringIndividual() {
        return bestIndividual;
    }

    @Override
    public void setChangeAmount(double changeAmount) {

    }

    @Override
    public int getIteration() {
        return 0;
    }

    public void addMutationStrategy(MutationStrategy strategy) {
        mutationStrategies.add(strategy);
    }
}
