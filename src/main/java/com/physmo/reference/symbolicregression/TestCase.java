package com.physmo.reference.symbolicregression;

import java.util.function.DoubleFunction;

public class TestCase {
    int numEntries;
    double[] inputs;
    double[] outputs;


    public TestCase(double startX, double endX, int numValues, DoubleFunction<Double> doubleFunction) {
        numEntries = numValues;
        inputs = new double[numValues];
        outputs = new double[numValues];
        double x = 0;
        double span = endX - startX;
        for (int i = 0; i < numValues; i++) {
            x = startX + ((span / numValues) * i);
            inputs[i] = x;
            outputs[i] = doubleFunction.apply(x);
        }
    }


}
