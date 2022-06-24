package com.physmo.javolver;

import java.util.function.IntToDoubleFunction;

/**
 * Chromosome is a simple list of double values (0..1), that supports some general functionality.
 * Values will be mapped to the required data ranges.
 *
 * @author nick
 */
public class Chromosome {

    /**
     * Genetic data store, stored as a list of real numbers.
     */
    private double[] data;

    /**
     * Default constructor.
     */
    public Chromosome(int dnaSize) {
        init(dnaSize);
    }

    /**
     * Init data to specified size with random values in the range 0..1
     *
     * @param size Length of DNA structure.
     */
    public void init(int size) {
        data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = Math.random();
        }
    }

    public int getSize() {
        if (data == null) return 0;
        return data.length;
    }

    public void initFromFunction(IntToDoubleFunction initFunction) {
        for (int i = 0; i < data.length; i++) {
            data[i] = initFunction.applyAsDouble(i);
        }
    }

    /**
     * Accessor for DNA data.
     *
     * @return Chromosome data.
     */
    public double[] getData() {
        return data;
    }

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
        double span = 'Z' - 'A';
        return (char) ('A' + (char) (span * data[i]));
    }

    /**
     * Set an element of the chromosome.
     *
     * @param i Index
     * @param v Value
     */
    public void set(int i, double v) {
        data[i] = v;
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
