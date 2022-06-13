package com.physmo.javolver.selectionstrategy;

import java.util.List;

import com.physmo.javolver.Individual;

public class SelectionStrategyTournament implements SelectionStrategy {

	private final double selectionRange;
	
	public SelectionStrategyTournament(double selectionRange) {
		this.selectionRange = selectionRange;
	}

	@Override
	public Individual select(List<Individual> pool) {
		int poolSize = pool.size();
		double maxScore = -1000;
		Individual currentWinner = getRandomIndividual(pool);
		
		double tournamentSize = selectionRange;
		
		int tSize = (int)(tournamentSize*(double)poolSize);
		if (tSize<2) tSize=2;

		for (int i=0;i<tSize;i++)
		{
			Individual contender = getRandomIndividual(pool);
			
			if (getSelectionScore(contender)>maxScore || maxScore==-1000)
			{
				maxScore = getSelectionScore(contender);
				currentWinner = contender;
			}
		}
		
		return currentWinner;
	}

	/**
	 * Used to get score for an individual while performing selection.
	 * This takes into account any extra affects like preferring diversity.
	 * @param	ind		The Individual
	 * @return	Score or rank value.
	 */
    private double getSelectionScore(Individual ind) {
		return ind.getScore();
	}
	
	/***
	 * Return an individual randomly selected from the supplied list of individuals.
	 * @param	pool	The pool of individuals to select from.
	 * @return			Random member of the supplied list.
	 */
    private Individual getRandomIndividual(List<Individual> pool)
	{
		int id = (int)((float)(pool.size()-1) * Math.random());
		return pool.get(id);
	}
}
