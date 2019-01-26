package javolver;

import java.util.ArrayList;
import java.util.List;

import javolver.breedingstrategy.BreedingStrategy;
import javolver.breedingstrategy.BreedingStrategyAverage;
import javolver.breedingstrategy.BreedingStrategyCrossover;
import javolver.breedingstrategy.BreedingStrategyUniform;
import javolver.mutationstrategy.MutationStrategy;
import javolver.mutationstrategy.MutationStrategySimple;
import javolver.mutationstrategy.MutationStrategySingle;
import javolver.mutationstrategy.MutationStrategySwap;
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
// TODO: make list types use interface type instead of ArrayList etc.

public class Javolver {

	// Keep the best individual alive between generations.
	public boolean keepBestIndividualAlive = false;
	
 // Use multi-threading for the scoring process.
 // Set this to true if your {@link Individual#calculateScore()} method
 // is expensive to run and may benefit from parallelization.
	public boolean parallelScoring = false;
	
	
	public Javolver keepBestIndividualAlive(boolean val) {
		this.keepBestIndividualAlive = val;
		return this;
	}
	public Javolver parallelScoring(boolean val) {
		this.parallelScoring = val;
		return this;
	}
	
	
	private ArrayList<Individual> genePool = new ArrayList<>(); 
	private Individual proto; // Copy of type of chromosome we will use.
	private boolean allScored = false;
	
	private BreedingStrategy breedingStrategy = null;
	private SelectionStrategy selectionStrategy = null;
	private List<MutationStrategy> mutationStrategies = new ArrayList<MutationStrategy>();
	
	public ArrayList<Individual> getPool() {
		return genePool;
	}
	
	/**
	 * Default constructor.
	 * @param proto		A subclassed object from Individual, from which to clone the other members of the generation.
	 */
	public Javolver(Individual proto)
	{
		this.proto = proto;
		
	}
	
	public Javolver setDefaultStrategies() {
		
		//breedingStrategy = new BreedingStrategyCrossover();
		breedingStrategy = new BreedingStrategyUniform();
		//breedingStrategy = new BreedingStrategyAverage();
		
		selectionStrategy = new SelectionStrategyTournament(0.15);
		//selectionStrategy = new SelectionStrategyRoulette();
		
		//mutationStrategies.add(new MutationStrategySimple(0.01, 0.022));
		
		mutationStrategies.add(new MutationStrategySimple(0.01, 0.012));
		//mutationStrategies.add(new MutationStrategySingle(0.1));
		//mutationStrategies.add(new MutationStrategySwap(0.1, 5));
		
		return this;
	}
	
	public Javolver setBreedingStrategy(BreedingStrategy strategy) {
		this.breedingStrategy = strategy;
		return this;
	}
	public Javolver setSelectionStrategy(SelectionStrategy strategy) {
		this.selectionStrategy = strategy;
		return this;
	}
	public Javolver addMutationStrategy(MutationStrategy strategy) {
		mutationStrategies.add(strategy);
		return this;
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
	
	// testing out a simple descent based approach.
	public void doOneCycleOfDescent() {
		// Request that all individuals perform scoring. 
		scoreGenes(genePool);
		
		JavolverRanking.calculateFitnessRank(genePool);
		JavolverRanking.calculateDiversityRank(genePool);
		
		Individual bestGuy = findBestScoringIndividual(genePool);
		Individual child = bestGuy.clone();
		
		for (int i=0;i<child.dna.getSize();i++) {
			child.dna.set(i,bestGuy.dna.getDouble(i));
		}
		
		double score1 = bestGuy.calculateScore();
		
		MutationStrategy ms = new MutationStrategySimple(0.1,0.01);
		MutationStrategy ms2 = new MutationStrategySimple(0.1,0.1);
		MutationStrategy ms3 = new MutationStrategySimple(1.0,1.0);
		
		
		ms.mutate(child);
		ms2.mutate(child);
		if (Math.random()>0.99) ms3.mutate(child);
		
		double score2 = child.calculateScore();
		
		//System.out.println("s1:"+score1+"   s2:"+score2 + "  poolSize:"+genePool.size());
		
		double delta = score2-score1; 
		if (delta>0.0001) {
			//genePool.add(child);
			genePool.set(0, child);
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
		if (keepBestIndividualAlive) {
			newGenePool.add(findBestScoringIndividual(genePool));
		}
		
		Individual g1=null,g2=null;
		
		while (newGenePool.size()<targetPop)
		{
			g1=g2=null;
			
			// Select parents
			for (int ii=0;ii<100;ii++) {
				while (g1==g2) {
					g1 = selectionStrategy.select(genePool);
					g2 = selectionStrategy.select(genePool);
				}
				double diff = g1.getDifference(g1);
				//if (diff!=0 && diff>0.001 && diff<0.3) break;
				//g1=g2;
			}

			// Breed
			List<Individual> children = breedingStrategy.breed( g1, g2);

			// Mutate children.
			for (Individual child : children) {
				for (MutationStrategy ms : mutationStrategies) {
					ms.mutate(child);
				}
			}
			
			// Add children to new gene pool.
			for (Individual child : children) {
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
		
		if (parallelScoring) {
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
