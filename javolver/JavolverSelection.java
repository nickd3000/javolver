package javolver;

import java.util.ArrayList;

public class JavolverSelection {

	/**
	 * tournament	chose fittest of a set number of individuals.
	 * roulette		chose fittest randomly, best individuals more likely to be selected.
	 */
	public enum SelectionType {TOURNAMENT,  ROULETTE};
	
	/**
	 * Use the selection method specified in config to select an individual from the supplied pool.
	 * @param pool		Pool of individuals to search in.
	 * @param config	Program settings.
	 * @return			Selected individual.
	 */
	public static Individual selectIndividual(ArrayList<Individual> pool, JavolverConfig config) {
		
		switch (config.selectionType) {
		case TOURNAMENT:
			return tournamentSelection(pool, config);
		case ROULETTE:
			return rouletteSelection(pool, config);
		default:
			return tournamentSelection(pool, config);
		}
		
	}
	
	/**
	 * Return the fittest individual from a pool, from a random selection.
	 * @param	pool			The pool of individuals to select from.
	 * @param	config			Program settings
	 * @return					The winner of the tournament
	 */
	public static Individual tournamentSelection(ArrayList<Individual> pool, JavolverConfig config)
	{
		int poolSize = pool.size();
		double maxScore = -1000;
		Individual currentWinner = null;
		
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
	 * Select a random gene from the pool weighted towards the fittest individuals.
	 * @param 	pool	The pool of individuals to select from.
	 * @param	config			Program settings
	 * @return			The winner of the selection process.
	 */
	public static Individual rouletteSelection(ArrayList<Individual> pool, JavolverConfig config)
	{
	    float totalScore = 0;
	    float runningScore = 0;
	    for (Individual g : pool)
	    {
	        totalScore += g.getScoreSquared();
	    }

	    float rnd = (float) (Math.random() * totalScore);

	    for (Individual g : pool)
	    {   
	        if (    rnd>=runningScore &&
	                rnd<=runningScore+g.getScoreSquared())
	        {
	            return g;
	        }
	        runningScore+=g.getScoreSquared();
	    }

	    return null;
	}
	
	
	/***
	 * Return an individual randomly selected from the supplied list of individuals.
	 * @param	pool	The pool of individuals to select from.
	 * @return			Random member of the supplied list.
	 */
	public static Individual getRandomIndividual(ArrayList<Individual> pool)
	{
		int id = (int)((float)(pool.size()-1) * Math.random());
		return pool.get(id);
	}
	
	/**
	 * Used to get score for an individual while performing selection.
	 * This takes into account any extra affects like preferring diversity.
	 * @param	config	Program settings
	 * @param	ind		The Individual
	 * @return	Score or rank value.
	 */
	public static double getSelectionScore(JavolverConfig config, Individual ind) {

		// Special handling if we are using the rank method.
		if (config.selectionUseScoreRank==true) {
			double score = (double)ind.rankScore;
			
			if (config.selectionUseDiversityRank==true) {
				score+=(double)ind.rankDiversity*0.5;
			}
			
			return score;
		}
		
		// Otherwise just return the individuals score.
		return ind.getScore();
	
	}
	
	
}
