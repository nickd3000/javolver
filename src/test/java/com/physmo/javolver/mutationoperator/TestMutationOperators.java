package com.physmo.javolver.mutationoperator;

import com.physmo.javolver.Individual;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestMutationOperators {

    @Test
    public void testMutationOperatorSimple() {
        Individual ind = new Individual(10);
        ind.getDna().initFromFunction(i -> 0.5);
        
        MutationOperatorSimple mop = new MutationOperatorSimple(1, 0.1);
        mop.mutate(ind, 1.0);
        
        // At least one gene should have changed (most likely, although random could result in no change if amount is 0, 
        // but it uses (Math.random() - 0.5) * amount * 2.0 * temperature).
        // Let's check that the individual is still valid.
        for (int i = 0; i < 10; i++) {
            double val = ind.getDna().getDouble(i);
            // Even if it goes slightly outside 0..1, it should be a double.
            Assert.assertFalse(Double.isNaN(val));
            Assert.assertFalse(Double.isInfinite(val));
        }
    }

    @Test
    public void testMutationOperatorSingle() {
        Individual ind = new Individual(10);
        ind.getDna().initFromFunction(i -> 0.5);
        
        MutationOperatorSingle mop = new MutationOperatorSingle(0.1);
        mop.mutate(ind, 1.0);
        
        int changedCount = 0;
        for (int i = 0; i < 10; i++) {
            if (ind.getDna().getDouble(i) != 0.5) changedCount++;
        }
        
        Assert.assertTrue("At most one gene should change in MutationOperatorSingle per mutate call if chance is 1.0", changedCount <= 1);
    }
    
    @Test
    public void testMutationOperatorRandomize() {
        Individual ind = new Individual(10);
        ind.getDna().initFromFunction(i -> 0.5);
        
        MutationOperatorRandomize mop = new MutationOperatorRandomize(1.0);
        mop.mutate(ind, 1.0);
        
        int changedCount = 0;
        for (int i = 0; i < 10; i++) {
            if (ind.getDna().getDouble(i) != 0.5) changedCount++;
        }
        Assert.assertTrue(changedCount > 0);
    }

    @Test
    public void testMutationOperatorSwap() {
        Individual ind = new Individual(2);
        ind.getDna().set(0, 0.1);
        ind.getDna().set(1, 0.9);
        
        MutationOperatorSwap mop = new MutationOperatorSwap(1.0, 1);
        mop.mutate(ind, 1.0);
        
        // Should have swapped 0 and 1
        Assert.assertEquals(0.9, ind.getDna().getDouble(0), 0.0001);
        Assert.assertEquals(0.1, ind.getDna().getDouble(1), 0.0001);
    }

    @Test
    public void testMutationOperatorShuffle() {
        Individual ind = new Individual(5);
        for (int i = 0; i < 5; i++) ind.getDna().set(i, i * 0.1);

        MutationOperatorShuffle mop = new MutationOperatorShuffle(1);
        mop.mutate(ind, 1.0);

        // All original values should still be present, just potentially in different order
        List<Double> originalValues = new ArrayList<>();
        for (int i = 0; i < 5; i++) originalValues.add(i * 0.1);

        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(originalValues.contains(ind.getDna().getDouble(i)));
        }
    }
}
