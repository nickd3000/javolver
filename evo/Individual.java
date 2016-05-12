package evo;


/**
 * @author nick
 *
 */

public abstract class Individual {

	public Chromosome dna = new Chromosome();
	protected boolean processed = false;
	protected double score = 0.0;
	protected int generation = 0;

	
	public Individual()
	{
		dna = new Chromosome();
	}
	
	// Must be provided / overridden.
	abstract public Individual clone();
	abstract public String toString();
	abstract public double calculateScore();
	
	
	public double getScore() {
		if (processed==false) calculateScore();
		return score;
	};
	
	public double setScore(double s) { return score=s; }
	
}
