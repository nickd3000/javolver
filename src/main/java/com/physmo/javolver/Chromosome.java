package com.physmo.javolver;

import java.util.ArrayList;

/**
 * Chromosome is a simple list of double values (0..1), that supports some general functionality.
 * Values will be mapped to the required data ranges.
 * TODO: Why is this a list and not an array?????
 * @author nick
 */
public class Chromosome {

    /**
     * Genetic data store, stored as a list of real numbers.
     */
    //ArrayList<Double> data;
    double [] data;

    public int getSize() {
        if (data == null) return 0;
        return data.length;
    }

    /**
     * Default constructor.
     */
    public Chromosome() {

        //data = new ArrayList<Double>();
        data = null;
    }


    /**
     * Init data to specified size with random values in the range 0..1
     *
     * @param size Length of DNA structure.
     */
    public void init(int size) {
        //data.clear();
        data = new double[size];
        for (int i = 0; i < size; i++) {
            //data.add(Math.random());
            data[i]=Math.random();
        }
    }


    /**
     * Accessor for DNA data.
     *
     * @return Chromosome data.
     */
    //public ArrayList<Double> getData() {
     //   return data;
    //}

    public double[] getData() { return data; }

    /**
     * Get raw double value.
     *
     * @param i Index
     * @return Double value
     */
    public double getDouble(int i) {
        return data[i];
    }


    /**
     * Get a chromosome element mapped to an uppercase char.
     *
     * @param i Index of the chromosome value to return.
     * @return Upper case Char representation of the chromosome value.
     */
    public char getChar(int i) {
        if (i >= data.length) return 'x';
        double span = (double) ((char) 'Z' - (char) 'A');
        return (char) ((char) 'A' + (char) (span * data[i]));
    }


    /**
     * Set an element of the chromosome.
     *
     * @param i Index
     * @param v Value
     */
    public void set(int i, double v) {
        //data.set(i, v);
        data[i]=v;
    }


    /**
     * Clamp an element of the chromosome to the supplied range.
     *
     * @param i   Index
     * @param min Min value of range
     * @param max Max value of range
     */
    public void clamp(int i, double min, double max) {
        double val = data[i];
        if (val < min) data[i] = min;
        if (val > max) data[i] = max;
    }


    public void swap(int index1, int index2) {
        double v1 = data[index1];
        double v2 = data[index2];
        data[index1] = v2;
        data[index2] = v1;
    }
}
