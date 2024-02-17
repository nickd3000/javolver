package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;

public abstract class Solver {
    int iteration = 0;
    private double temperature = 1;

    public abstract void init();

    public void doOneCycle() {
        iteration++;
        runOneGeneration();
    }


    abstract void runOneGeneration();

    public abstract Individual getBestScoringIndividual();

    // 0..1 value controlling mutation amount.
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getIteration() {
        return iteration;
    }

    public abstract void setScoreFunction(ScoreFunction scoreFunction);

    public abstract void setDnaSize(int size);
}
