package javolver;

import java.util.ArrayList;



/**
 * Javolver is a simple engine that processes a pool of individuals using genetic selection.
 * The user must derive a class from the Individual class and supply an object of this derived type to the constructor.
 * 
 * @author	Nick Donnelly (Twitter: @nickd3000)
 * @version	1.0
 * @since	2016-04-01
 */
public class Javolver {

	/**
	 * @author nick
	 *
	 */
	public enum SELECTION_TYPE {tournament,  roulette};
	
	SELECTION_TYPE	selectionType = SELECTION_TYPE.tournament;
	double			selectionRange = 0.2;
	double			mutationAmount = 0.10;
	int				mutationCount = 2;
	boolean			keepBestIndividualAlive = false;
	
	private			ArrayList<Individual> genePool = new ArrayList<>();
	private			Individual proto; // Copy of type of chromosome we will use.

	
	/**
	 * Default constructor.
	 * @param proto		A subclassed object from Individual, from which to clone the other members of the generation.
	 */
	public Javolver(Individual proto)
	{
		this.proto = proto;
	}
	
	/**
	 * Create Javolver object with prototype individual and set the population size.
	 * @param proto				A subclassed object from Individual, from which to clone the other members of the generation.
	 * @param populationSize	Required population size.
	 */
	public Javolver(Individual proto, int populationSize) {
		this(proto);
		increasePopulation(populationSize);
	}
	
	
	/**
	 * Find the best score of any individual in the current generation.
	 * @return	The best score of any individual in the current generation.
	 */
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
	 * <br>
	 * The size of the new pool will match the previous generation population.
	 */
	public void doOneCycle()
	{
		scoreGenes(genePool);
		//cullHalf();
	
		ArrayList<Individual> newGenePool = null;
		ArrayList<Individual> brood = null;
		newGenePool = new ArrayList<Individual>();
		brood = new ArrayList<Individual>();
		
		int targetPop = genePool.size();
		
		// Elitism - keep the best individual in the new pool.
		if (keepBestIndividualAlive) {
			newGenePool.add(findBestScoringIndividual(genePool));
		}
		
		Individual g1=null,g2=null;
		
		while (newGenePool.size()<targetPop)
		{
			g1=g2=null;
			
			while (g1==g2) {
				if (selectionType==SELECTION_TYPE.tournament) {
					g1 = tournamentSelection(genePool, selectionRange);
					g2 = tournamentSelection(genePool, selectionRange);
				}
				else if (selectionType==SELECTION_TYPE.roulette) {
					g1 = rouletteSelection(genePool);
					g2 = rouletteSelection(genePool);
			}} 

			Individual child = breed(g1,g2); 
			mutate(child,mutationCount,mutationAmount);
			newGenePool.add(child);
			
	
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
				
		int crossover=(int)(Math.random()*(double)dnaSize);
		
		for (int i=0;i<dnaSize;i++)
		{
			d1 = g1.dna.getDouble(i);
			d2 = g2.dna.getDouble(i);
			
			if (i<crossover)
				child.dna.getData().set(i,d1);
			else
				child.dna.getData().set(i,d2);

		}
		
		return child;
	}
	
	/**
	 * @param ind		The individual to mutate.
	 * @param count		Number of DNA elements to mutate. 
	 * @param amount	Amount to mutate by.
	 */
	public void mutate(Individual ind, int count, double amount) {
		double jiggle = 0, value = 0;
		int index = 0;
		for (int i=0;i<count;i++) {
			index = (int)((double)ind.dna.data.size()*Math.random());
			jiggle = (Math.random()-0.5) * amount * 2.0;
			value = ind.dna.getDouble(index);
			ind.dna.set(index, value+jiggle);
		}
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
		if (tSize<2) tSize=2;
		
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
		if (pool==null) pool = genePool;
		
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

	/**
	 * @return the selectionType
	 */
	public SELECTION_TYPE getSelectionType() {
		return selectionType;
	}

	/**
	 * @param selectionType the selectionType to set
	 */
	public void setSelectionType(SELECTION_TYPE selectionType) {
		this.selectionType = selectionType;
	}

	/**
	 * @return the mutationAmount
	 */
	public double getMutationAmount() {
		return mutationAmount;
	}

	/**
	 * The mutation amount is the maximum amount that a DNA element can be mutated by in a mutation.
	 * @param mutationAmount the mutationAmount to set
	 */
	public void setMutationAmount(double mutationAmount) {
		this.mutationAmount = mutationAmount;
	}

	/**
	 * @return the mutationCount
	 */
	public int getMutationCount() {
		return mutationCount;
	}

	/**
	 * @param mutationCount the mutationCount to set
	 */
	public void setMutationCount(int mutationCount) {
		this.mutationCount = mutationCount;
	}

	/**
	 * @return the keepBestIndividualAlive
	 */
	public boolean isKeepBestIndividualAlive() {
		return keepBestIndividualAlive;
	}

	/**
	 * @param keepBestIndividualAlive the keepBestIndividualAlive to set
	 */
	public void setKeepBestIndividualAlive(boolean keepBestIndividualAlive) {
		this.keepBestIndividualAlive = keepBestIndividualAlive;
	}

	/**
	 * @return the selectionRange
	 */
	public double getSelectionRange() {
		return selectionRange;
	}

	/**
	 * @param selectionRange the selectionRange to set
	 */
	public void setSelectionRange(double selectionRange) {
		this.selectionRange = selectionRange;
	}
		
}
