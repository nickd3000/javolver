package com.physmo.javolver.solver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.ScoreFunction;
import com.physmo.javolver.mutationoperator.MutationOperator;

import java.util.*;


/**
 * A solver that uses an Evolutionary Strategy (ES) approach.
 * Unlike the standard population-based genetic algorithm, ES typically focuses on
 * evolving a single best individual (or a small set of individuals) through
 * heavy mutation and recombination of a pool of mutated clones.
 */
public class OptimizerES extends Solver {

    private final List<MutationOperator> mutationStrategies = new ArrayList<>();
    Individual bestIndividual;
    int dnaSize = 10;
    int poolSize = 20;
    int combineSize = poolSize/4;
    int mutationCount = 2;
    int iteration = 0;
    Random random = new Random();
    double changeAmount = 0.01;
    int successCount = 0;
    int totalCount = 0;
    boolean useAdaptiveStepSize = true;
    boolean useClamping = true;
    private ScoreFunction scoreFunction;

    /**
     * Default constructor.
     */
    public OptimizerES() {

    }

    @Override
    public void init() {
        bestIndividual = new Individual(dnaSize);
        bestIndividual.setScoreFunction(scoreFunction);

    }

    @Override
    public void resetPopulation() {
        bestIndividual = new Individual(dnaSize);
        bestIndividual.setScoreFunction(scoreFunction);
    }

    @Override
    public void setDnaSize(int dnaSize) {
        this.dnaSize = dnaSize;
    }

    @Override
    public void setScoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = scoreFunction;
    }

    @Override
    public void runOneGeneration() {
        algorithm();
        iteration++;

        if (useAdaptiveStepSize) {
            if (totalCount >= 10) {
                double rate = (double) successCount / totalCount;
                if (rate > 0.2) changeAmount *= 1.1;
                else if (rate < 0.2) changeAmount *= 0.9;
                successCount = 0;
                totalCount = 0;
            }
        } else {
            changeAmount *= 0.999;
        }
    }

    /**
     * Performs one generation of the ES algorithm.
     * This includes creating a pool of mutated clones, sorting them by score,
     * and combining the top individuals into a new "best" individual.
     */
    public void algorithm() {
        List<Individual> pool = new ArrayList<>();

        // Create pool of mutated clones.
        for (int i = 0; i < poolSize; i++) {
            pool.add(createMutatedClone(bestIndividual, changeAmount));
        }

        // Sort pool.
        pool.sort(Comparator.comparingDouble(Individual::getScore).reversed());

        // Recombination: average the top individuals.
        Individual offspring = bestIndividual.cloneFully();
        offspring.setProcessed(false);

        double[] offspringDna = offspring.getDna().getData();
        Arrays.fill(offspringDna, 0);

        int count = Math.min(combineSize, pool.size());
        for (int i = 0; i < count; i++) {
            double[] parentDna = pool.get(i).getDna().getData();
            for (int j = 0; j < parentDna.length; j++) {
                offspringDna[j] += parentDna[j];
            }
        }

        if (count > 0) {
            for (int j = 0; j < offspringDna.length; j++) {
                offspringDna[j] /= count;
            }
        }

        // Pick the best among bestIndividual, bestClone (pool.get(0)), and offspring.
        Individual winner = bestIndividual;
        Individual bestClone = pool.get(0);

        if (bestClone.getScore() > winner.getScore()) {
            winner = bestClone;
        }
        if (offspring.getScore() > winner.getScore()) {
            winner = offspring;
        }

        if (winner != bestIndividual) {
            bestIndividual = winner;
            successCount++;
        }
        totalCount++;
    }

    /**
     * Creates a mutated copy of a parent individual.
     *
     * @param parent         The parent individual to clone.
     * @param mutationAmount The magnitude of mutation to apply.
     * @return A new, mutated Individual.
     */
    public Individual createMutatedClone(Individual parent, double mutationAmount) {
        Individual clone = parent.cloneFully();
        clone.setProcessed(false);

        if (mutationStrategies.isEmpty()) {
            double[] data = clone.getDna().getData();
            int size = data.length;
            int count = Math.min(mutationCount, size);
            
            // If mutationCount is set to a negative value or exceeds size, mutate all.
            if (mutationCount <= 0 || mutationCount >= size) {
                for (int i = 0; i < size; i++) {
                    data[i] += random.nextGaussian() * mutationAmount;
                    if (useClamping) {
                        if (data[i] < 0) data[i] = 0;
                        if (data[i] > 1) data[i] = 1;
                    }
                }
            } else {
                for (int j = 0; j < mutationCount; j++) {
                    int i = random.nextInt(size);
                    data[i] += random.nextGaussian() * mutationAmount;
                    if (useClamping) {
                        if (data[i] < 0) data[i] = 0;
                        if (data[i] > 1) data[i] = 1;
                    }
                }
            }
        } else {
            for (MutationOperator strategy : mutationStrategies) {
                strategy.mutate(clone, mutationAmount);
            }
        }
        return clone;
    }

    @Override
    public Individual getBestScoringIndividual() {
        return bestIndividual;
    }

    @Override
    public void setTemperature(double temperature) {
        this.changeAmount = temperature;
    }

    @Override
    public int getIteration() {
        return iteration;
    }

    /**
     * Adds a mutation operator to be used by the solver.
     *
     * @param strategy The mutation strategy to add.
     */
    public void addMutationStrategy(MutationOperator strategy) {
        mutationStrategies.add(strategy);
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
        this.combineSize = poolSize/4;
    }


    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public boolean isUseAdaptiveStepSize() {
        return useAdaptiveStepSize;
    }

    public void setUseAdaptiveStepSize(boolean useAdaptiveStepSize) {
        this.useAdaptiveStepSize = useAdaptiveStepSize;
    }

    public boolean isUseClamping() {
        return useClamping;
    }

    public void setUseClamping(boolean useClamping) {
        this.useClamping = useClamping;
    }

    public int getMutationCount() {
        return mutationCount;
    }

    public void setMutationCount(int mutationCount) {
        this.mutationCount = mutationCount;
    }
}
