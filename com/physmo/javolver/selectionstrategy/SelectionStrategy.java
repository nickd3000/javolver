package com.physmo.javolver.selectionstrategy;

import java.util.List;
import com.physmo.javolver.Individual;

public interface SelectionStrategy {
	public Individual select(List<Individual> pool);
}

