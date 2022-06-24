package com.physmo.javolver;

import java.util.HashMap;
import java.util.Map;

public class Attenuator {

    double minScore;
    double maxScore;
    double currentScore;
    double normalisedScore;

    Map<String, double[]> parameters = new HashMap<>();

    public Attenuator() {
        setCurrentScore(0);
    }

    public double setCurrentScore(double currentScore) {
        this.currentScore = currentScore;

        if (currentScore <= minScore) {
            normalisedScore = 0.0;
        } else if (currentScore >= maxScore) {
            normalisedScore = 1.0;
        } else {
            normalisedScore = (currentScore - minScore) / (maxScore - minScore);
        }
        return normalisedScore;
    }

    public double setCurrentIteration(int iteration) {
        return setCurrentScore(iteration);
    }

    public double getValue(String name) {
        double[] doubles = parameters.get(name);
        return lerp(doubles[0], doubles[1], normalisedScore);
    }

    public double lerp(double a, double b, double v) {
        return a + ((b - a) * v);
    }

    public void addParameter(String name, double minValue, double maxValue) {
        double[] array = new double[]{minValue, maxValue};
        parameters.put(name, array);
    }

    public void setScoreRange(double minScore, double maxScore) {
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public void setIterationRange(int maxIterations) {
        this.minScore = 0;
        this.maxScore = maxIterations;
    }
}


