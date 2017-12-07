package javolver;

import java.util.ArrayList;
import java.util.List;

import javolver.breedingstrategy.BreedingStrategy;
import javolver.breedingstrategy.BreedingStrategyAverage;
import javolver.breedingstrategy.BreedingStrategyCrossover;
import javolver.breedingstrategy.BreedingStrategyUniform;
import javolver.selectionstrategy.SelectionStrategy;
import javolver.selectionstrategy.SelectionStrategyRoulette;
import javolver.selectionstrategy.SelectionStrategyTournament;


/**
 * Javolver is a simple engine that processes a pool of individuals using genetic selection.
 * The user must derive a class from the Individual class and supply an object of this derived type to the constructor.
 * 
 * @author	Nick Donnelly (Twitter: @nickd3000)
 * @version	1.0
 * @since	2016-04-01
 */
public class Javolver {

	public JavolverConfig config = new JavolverConfig();
	
	private ArrayList<Individual> genePool = new ArrayList<>();
	private Individual proto; // Copy of type of chromosome we will use.
	private boolean allScored = false;
	
	private BreedingStrategy breedingStrategy = null;
	private SelectionStrategy selectionStrategy = null;
	
	/**
	 * Default constructor.
	 * @param proto		A subclassed object from Individual, from which to clone the other members of the generation.
	 */
	public Javolver(Individual proto)
	{
		this.proto = proto;
		
		// TEMP: Keeping these here while developing new system.
		breedingStrategy = new BreedingStrategyCrossover();
		//breedingStrategy = new BreedingStrategyUniform();
		//breedingStrategy = new BreedingStrategyAverage();
		
		selectionStrategy = new SelectionStrategyTournament();
		//selectionStrategy = new SelectionStrategyRoulette();
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
		// Request that all individuals perform scoring. 
		scoreGenes(genePool);
		
		JavolverRanking.calculateFitnessRank(genePool);
		JavolverRanking.calculateDiversityRank(genePool);
	
		ArrayList<Individual> newGenePool = null;

		newGenePool = new ArrayList<Individual>();
		
		int targetPop = genePool.size();
		
		// Elitism - keep the best individual in the new pool.
		if (config.keepBestIndividualAlive) {
			newGenePool.add(findBestScoringIndividual(genePool));
		}
		
		Individual g1=null,g2=null;
		
		while (newGenePool.size()<targetPop)
		{
			g1=g2=null;
			
			while (g1==g2) {
				g1 = selectionStrategy.select(config, genePool);
				g2 = selectionStrategy.select(config, genePool);
			}

			List<Individual> children = breedingStrategy.breed(config, g1, g2);

			for (Individual child : children) {
				JavolverMutate.mutate(config, child);
				newGenePool.add(child);
			}
			
			
		}
		
		// Copy new pool over main pool.
		genePool = newGenePool;
		allScored = false;

		// Request that all individuals perform scoring. 
		scoreGenes(genePool);
	}
	
	/***
	 * Return a string containing some basic information about the state of the system.
	 * @return		String containing simple report
	 */
	public String report()
	{
		Individual best = findBestScoringIndividual(genePool);
		String retStr = "Pool Size: " + genePool.size() + " Best: " + best.toString();
		return retStr;
	}
	
	/***
	 * Triggers each individual in the pool to calculate it's score.
	 * The sequential or parallel method is used depending on config settings.
	 * @param	pool	ArrayList of individuals to be scored.
	 */
	public void scoreGenes(ArrayList<Individual> pool)
	{
		if (allScored==true) return;
		
		if (config.parallelScoring) {
			scoreGenesParallel(genePool);
		} else {
			scoreGenesSequential(genePool);
		}
		
		allScored=true;
	}
	
	public void scoreGenesSequential(ArrayList<Individual> pool)
	{
		for (Individual gene : pool)
		{
			gene.getScore();
		}
	}
	
	public void scoreGenesParallel(ArrayList<Individual> pool) {
		pool.parallelStream().unordered().forEach(Individual::getScore);
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

	
	
	

	

}
