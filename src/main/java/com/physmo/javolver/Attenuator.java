package com.physmo.javolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Attenuator: smoothly interpolates parameters between min/max as fitness/score improves.
 * Useful for dynamically adapting parameters (like mutation rates) in evolutionary algorithms.
 */
public class Attenuator {

    private double minScore;
    private double maxScore;
    private double currentScore;
    private double previousScore;
    private double normalizedScore;

    private final Map<String, Double> minValues = new HashMap<>();
    private final Map<String, Double> maxValues = new HashMap<>();

    /**
     * Set the domain of scores for attenuation.  
     * Call before using other methods.
     */
    public Attenuator setScoreRange(double min, double max) {
        if (max <= min)
            throw new IllegalArgumentException("Max score must be greater than min score");
        this.minScore = min;
        this.maxScore = max;
        return this;
    }

    /**
     * Register a parameter to attenuate.  
     * @param param parameter name (e.g. "mutation", "learningRate")
     * @param min   value when score is lowest
     * @param max   value when score is highest
     */
    public Attenuator addParam(String param, double min, double max) {
        Objects.requireNonNull(param);
        minValues.put(param, min);
        maxValues.put(param, max);
        return this;
    }

    /**
     * Set the current score (in the configured range).
     * The attenuator will calculate a normalized score based on the range
     * provided in {@link #setScoreRange(double, double)}.
     *
     * @param score The current fitness score.
     */
    public void setScore(double score) {
        if (score<previousScore) score=previousScore;
        this.currentScore = Math.max(minScore, Math.min(maxScore, score));
        this.normalizedScore = (this.currentScore - minScore) / (maxScore - minScore);
        previousScore = score;
    }

    /**
     * Get the current attenuated value for a parameter based on the last score set.
     *
     * @param param The name of the parameter.
     * @return The interpolated value between min and max for the current normalized score.
     */
    public double getValue(String param) {
        if (!minValues.containsKey(param) || !maxValues.containsKey(param)) {
            throw new IllegalArgumentException("Unknown parameter: " + param);
        }
        double min = minValues.get(param);
        double max = maxValues.get(param);
        return min + (max - min) * normalizedScore;
    }

    /**
     * Expose parameter info for diagnostics.
     */
    public Map<String, Double[]> getRegisteredParameters() {
        Map<String, Double[]> params = new HashMap<>();
        for (String param : minValues.keySet()) {
            params.put(param, new Double[]{ minValues.get(param), maxValues.get(param) });
        }
        return Collections.unmodifiableMap(params);
    }
}