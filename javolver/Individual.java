package javolver;


/**
 * @author nick
 * This is the base class that should be used to create custom individual types to feed into the evolver.
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
	
	/**
	 * Return a new copy of the subclass.
	 */
	abstract public Individual clone();
	
	/**
	 * Return simple string representation of the individual.
	 */
	abstract public String toString();
	
	/**
	 * Calculate the score of the individual.
	 */
	abstract public double calculateScore();
	
	
	// Provided functionality.
	public double getScore() {
		if (processed==false) {
			score = calculateScore();
			processed = true;
		}
		
		return score;
	};
	
	public double setScore(double s) { return score=s; }
	
}
