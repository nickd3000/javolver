package com.physmo.javolver;


public class Scoring {

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
