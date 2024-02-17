package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;

public interface MutationOperator {
    void mutate(Individual individual, double temperature);
}
