package com.physmo.javolver.selectionstrategy;

import java.util.List;

import com.physmo.javolver.Individual;

public class SelectionStrategyRoulette implements SelectionStrategy {

	@Override
	public Individual select(List<Individual> pool) {
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

}
