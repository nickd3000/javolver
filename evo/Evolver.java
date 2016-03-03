package evo;

import java.util.ArrayList;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;


// TODO: add randomize function to igene to make things more explicit
// TODO: add settings to evolver to control mutation rate, breed rate etc.



public class Evolver {

	private ArrayList<IGene> genePool = null;
	private IGene proto; // Copy of type of gene we will use.

	
	public Evolver(IGene proto)
	{
		this.proto = proto;
		init();
	}
	
	public void init()
	{
		genePool = new ArrayList<IGene>();
	}
	
	
	/**
	 * Add a number of randomly initialized genes to the population.
	 */
	public void addRandomPopulation(int count)
	{
		IGene n = null;
		
		for (int i=0; i<count; i++)
		{
			n = proto.newInstance();
			n.init();
			n.randomise();
			genePool.add(n);
		}
	}
	
	
	public void doOneCycle()
	{
		scoreGenes();
		//cullHalf();
	
		ArrayList<IGene> newGenePool = null;
		newGenePool = new ArrayList<IGene>();
		
		// Elitism.
		newGenePool.add(findBestScoringGene());
		
		for (int i=0;i<genePool.size()-1;i++)
		{
			IGene g1 = tournamentSelection(genePool, 100);
			IGene g2 = tournamentSelection(genePool, 100);
			newGenePool.add(breed(g1,g2));
		}
		
		// Copy new pool over main pool.
		genePool = newGenePool;
		
		
		// for (int i=0;i<(getGenePool().size()/10)+3;i++) breed();
		// for (int i=0;i<(genePool.size()/10)+3;i++) breed();
		
		
		// Introduce new, random genes.
		/*
		for (int i=0;i<10;i++)
		{
			if (getGenePool().size()>0)
			{
				IGene best = getGenePool().get(0);
				best = best.createChild(best, best); // Looks weird but we are going to randomise it.
				best.init();
				getGenePool().add(best);
			}
		}
		*/
		
		// Clone the best one.
		/*
		IGene best = findBestScoringGene();
		if (best!=null)
		{
			IGene clone = best.createChild(best, best); // Looks weird but we are going to randomise it.
			getGenePool().add(clone);
			clone = best.createChild(best, best); // Looks weird but we are going to randomise it.
			getGenePool().add(clone);
			clone = best.createChild(best, best); // Looks weird but we are going to randomise it.
			getGenePool().add(clone);
		}
		*/
		
		//scoreGenes();
		
		//killOld(50);
		//while (getGenePool().size()>100) { 
		//cullHalf();
		//cullHalf();
		//reduceSetTo(500);
		
	}
	
	public void report()
	{
		IGene best = findBestScoringGene();
		
		System.out.printf("\nNum Genes: %d   ", getGenePool().size());
		System.out.printf("%s\n",best.asString());
	}
	
	public void scoreGenes()
	{
		for (IGene gene : getGenePool())
		{
			gene.calculateScore();
			
		}
	}
	
	
	/***
	 * Select a pair of genes, mate them and add the children to the pool.
	 * 
	 */
	public IGene breed(IGene g1, IGene g2)
	{
		IGene child = g1.createChild(g1, g2);
		child.mutate();
		return child;
	}
	
	/***
	 * Select a pair of genes, mate them and add the children to the pool.
	 * 
	 */
	public void breed()
	{
		IGene g1 = null, g2 = null;
		
		// If we are not allowed to mate these two genes, try to find a pair we can (don't search for long)
		for (int i=0;i<100;i++)
		{
			g1 = rouletteSelection();
			g2 = rouletteSelection();
			
			if (g1==null || g2==null) continue;
			
			if (g1.canMate(g2, true)) break;
		}
		
		if (g1==null || g2==null) return;
		
		if (g1.canMate(g2, true)==false) return;
		
		IGene child = g1.createChild(g1, g2);
		child.mutate();
		addGene(child);
	}
	
	/**
	 * Return the fittest individual from a pool, from a random selection.
	 */
	public IGene tournamentSelection(ArrayList<IGene> pool, int tournamentSize)
	{
		int poolSize = pool.size();
		float maxScore = -1000;
		IGene currentWinner = null;
		
		for (int i=0;i<tournamentSize;i++)
		{
			IGene contender = getRandomIndividual(pool);
			if (contender.getScore()>maxScore)
			{
				maxScore = contender.getScore();
				currentWinner = contender;
			}
		}
		
		return currentWinner;
	}
	
