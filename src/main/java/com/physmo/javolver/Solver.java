package com.physmo.javolver;

public interface Solver {
    void init();

    void doOneCycle();

    Individual getBestScoringIndividual();
    // 0..1 value controlling mutation amount.
    void setTemperature(double temperature);

    int getIteration();

    void setScoreFunction(ScoreFunction scoreFunction);

    void setDnaSize(int size);
}
