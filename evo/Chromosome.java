package evo;

import java.util.ArrayList;

import test.GeneProgram;

public class Chromosome<T> {

	private ArrayList<T> dna = null;
	
	public Chromosome()
	{
		dna = new ArrayList<T>();
	}
	
	public ArrayList<T> getDna() { return dna; }
	
	public void add(T newItem)
	{
		dna.add(newItem);
	}
	
	/**
	 * Return a new strand of DNA based on this and the passed in other parent.
	 * At the moment, assumes both dna strands are the same size.
	 */
	public ArrayList<T> crossover(ArrayList<T> otherChromosome, int numCrossoverPoints)
	{
		ArrayList<T> DNA1 = this.dna;
		ArrayList<T> DNA2 = otherChromosome;
		ArrayList<T> DNA3 = new ArrayList<T>();
		boolean[] crossPoints = new boolean [DNA1.size()];
		
		for (int i=0;i<crossPoints.length;i++) crossPoints[i]=false;
		
		// Calculate requested number of crossover points.
		for (int i=0;i<numCrossoverPoints;i++)
		{
			crossPoints[(int)(Math.random()*(double)DNA1.size())]=true;
		}
		
		// Select a random starting side.
		boolean side = Math.random()<0.5?false:true;
		
		for (int i=0;i<DNA1.size();i++)
		{	
			if (crossPoints[i]==true) {
				if (side==true) side=false;
				else side = true;
			}
				
			if (side)	DNA3.add(i, DNA1.get(i));
			else			DNA3.add(i, DNA2.get(i));
		}
		
		//DNA3 = new String(chars);
		
		//GeneProgram child = new GeneProgram();
		//child.dna = DNA3;
		
		//child.generation = Math.max(p1.getGeneration(), p2.getGeneration()) + 1;
				
		return DNA3;
		//return null;
	}
	
}
