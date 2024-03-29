package com.physmo.javolver;


/**
 * This is the base class that should be used to create custom individual types to feed into the evolver.<br>
 * Individual provides a simple array of floats to represent the 'DNA' of the individual, <br>
 * these floats can be mapped by the user to any data type they require.
 *
 * @author Nick Donnelly (Twitter: @nickd3000)
 * @version 1.0
 * @since 2016-04-01
 */
public class Individual {
    /**
     * Holds the genetic information for the individual as an array of doubles
     * Each type of individual should map double values to theie required data ranages.
     */
    public Chromosome dna;
    /**
     * The score of this individual, higher is better.
     */
    protected double score = 0.0;
    ScoreFunction scoreFunction;
    /**
     * A latch variable that represents whether the individual has being scored or not.
     * Scoring can be computationally intensive so this helps prevent multiple scoring events.
     */
    boolean processed = false;
    double diversity = 0.0;

    /**
     * Default constructor.
     */
    public Individual(int dnaSize) {
        dna = new Chromosome(dnaSize);
    }

    public Individual(Individual cloneSource) {
        this.dna = new Chromosome(cloneSource.getDna().getSize());
        this.scoreFunction = (cloneSource.scoreFunction);
    }

    public ScoreFunction getScoreFunction() {
        return scoreFunction;
    }

    public void setDna(Chromosome dna) {
        this.dna = dna;
    }

    /**
     * Return score of this individual. If the individual has not yet been processed, call calculateScore() first.
     *
     * @return Double value representing the score of the individual. Higher is better.
     */
    public double getScore() {
        if (!processed) {
            score = scoreFunction.score(this);
            processed = true;
        }
        return score;
    }

    public double getDiversity() {
        return diversity;
    }

    /**
     * Return score*score.
     *
     * @return Score value squared.
     */
    public double getScoreSquared() {
        getScore();
        return (score * score);
    }

    /**
     * Sets the individuals score.
     *
     * @param s The score.
     * @return The score (pass through).
     */
    public double setScore(double s) {
        return score = s;
    }

    public Individual cloneFully() {
        Individual clone = new Individual(this);
        for (int i = 0; i < this.getDna().getSize(); i++) {
            clone.getDna().set(i, getDna().getDouble(i));
        }
        return clone;
    }

    public Chromosome getDna() {
        return dna;
    }


    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    /**
     * Calculate the average difference between the DNA of two individuals.
     *
     * @param other Individual to compare to.
     * @return difference between individuals.
     */
    public double getDifference(Individual other) {
        int size = this.dna.getSize();
        double diff = 0;
        for (int i = 0; i < size; i++) {
            diff += Math.pow(Math.abs(this.dna.getDouble(i) - other.dna.getDouble(i)), 2);
        }
        if (diff > 0) diff = Math.sqrt(diff);

        return diff / (double) size;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public int getHash() {
        int combined=0;
        for (double val : dna.getData()) {
            combined += Double.hashCode(val);
        }
        return Integer.hashCode(combined);
    }
}
