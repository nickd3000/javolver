package com.physmo.javolver.selectionoperator;

import com.physmo.javolver.Individual;

import java.util.List;
import java.util.Random;

public class SelectionOperatorTournament implements SelectionOperator {

    private final double selectionRange;
    private final Random random = new Random();

    /**
     * Constructs a new {@code SelectionOperatorTournament} instance with the specified selection range.
     * The selection range determines the proportion of the population that participates
     * in the tournament selection process.
     *
     * @param selectionRange the range, between 0 and 1, used to define the fraction of
     *                       the population considered during the tournament. A higher
     *                       value increases the number of candidates in the tournament,
     *                       while a lower value reduces it. The value must be greater than 0.
     */
    public SelectionOperatorTournament(double selectionRange) {
        this.selectionRange = selectionRange;
    }

    @Override
    public Individual select(List<Individual> pool) {
        int poolSize = pool.size();
        double maxScore = -1000;
        Individual currentWinner = getRandomIndividual(pool);

        double tournamentSize = selectionRange;

        int tSize = (int) (tournamentSize * (double) poolSize);
        if (tSize < 2) tSize = 2;

        for (int i = 0; i < tSize; i++) {
            Individual contender = getRandomIndividual(pool);

            if (getSelectionScore(contender) > maxScore || maxScore == -1000) {
                maxScore = getSelectionScore(contender);
                currentWinner = contender;
            }
        }

        return currentWinner;
    }


    private double getSelectionScore(Individual ind) {
        return ind.getScore();
    }

    private Individual getRandomIndividual(List<Individual> pool) {
        //int id = (int) ((float) (pool.size() - 1) * Math.random());
        int id = random.nextInt(pool.size());
        return pool.get(id);
    }
}
