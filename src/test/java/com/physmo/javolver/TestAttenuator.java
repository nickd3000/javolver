package com.physmo.javolver;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TestAttenuator {

    @Test
    public void testCanSetScoreRangeAndAddParameters() {
        Attenuator att = new Attenuator();
        att.setScoreRange(0, 100);
        att.addParam("mutationRate", 0.5, 0.01);

        Map<String, Double[]> params = att.getRegisteredParameters();
        Assert.assertTrue(params.containsKey("mutationRate"));
        Assert.assertArrayEquals(new Double[]{0.5, 0.01}, params.get("mutationRate"));
    }

    @Test
    public void testReturnsMinValueAtMinScoreAndMaxAtMaxScore() {
        Attenuator att = new Attenuator();
        att.setScoreRange(0, 100);
        att.addParam("x", 10, 20);

        att.setScore(0);
        Assert.assertEquals(10, att.getValue("x"), 0.001);

        att.setScore(100);
        Assert.assertEquals(20, att.getValue("x"), 0.001);
    }

    @Test
    public void testReturnsInterpolatedValueAtMidScore() {
        Attenuator att = new Attenuator();
        att.setScoreRange(0, 100);
        att.addParam("y", 0, 10);

        att.setScore(50);
        Assert.assertEquals(5, att.getValue("y"), 0.001);
    }

    @Test
    public void testScoreClampsAtBounds() {
        Attenuator att = new Attenuator();
        att.setScoreRange(0, 100);
        att.addParam("z", 100, 200);

        att.setScore(-50);
        Assert.assertEquals(100, att.getValue("z"), 0.001);

        att.setScore(150);
        Assert.assertEquals(200, att.getValue("z"), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnUnknownParameter() {
        Attenuator att = new Attenuator();
        att.setScoreRange(0, 1);
        att.getValue("notAdded");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsForInvalidScoreRange() {
        new Attenuator().setScoreRange(10, 5);
    }

    @Test
    public void testSupportsMethodChainingForInitialization() {
        Attenuator att = new Attenuator()
                .setScoreRange(0, 10)
                .addParam("alpha", 5, 15);
        att.setScore(5);
        Assert.assertEquals(10, att.getValue("alpha"), 0.001);
    }
}
