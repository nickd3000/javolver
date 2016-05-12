package javolver;

import java.util.ArrayList;


// Chromosome is a simple list of double values (0..1), that supports some general functionality.
// Values will be mapped to the required data ranges.
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
	
	public double getDouble(int i) {
		return data.get(i);
	}
	
	public char getChar(int i) {
		if (i>=data.size()) return 'x';
		double span = (double) ((char)'Z'-(char)'A');
		return (char) ((char)'A'+(char)(span*data.get(i)));
	}

}
