package com.physmo.javolver.selectionstrategy;

import com.physmo.javolver.Individual;

import java.util.List;

/*
	Note, this selection strategy has no real use except in testing.
 */
public class SelectionStrategyRandom implements SelectionStrategy {

	@Override
	public Individual select(List<Individual> pool) {
		int numIndividuals = pool.size();
		int randomOne = (int)(Math.random()*(double)numIndividuals);

		return pool.get(randomOne);

	}

}