	public IGene getRandomIndividual(ArrayList<IGene> pool)
	{
		int id = (int)((float)(pool.size()-1) * Math.random());
		return pool.get(id);
	}
	
	public void addGene(IGene newGene)
	{
		getGenePool().add(newGene);
	}
	
	public IGene findBestScoringGene()
	{
		float highestScore = 0.0f;
		IGene highestGene = getGenePool().get(0);
		for (IGene gene : getGenePool())
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
		ArrayList<IGene> NewGenePool = null;
		NewGenePool = new ArrayList<IGene>();
		
		for (int i=0;i<amount;i++)
		{
			//int id = (int)((float)(getGenePool().size()-1) * Math.random());
			//IGene cpy = getGenePool().get(id);
			
			IGene cpy = this.rouletteSelection();
			
			NewGenePool.add(cpy);
		}
		
		// Switch over.
		setGenePool(NewGenePool);
	}
	

	public void killOld(int ageLimit)
	{
		ArrayList<IGene> NewGenePool = null;
		NewGenePool = new ArrayList<IGene>();
		for (IGene gene : getGenePool())
		{
			if (gene.getAge()<ageLimit)
				NewGenePool.add(gene);
		}
		// Switch over.
		setGenePool(NewGenePool);
	}
	
	public void cullHalf()
	{
		if (getGenePool().size()<20) return; // Don't do it if we risk extinction. 
		
		float allScores = 0.0f;
		for (IGene gene : getGenePool())
		{
			allScores += gene.getScore();
		}
		float avScore = allScores / (float)getGenePool().size();
		
		ArrayList<IGene> NewGenePool = null;
		NewGenePool = new ArrayList<IGene>();
		
		int numCopied = 0;
		
		for (IGene gene : getGenePool())
		{
			if (gene.getScore() >= avScore)
			{
				NewGenePool.add(gene);
				numCopied++;
			}
		}
		
		if (numCopied>(int)((float)getGenePool().size()*0.95f) && numCopied>500)
		{
			NewGenePool.clear();
			numCopied=0;
			for (IGene gene : getGenePool())
			{
				if (Math.random()>0.5)
				{
					NewGenePool.add(gene);
					numCopied++;
				}
			}
		}
		
		if (NewGenePool.size()<10) return;
		
		// Switch over.
		setGenePool(NewGenePool);
	}
	
	/**
	 * Select a random gene from the pool weighted towards the fittest individuals.
	 * @return
	 */
	public IGene rouletteSelection()
	{
	    float totalScore = 0;
	    float runningScore = 0;
	    for (IGene g : genePool)
	    {
	        totalScore += g.getScore();
	    }

	    float rnd = (float) (Math.random() * totalScore);

	    for (IGene g : genePool)
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
	
//	static ArrayList<Gene> GenePool = null;
/*
	public static void main(String[] args) {
		
		GenePool = new ArrayList<Gene>();
		
		setUpInitialPopulation(100);
		scorePopulation();
		
		System.out.println("Test");
		System.out.println(evaluateString("2+3+4+5",1.0f));
	}
	*/
	
	/*
	public static void setUpInitialPopulation(int count)
	{
		Gene g = null;
		for (int i=0;i<count;i++)
		{
			g = new Gene();
			for (int j=0;j<20;j++)
			{
				g.sequence+=getRandomCharacter();
			}
			GenePool.add(g);
		}
	}
	*/

	/*
	public static void scorePopulation()
	{
		float evalResult = 0f;
		float targetResult = 0f;
		float score=0f;
		for (Gene g : GenePool)
		{
			score=0f;
			for (int x=0;x<20;x++)
			{
				evalResult = (float) evaluateString(g.sequence,(float)x);
				targetResult = mysteryFunction((float)x);
				score+=1f / Math.abs(evalResult-targetResult);
				g.fitness = score;
			}
		}
	}
	*/
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Double(result);
	}

	public ArrayList<IGene> getGenePool() {
		return this.genePool;
	}

	public void setGenePool(ArrayList<IGene> genePool) {
		this.genePool = genePool;
	}
	
}
