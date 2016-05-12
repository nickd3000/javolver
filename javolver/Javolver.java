package javolver;

import java.util.ArrayList;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;


// TODO: add randomize function to igene to make things more explicit
// TODO: add settings to evolver to control mutation rate, breed rate etc.



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
		return findBestScoringGene(genePool).getScore();
	}
	
	public void runUntilMaximum() {
		
		double previousBestScore = 0;
		int runOfNoImprovements = 0;
		int i=0;
		for (i=0;i<100;i++) {
			doOneCycle();
			double s = getBestScore();
			double imp = previousBestScore-s;
			previousBestScore = s;
			if (imp<0.1) runOfNoImprovements++;
			else runOfNoImprovements=0;
			if (runOfNoImprovements>50) break;
		}
		System.out.println("Iterations: " + i + " result:" + findBestScoringGene(genePool).toString());
	}
	
	
	/**
	 * Add a number of randomly initialized genes to the population, until it reaches specified size.
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
	
	
	public void doOneCycle()
	{
		scoreGenes(genePool);
		//cullHalf();
	
		ArrayList<Individual> newGenePool = null;
		ArrayList<Individual> brood = null;
		newGenePool = new ArrayList<Individual>();
		brood = new ArrayList<Individual>();
		
		// Elitism - keep the best individual in the new pool.
		newGenePool.add(findBestScoringGene(genePool));
		
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
			newGenePool.add(findBestScoringGene(brood));
		}
		
		// Copy new pool over main pool.
		genePool = newGenePool;

	}
	
	public void report()
	{
		Individual best = findBestScoringGene(genePool);
		
		System.out.printf("\nNum Genes: %d   ", getGenePool().size());
		System.out.printf("%s\n",best.toString());
	}
	
	public void scoreGenes(ArrayList<Individual> pool)
	{
		for (Individual gene : pool)
		{
			gene.getScore();
		}
	}
	
	
	/***
	 * Select a pair of genes, mate them and add the children to the pool.
	 * 
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
	 * @param	pool				The pool of genes to select from.
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
	 * @return
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
	
	public Individual getRandomIndividual(ArrayList<Individual> pool)
	{
		int id = (int)((float)(pool.size()-1) * Math.random());
		return pool.get(id);
	}
	
	public void addGene(Individual newGene)
	{
		getGenePool().add(newGene);
	}
	
	public Individual findBestScoringGene(ArrayList<Individual> pool)
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
	
	
	public void reduceSetTo(int amount)
	{
		if (getGenePool().size()<amount) return;
		ArrayList<Individual> NewGenePool = null;
		NewGenePool = new ArrayList<Individual>();
		
		for (int i=0;i<amount;i++)
		{
			//int id = (int)((float)(getGenePool().size()-1) * Math.random());
			//IGene cpy = getGenePool().get(id);
			
			Individual cpy = this.rouletteSelection(genePool);
			
			if (cpy!=null) NewGenePool.add(cpy);
		}
		
		// Switch over.
		setGenePool(NewGenePool);
	}
	
	public float mysteryFunction(float x)
	{
		return x*x;
	}
	
	// Not random, selected from list of math characters.
	public char getRandomCharacter()
	{
		String base = "0123456789./+-*()x      ";
		return base.charAt((int)(Math.random()*base.length()));
	}
	
	public double evaluateString(String str, float x)
	{
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String result="";
		try {
			result = engine.eval(str).toString();
		} catch (ScriptException e) {
			
			e.printStackTrace();
		}
		
		return new Double(result);
	}

	public ArrayList<Individual> getGenePool() {
		return this.genePool;
	}

	public void setGenePool(ArrayList<Individual> genePool) {
		this.genePool = genePool;
	}
	
	
	

	
}
