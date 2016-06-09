package javolver;

import java.util.ArrayList;

/**
 * @author nick
 *	Chromosome is a simple list of double values (0..1), that supports some general functionality.
 *	Values will be mapped to the required data ranges.
 */
public class Chromosome {

	ArrayList<Double> data; //= new ArrayList<Double>(); 
	
	
	public Chromosome() {
		data = new ArrayList<Double>();
	}
	
	// Init data to specified size with random values in the range 0..1
	public void init(int size) {
		data.clear();
		for (int i=0;i<size;i++) {
			data.add(Math.random());
		}
	}
	
	public ArrayList<Double> getData() { return data; };
	
	/**
	 * Get raw double value.
	 * @param i	Index
	 * @return	Double value
	 */
	public double getDouble(int i) {
		return data.get(i);
	}
	
	
	/**
	 * @param i		Index of the chromosome value to return.
	 * @return		Upper case Char representation of the chromosome value. 
	 */
	public char getChar(int i) {
		if (i>=data.size()) return 'x';
		double span = (double) ((char)'Z'-(char)'A');
		return (char) ((char)'A'+(char)(span*data.get(i)));
	}
	
	public void set(int i, double v) { data.set(i,v); }

	public void clamp(int i, double min, double max) {
		if (data.get(i)<min) data.set(i, min);
		if (data.get(i)>max) data.set(i, max);
	}
	
}
