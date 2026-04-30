package com.physmo.javolver.selectionoperator;

import com.physmo.javolver.Individual;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestSelectionOperators {

    @Test
    public void testSelectionOperatorTournament() {
        List<Individual> pool = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Individual ind = new Individual(1);
            ind.setScore(i); // Scores 0 to 9
            pool.add(ind);
        }

        SelectionOperatorTournament sop = new SelectionOperatorTournament(0.5);
        Individual selected = sop.select(pool);

        Assert.assertNotNull(selected);
        // With tournament size 0.5 * 10 = 5, we expect a relatively high score on average.
        // But since it's random, we can't be sure about the exact score.
        // However, we can check it's one of the individuals in the pool.
        Assert.assertTrue(pool.contains(selected));
    }

    @Test
    public void testSelectionOperatorRoulette() {
        List<Individual> pool = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Individual ind = new Individual(1);
            ind.setScore(i + 1); // Scores 1 to 10
            pool.add(ind);
        }

        SelectionOperatorRoulette sop = new SelectionOperatorRoulette();
        Individual selected = sop.select(pool);

        Assert.assertNotNull(selected);
        Assert.assertTrue(pool.contains(selected));
    }
}
