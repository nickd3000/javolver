package javolver;


/**
 * This is the base class that should be used to create custom individual types to feed into the evolver.
 * 
 * @author	Nick Donnelly (Twitter: @nickd3000)
 * @version	1.0
 * @since	2016-04-01
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
	 * @return		Double value representing the score of the individual. Higher is better.
	 */
	abstract public double calculateScore();
	
	
	/**
	 * Return score of this individual. If the individual has not yet been processed, call calculateScore() first.
	 * @return		Double value representing the score of the individual. Higher is better.
	 */
	public double getScore() {
		if (processed==false) {
			score = calculateScore();
			processed = true;
		}
		
		return score;
	};
	
	/**
	 * Sets the individuals score.
	 * @param	s 	The score.
	 * @return		The score (pass through).
	 */
	public double setScore(double s) { return score=s; }
	
}
