package com.physmo.javolver;


/**
 * This is the base class that should be used to create custom individual types to feed into the evolver.<br>
 * Individual provides a simple array of floats to represent the 'DNA' of the individual. <br>
 * the user can map these floats to any data type they require.
 *
 * @author Nick Donnelly (Twitter: @nickd3000)
 * @version 1.0
 * @since 2016-04-01
 */
public class Individual {
    /**
     * Holds the genetic information for the individual as an array of doubles
     * Each type of individual should map double values to their required data ranges.
     */
    public Chromosome dna;
    /**
     * The score of this individual, higher is better.
     */
    protected double score = 0.0;
    ScoreFunction scoreFunction;
    /**
     * A latch variable that represents whether the individual has been scored or not.
     * Scoring can be computationally intensive, so this helps prevent multiple scoring events.
     */
    boolean processed = false;
    double diversity = 0.0;

    /**
     * Default constructor.
     */
    public Individual(int dnaSize) {
        dna = new Chromosome(dnaSize);
    }

    /**
     * Copy constructor. Creates a new individual with the same DNA size as the source,
     * and copies the score function. The score and processed status are reset.
     *
     * @param cloneSource The individual to copy configuration from.
     */
    public Individual(Individual cloneSource) {
        this.dna = new Chromosome(cloneSource.getDna().getSize());
        this.scoreFunction = (cloneSource.scoreFunction);
        this.processed=false;
        this.score=0;
        this.diversity=0;
    }

    /**
     * Gets the score function used to evaluate this individual.
     *
     * @return The score function.
     */
    public ScoreFunction getScoreFunction() {
        return scoreFunction;
    }

    /**
     * Sets the score function used to evaluate this individual.
     *
     * @param scoreFunction The score function.
     */
    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    /**
     * Return the score of this individual. If the individual has not yet been processed, call calculateScore() first.
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

    /**
     * Gets the diversity value of this individual.
     * Diversity can be used to maintain variety in the population.
     *
     * @return The diversity value.
     */
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
     * Sets the individual's score.
     *
     * @param s The score.
     * @return The score (pass through).
     */
    public double setScore(double s) {
        return score = s;
    }

    /**
     * Creates a deep copy of this individual, including its DNA data.
     * The new individual will have the same DNA values but a reset score and processed status.
     *
     * @return A new Individual instance that is a deep clone of this one.
     */
    public Individual cloneFully() {
        Individual clone = new Individual(this);
        for (int i = 0; i < this.getDna().getSize(); i++) {
            clone.getDna().set(i, getDna().getDouble(i));
        }
        return clone;
    }

    /**
     * Gets the chromosome (DNA) of this individual.
     *
     * @return The chromosome.
     */
    public Chromosome getDna() {
        return dna;
    }

    /**
     * Sets the chromosome (DNA) of this individual.
     *
     * @param dna The new chromosome.
     */
    public void setDna(Chromosome dna) {
        this.dna = dna;
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

    /**
     * Checks whether the individual has been processed and scored.
     *
     * @return True if processed, false otherwise.
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Sets whether this individual has been processed (scored).
     *
     * @param processed True if the individual should be marked as processed.
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        if (this.dna.getSize() != that.dna.getSize()) return false;
        double[] thisData = this.dna.getData();
        double[] thatData = that.dna.getData();
        for (int i = 0; i < thisData.length; i++) {
            if (Double.compare(thisData[i], thatData[i]) != 0) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (double val : dna.getData()) {
            result = 31 * result + Double.hashCode(val);
        }
        return result;
    }

    /**
     * Gets a hash code representing the state of this individual's DNA.
     * This is a convenience method that calls {@link #hashCode()}.
     *
     * @return An integer hash of the DNA data.
     */
    public int getHash() {
        return hashCode();
    }
}
