package com.physmo.javolver;


public class Scoring {

    /**
     * Calculates a score based on the difference between the actual and expected values,
     * normalized by the provided range. The score is scaled between 0.0 and 1.0, where
     * a smaller difference results in a higher score.
     *
     * @param actual   the actual value to evaluate
     * @param expected the expected target value
     * @param range    the maximum allowed range for scoring; differences beyond this
     *                 range are clamped
     * @return the computed score as a double, where 1.0 represents a perfect match
     *         and 0.0 represents the maximum difference within the range
     */
    public static double scoreValue(double actual, double expected, double range) {
        double diff = Math.abs(expected - actual);
        if (diff > range) diff = range;
        diff = 1.0-(diff / range);
        return (diff * diff);
    }

    public static double getScoreForCharacter(char a, char b) {
        int range = 15;
        double diff = Math.abs(a - b);
        if (diff > range) diff = range;
        diff = 1.0-(diff / range);
        return (diff * diff);
    }
}
