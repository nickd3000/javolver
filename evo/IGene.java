package evo;


/**
 * @author nick
 *
 */
/* NOTES:
 * This is an interface used to define a set of operations that can be applied to a gene,
 * the format of the genetic material
 * scoring,
 * mutation
 * serialisation
 */
public interface IGene {

	public void init();
	//public void copyFrom(IGene parent);
	
	public IGene	newInstance();
	public void		mutate();
	public void		calculateScore();
	public float	getScore();
	public IGene	createChild(IGene p1, IGene p2);
	public int		getAge();
	public String	asString(); 		// Return string representation of DNA.
	public boolean	canMate(IGene partner, boolean excludeSimilar); // Return true if two genes can mate.
	public int		getGeneration(); 	// Return the generation of this gene.
	public void		randomise(); 		// Used to initialise dna to random state.
	
	
	public int age = 0;
	public int numberOfTestsPerformed = 0;
	public float scoreAccumulator = 0.0f; // Used when scoring the same gene multiple times.
}
