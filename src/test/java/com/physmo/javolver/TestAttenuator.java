package com.physmo.javolver;

import org.junit.Assert;
import org.junit.Test;

public class TestAttenuator {

    @Test
    public void t1() {
        Attenuator attenuator = new Attenuator();

        attenuator.setScoreRange(10, 20);

        Assert.assertEquals(0, attenuator.setCurrentScore(5), 0.01);
        Assert.assertEquals(0, attenuator.setCurrentScore(10), 0.01);
        Assert.assertEquals(0.5, attenuator.setCurrentScore(15), 0.01);
        Assert.assertEquals(1.0, attenuator.setCurrentScore(20), 0.01);
        Assert.assertEquals(1.0, attenuator.setCurrentScore(25), 0.01);

        attenuator.addParameter("var1", 100, 200);
        attenuator.setCurrentScore(15);
        Assert.assertEquals(150, attenuator.getValue("var1"), 0.01);
    }
}
