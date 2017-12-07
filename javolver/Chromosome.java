package javolver;

import java.util.ArrayList;

/**
 *	Chromosome is a simple list of double values (0..1), that supports some general functionality.
 *	Values will be mapped to the required data ranges.
 *	@author nick
 */
public class Chromosome {

	/**
	 * Genetic data store, stored as a list of real numbers.
	 */
	ArrayList<Double> data; 
	
	public int getSize() {
		if (data==null) return 0;
		return data.size();
	}
	
	/**
	 * Default constructor.
	 */
	public Chromosome() {
		data = new ArrayList<Double>();
	}
	

	/**
	 * Init data to specified size with random values in the range 0..1
	 * @param size	Length of DNA structure.
	 */
	public void init(int size) {
		data.clear();
		for (int i=0;i<size;i++) {
			data.add(Math.random());
		}
	}
	
	
	/**
	 * Accessor for DNA data.
	 * @return	Chromosome data.
	 */
	public ArrayList<Double> getData() {
		return data;
	};
	
	/**
	 * Get raw double value.
	 * @param i	Index
	 * @return	Double value
	 */
	public double getDouble(int i) {
		return data.get(i);
	}
	
	
	/**
	 * Get a chromosome element mapped to an uppercase char.
	 * @param i		Index of the chromosome value to return.
	 * @return		Upper case Char representation of the chromosome value. 
	 */
	public char getChar(int i) {
		if (i>=data.size()) return 'x';
		double span = (double) ((char)'Z'-(char)'A');
		return (char) ((char)'A'+(char)(span*data.get(i)));
	}
	
	
	/**
	 * Set an element of the chromosome.
	 * @param i		Index
	 * @param v		Value
	 */
	public void set(int i, double v) {
		data.set(i,v);
	}

	
	/**
	 * Clamp an element of the chromosome to the supplied range.
	 * @param i		Index
	 * @param min	Min value of range
	 * @param max	Max value of range
	 */
	public void clamp(int i, double min, double max) {
		if (data.get(i)<min) data.set(i, min);
		if (data.get(i)>max) data.set(i, max);
	}
	
	
	public void swap(int index1, int index2) {
		double v1 = data.get(index1);
		double v2 = data.get(index2);
		data.set(index1, v2);
		data.set(index2, v1);
	}
}
