package com.physmo.javolver;

import org.junit.Assert;
import org.junit.Test;

public class TestScoring {

    @Test
    public void testScoreValueExactMatch() {
        double result = Scoring.scoreValue(5.0, 5.0, 10.0);
        Assert.assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testScoreValueOutsideRange() {
        double result = Scoring.scoreValue(20.0, 5.0, 10.0);
        Assert.assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testScoreValueAtRangeBoundary() {
        double result = Scoring.scoreValue(15.0, 5.0, 10.0);
        Assert.assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testScoreValueHalfWay() {
        // diff = 5, range = 10 -> diff/range = 0.5 -> (1-0.5)^2 = 0.25
        double result = Scoring.scoreValue(10.0, 5.0, 10.0);
        Assert.assertEquals(0.25, result, 0.0001);
    }

    @Test
    public void testGetScoreForCharacterMatch() {
        double result = Scoring.getScoreForCharacter('A', 'A');
        Assert.assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testGetScoreForCharacterDifference() {
        // range is 15. diff = 3 -> 3/15 = 0.2 -> (1-0.2)^2 = 0.8^2 = 0.64
        double result = Scoring.getScoreForCharacter('A', 'D');
        Assert.assertEquals(0.64, result, 0.0001);
    }

    @Test
    public void testGetScoreForCharacterOutsideRange() {
        // range is 15. 'A' (65), 'Z' (90). diff = 25 > 15 -> result should be 0.0
        double result = Scoring.getScoreForCharacter('A', 'Z');
        Assert.assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testScoreValueNegative() {
        // expected -5, actual -7, range 10 -> diff = 2 -> 2/10 = 0.2 -> (1-0.2)^2 = 0.64
        double result = Scoring.scoreValue(-7.0, -5.0, 10.0);
        Assert.assertEquals(0.64, result, 0.0001);
    }

    @Test
    public void testScoreValueZeroRange() {
        // Current implementation results in NaN for range 0 due to division by zero.
        double result = Scoring.scoreValue(5.0, 5.0, 0.0);
        Assert.assertTrue(Double.isNaN(result));
    }
    @Test
    public void testScoreValueVarious() {
        Assert.assertEquals(1.0, Scoring.scoreValue(10, 10, 100), 0.0001);
        Assert.assertEquals(1.0, Scoring.scoreValue(10, 10, 10), 0.0001);
        Assert.assertEquals(0.9025, Scoring.scoreValue(5, 10, 100), 0.0001);
        Assert.assertEquals(0.81, Scoring.scoreValue(5, 10, 50), 0.0001);
    }

    @Test
    public void testGetScoreForCharacterVarious() {
        Assert.assertEquals(1.0, Scoring.getScoreForCharacter('a', 'a'), 0.001);
        Assert.assertEquals(0.871, Scoring.getScoreForCharacter('b', 'a'), 0.001);
        Assert.assertEquals(0.871, Scoring.getScoreForCharacter('a', 'b'), 0.001);
        Assert.assertEquals(0.751, Scoring.getScoreForCharacter('c', 'a'), 0.001);
        Assert.assertEquals(0.640, Scoring.getScoreForCharacter('d', 'a'), 0.001);
    }
}
