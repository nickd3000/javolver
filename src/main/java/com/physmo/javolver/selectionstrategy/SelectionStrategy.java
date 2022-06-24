package com.physmo.javolver.selectionstrategy;

import com.physmo.javolver.Individual;

import java.util.List;

// TODO: describe this interface
public interface SelectionStrategy {
    Individual select(List<Individual> pool);
}

