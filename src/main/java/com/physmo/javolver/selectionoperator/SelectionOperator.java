package com.physmo.javolver.selectionoperator;

import com.physmo.javolver.Individual;

import java.util.List;

// TODO: describe this interface
public interface SelectionOperator {
    Individual select(List<Individual> pool);
}

