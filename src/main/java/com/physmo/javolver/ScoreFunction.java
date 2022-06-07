package com.physmo.javolver;

@FunctionalInterface
public interface ScoreFunction {
    double score(Individual i);
}
