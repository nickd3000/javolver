package javolver.selectionstrategy;

import java.util.List;

import javolver.Individual;
import javolver.JavolverConfig;

public class SelectionStrategyRoulette implements SelectionStrategy {

	@Override
	public Individual select(JavolverConfig config, List<Individual> pool) {
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
