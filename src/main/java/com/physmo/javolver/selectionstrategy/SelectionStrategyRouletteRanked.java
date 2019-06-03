package com.physmo.javolver.selectionstrategy;

import com.physmo.javolver.Individual;

import java.util.List;

public class SelectionStrategyRouletteRanked implements SelectionStrategy {

	@Override
	public Individual select(List<Individual> pool) {
	    float totalScore = 0;
	    float runningScore = 0;
	    for (Individual g : pool)
	    {
	        totalScore += g.getRankScore();
	    }

	    float rnd = (float) (Math.random() * totalScore);

	    for (Individual g : pool)
	    {   
	        if (    rnd>=runningScore &&
	                rnd<=runningScore+g.getRankScore())
	        {
	            return g;
	        }
	        runningScore+=g.getRankScore();
	    }

	    return null;
	}

}
