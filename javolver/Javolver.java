package javolver;

import java.util.ArrayList;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;


// TODO: add randomize function to igene to make things more explicit
// TODO: add settings to evolver to control mutation rate, breed rate etc.


/**
 * Javolver is a simple engine that processes a pool of individuals using genetic selection.
 * The user must derive a class from the Individual class and supply an object of this derived type to the constructor.
 * 
 * @author	Nick Donnelly (Twitter: @nickd3000)
 * @version	1.0
 * @since	2016-04-01
 */
public class Javolver {

	
	private ArrayList<Individual> genePool = new ArrayList<>();
	private Individual proto; // Copy of type of chromosome we will use.

	
	public Javolver(Individual proto)
	{
		this.proto = proto;
		//init();
	}
	
	public void init()
	{
		//genePool = new ArrayList<Individual>();
	}
	
	public double getBestScore() {
		return findBestScoringIndividual(genePool).getScore();
	}
	
	
	/**
	 * experimental function to run the system until improvement slows.
	 */
	public void runUntilMaximum() {
		
		double previousBestScore = 0;
		int runOfNoImprovements = 0;
		int i=0;
		for (i=0;i<100;i++) {
			doOneCycle();
			double s = getBestScore();
			double imp = Math.abs(previousBestScore-s);
			previousBestScore = s;
			if (imp<0.001) runOfNoImprovements++;
			else runOfNoImprovements=0;
			if (runOfNoImprovements>50) break;
		}
		System.out.println("Iterations: " + i + " result:" + findBestScoringIndividual(genePool).toString());
	}
	
	
	/**
	 * Add a number of randomly initialized genes to the population, until it reaches specified size.
	 * 
	 * @param	targetCount	The target number of individuals that the population will reach.
	 */
	public void increasePopulation(int targetCount)
	{
		Individual n = null;
		int target = targetCount - genePool.size();
		if (target<1) return;
		
		for (int i=0; i<target; i++)
		{
			n = proto.clone();
			
			genePool.add(n);
		}
	}
	
	/**
	 * The main function that does most of the work to evolve the system.<br>
	 * 1. All individuals scoring mechanisms get called.<br>
	 * 2. The best scoring individual is automatically moved to the next generation.<br>
	 * 3. A new generation of individuals is created by breeding selected member from the current generation.<br>
	 */
	public void doOneCycle()
	{
		scoreGenes(genePool);
		//cullHalf();
	
		ArrayList<Individual> newGenePool = null;
		ArrayList<Individual> brood = null;
		newGenePool = new ArrayList<Individual>();
		brood = new ArrayList<Individual>();
		
		// Elitism - keep the best individual in the new pool.
		newGenePool.add(findBestScoringIndividual(genePool));
		
		for (int i=0;i<genePool.size()-1;i++)
		{
			//Individual g1 = tournamentSelection(genePool, 0.3);
			//Individual g2 = tournamentSelection(genePool, 0.3);
			
			Individual g1 = rouletteSelection(genePool);
			Individual g2 = rouletteSelection(genePool);
			
			brood.clear();
			for (int j=0;j<4;j++)
			{
				brood.add(breed(g1,g2));
			}
			
			scoreGenes(brood);
			newGenePool.add(findBestScoringIndividual(brood));
		}
		
		// Copy new pool over main pool.
		genePool = newGenePool;

	}
	
	/***
	 * Return a string containing some basic information about the state of the system.
	 * @return		String containing simple report
	 */
	public String report()
	{
		Individual best = findBestScoringIndividual(genePool);
		String retStr = "\nNum Genes: " + genePool.size() + " Best: " + best.toString();
		return retStr;
	}
	
	/***
	 * Triggers each individual in the pool to calculate it's score.
	 * @param	pool	ArrayList of individuals to be scored.
	 */
	public void scoreGenes(ArrayList<Individual> pool)
	{
		for (Individual gene : pool)
		{
			gene.getScore();
		}
	}
	
	
	/***
	 * Select a pair of genes, mate them and return the new child.
	 * @param	g1	First parent
	 * @param	g2	Second parent
	 * @return		The child
	 */
	public Individual breed(Individual g1, Individual g2)
	{
		Individual child = proto.clone();
		int dnaSize = g1.dna.getData().size();
		double d1=0,d2=0;
		
		double jiggle = (Math.random()-0.5) * 0.10;
		
		for (int i=0;i<dnaSize;i++)
		{
			d1 = g1.dna.getDouble(i);
			d2 = g2.dna.getDouble(i);
			
			if (Math.random()<0.5)
				child.dna.getData().set(i,d1+jiggle);
			else
				child.dna.getData().set(i,d2+jiggle);
			
			// Mutate
			//child.dna.set(i, child.mutate(child.dna.getDna().get(i)));
		}
		
		
		return child;
	}
	
	
	
	/**
	 * Return the fittest individual from a pool, from a random selection.
	 * @param	pool			The pool of individuals to select from.
	 * @param	tournamentSize	0..1 value, percentage of pool to include in tournament
	 * @return					The winner of the tournament
	 */
	public Individual tournamentSelection(ArrayList<Individual> pool, double tournamentSize)
	{
		int poolSize = pool.size();
		double maxScore = -1000;
		Individual currentWinner = null;
		
		int tSize = (int)(tournamentSize*(double)poolSize);
		
		for (int i=0;i<tSize;i++)
		{
			Individual contender = getRandomIndividual(pool);
			if (contender.getScore()>maxScore)
			{
				maxScore = contender.getScore();
				currentWinner = contender;
			}
		}
		
		return currentWinner;
	}
	
	/**
	 * Select a random gene from the pool weighted towards the fittest individuals.
	 * @param 	pool	The pool of individuals to select from.
	 * @return			The winner of the selection process.
	 */
	public Individual rouletteSelection(ArrayList<Individual> pool)
	{
	    float totalScore = 0;
	    float runningScore = 0;
	    for (Individual g : pool)
	    {
	        totalScore += g.getScore();
	    }

	    float rnd = (float) (Math.random() * totalScore);

	    for (Individual g : pool)
	    {   
	        if (    rnd>=runningScore &&
	                rnd<=runningScore+g.getScore())
	        {
	            return g;
	        }
	        runningScore+=g.getScore();
	    }

	    return null;
	}
	
	/***
	 * Return an individual randomly selected from the supplied list of individuals.
	 * @param	pool	The pool of individuals to select from.
	 * @return			Random member of the supplied list.
	 */
	public Individual getRandomIndividual(ArrayList<Individual> pool)
	{
		int id = (int)((float)(pool.size()-1) * Math.random());
		return pool.get(id);
	}
	

	
	/***
	 * Search the supplied pool of individuals and return the highest scoring one.
	 * @param	pool	The pool of individuals to select from.
	 * @return			Highest scoring member of the supplied list.
	 */
	public Individual findBestScoringIndividual(ArrayList<Individual> pool)
	{
		double highestScore = 0.0f;
		Individual highestGene = pool.get(0);
		for (Individual gene : pool)
		{
			if (gene.getScore()>highestScore)
			{
				highestGene = gene;
				highestScore = gene.getScore();
			}
		}
		return highestGene;
	}
	
	
	
	

	
}
