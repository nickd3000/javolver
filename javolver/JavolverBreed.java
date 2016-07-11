package javolver;

/**
 * @author nick
 *	Various breeding strategies that take two parent individuals and return
 *	a child derived from them.
 */
public class JavolverBreed {

	public enum BreedMethod {CROSSOVER, UNIFORM, AVERAGE};
	
	/**
	 * Breed two parents and return the resulting child.
	 * The breeding method is selected from the config structure.
	 * @param config	Program settings.
	 * @param	parent1	First parent
	 * @param	parent2	Second parent
	 * @return			child Individual
	 */
	public static Individual breed(JavolverConfig config, Individual parent1, Individual parent2)
	{
		switch (config.breedMethod) {
		case CROSSOVER:
			return breedCrossover(parent1,parent2);
		case UNIFORM:
			return breedUniform(parent1,parent2);
		case AVERAGE:
			return breedAverage(parent1,parent2);
		default:
			return breedUniform(parent1,parent2);
		}
	}
	
	/***
	 * Select a pair of genes, mate them and return the new child.
	 * Using crossover method, there is one crossover point.
	 * @param	parent1	First parent
	 * @param	parent2	Second parent
	 * @return		The child
	 */
	public static Individual breedCrossover(Individual parent1, Individual parent2)
	{
		Individual child = parent1.clone(); //proto.clone();
		int dnaSize = parent1.dna.getData().size();
		double d1=0,d2=0;
				
		int crossover=(int)(Math.random()*(double)dnaSize);
		
		for (int i=0;i<dnaSize;i++)
		{
			d1 = parent1.dna.getDouble(i);
			d2 = parent2.dna.getDouble(i);
			
			if (i<crossover)
				child.dna.getData().set(i,d1);
			else
				child.dna.getData().set(i,d2);

		}
		
		return child;
	}
	
	/***
	 * Select a pair of genes, mate them and return the new child.
	 * Using uniform method, each element is taken from a random parent.
	 * @param	parent1	First parent
	 * @param	parent2	Second parent
	 * @return		The child
	 */
	public static Individual breedUniform(Individual parent1, Individual parent2)
	{
		Individual child = parent1.clone();
		int dnaSize = parent1.dna.getData().size();
		double d1=0,d2=0;

		for (int i=0;i<dnaSize;i++)
		{
			d1 = parent1.dna.getDouble(i);
			d2 = parent2.dna.getDouble(i);
			
			if (Math.random()<0.5)
				child.dna.getData().set(i,d1);
			else
				child.dna.getData().set(i,d2);

		}
		
		return child;
	}
	
	/**
	 * A breed function that returns the average of the parents - experimental.
	 * Elements are taken as the average of both parents.
	 * @param	parent1	First parent
	 * @param	parent2	Second parent
	 * @return	The child
	 */
	public static Individual breedAverage(Individual parent1, Individual parent2)
	{
		Individual child = parent1.clone();//proto.clone();
		int dnaSize = parent1.dna.getData().size();
		double d1=0,d2=0;
		
		for (int i=0;i<dnaSize;i++)
		{
			d1 = parent1.dna.getDouble(i);
			d2 = parent2.dna.getDouble(i);
			child.dna.getData().set(i,(d1+d2)*0.5);
		}
		
		return child;
	}
	
}
