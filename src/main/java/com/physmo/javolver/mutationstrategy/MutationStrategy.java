package com.physmo.javolver.mutationstrategy;

import com.physmo.javolver.Individual;

public interface MutationStrategy {
    void mutate(Individual individual, double scaleChange);
}
