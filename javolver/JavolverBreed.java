package javolver;

public class JavolverBreed {

	public enum BreedMethod {CROSSOVER, UNIFORM, AVERAGE};
	
	public static Individual breed(JavolverConfig config, Individual g1, Individual g2)
	{
		switch (config.breedMethod) {
		case CROSSOVER:
			return breedCrossover(g1,g2);
		case UNIFORM:
			return breedUniform(g1,g2);
		case AVERAGE:
			return breedAverage(g1,g2);
		default:
			return breedUniform(g1,g2);
		}
	}
	
	/***
	 * Select a pair of genes, mate them and return the new child.
	 * @param	g1	First parent
	 * @param	g2	Second parent
	 * @return		The child
	 */
	public static Individual breedCrossover(Individual g1, Individual g2)
	{
		Individual child = g1.clone(); //proto.clone();
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
	
	/***
	 * Select a pair of genes, mate them and return the new child.
	 * @param	g1	First parent
	 * @param	g2	Second parent
	 * @return		The child
	 */
	public static Individual breedUniform(Individual g1, Individual g2)
	{
		Individual child = g1.clone();
		int dnaSize = g1.dna.getData().size();
		double d1=0,d2=0;

		for (int i=0;i<dnaSize;i++)
		{
			d1 = g1.dna.getDouble(i);
			d2 = g2.dna.getDouble(i);
			
			if (Math.random()<0.5)
				child.dna.getData().set(i,d1);
			else
				child.dna.getData().set(i,d2);

		}
		
		return child;
	}
	
	/**
	 * A breed function that returns the average of the parents - experimental.
	 * @param g1
	 * @param g2
	 * @return
	 */
	public static Individual breedAverage(Individual g1, Individual g2)
	{
		Individual child = g1.clone();//proto.clone();
		int dnaSize = g1.dna.getData().size();
		double d1=0,d2=0;
		
		for (int i=0;i<dnaSize;i++)
		{
			d1 = g1.dna.getDouble(i);
			d2 = g2.dna.getDouble(i);
			child.dna.getData().set(i,(d1+d2)*0.5);
		}
		
		return child;
	}
	
}
