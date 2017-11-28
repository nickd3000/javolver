package javolver.selectionstrategy;

import java.util.ArrayList;
import java.util.List;

import javolver.Individual;
import javolver.JavolverConfig;

public class SelectionStrategyTournament implements SelectionStrategy {

	@Override
	public Individual select(JavolverConfig config, List<Individual> pool) {
		int poolSize = pool.size();
		double maxScore = -1000;
		Individual currentWinner = getRandomIndividual(pool);
		
		double tournamentSize = config.selectionRange;
		
		int tSize = (int)(tournamentSize*(double)poolSize);
		if (tSize<2) tSize=2;
		
		for (int i=0;i<tSize;i++)
		{
			Individual contender = getRandomIndividual(pool);
			
			if (getSelectionScore(config, contender)>maxScore || maxScore==-1000)
			{
				maxScore = getSelectionScore(config, contender);
				currentWinner = contender;
			}
		}
		
		return currentWinner;
	}

	/**
	 * Used to get score for an individual while performing selection.
	 * This takes into account any extra affects like preferring diversity.
	 * @param	config	Program settings
	 * @param	ind		The Individual
	 * @return	Score or rank value.
	 */
	public double getSelectionScore(JavolverConfig config, Individual ind) {

		// Special handling if we are using the rank method.
		if (config.selectionUseScoreRank==true) {
			double score = (double)ind.getRankScore();
			
			if (config.selectionUseDiversityRank==true) {
				score+=(double)ind.getRankDiversity()*0.5;
			}
			
			return score;
		}
		
		// Otherwise just return the individuals score.
		return ind.getScore();
	
	}
	
	/***
	 * Return an individual randomly selected from the supplied list of individuals.
	 * @param	pool	The pool of individuals to select from.
	 * @return			Random member of the supplied list.
	 */
	public Individual getRandomIndividual(List<Individual> pool)
	{
		int id = (int)((float)(pool.size()-1) * Math.random());
		return pool.get(id);
	}
}
