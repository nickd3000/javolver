package com.physmo.javolver;

import java.util.ArrayList;

public interface Solver {
    void init();
    void doOneCycle();
    Individual findBestScoringIndividual();

    void setTemperature(double temperature);
}
