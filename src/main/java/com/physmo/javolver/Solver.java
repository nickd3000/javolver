package com.physmo.javolver;

public interface Solver {
    void init();

    void doOneCycle();

    Individual findBestScoringIndividual();

    void setTemperature(double temperature);
}
