package com.physmo.javolver.selectionstrategy;

import java.util.List;
import com.physmo.javolver.Individual;

// TODO: describe this interface
public interface SelectionStrategy {
	Individual select(List<Individual> pool);
}

