package com.physmo.reference.symbolicregression;

public class Remapper {

    double inMin;
    double inMax;
    double outMin;
    double outMax;


    public Remapper(double inMin, double inMax, double outMin, double outMax) {
        this.inMin=inMin;
        this.inMax=inMax;
        this.outMin=outMin;
        this.outMax=outMax;
    }

    public double remap(double value) {
        value = (value - inMin) / ((inMax - inMin) / (outMax - outMin));
        return value + outMin;
    }

    public static double remap(double value, double inMin, double inMax, double outMin, double outMax) {
        if (outMax - outMin == 0) return 0;
        value = (value - inMin) / ((inMax - inMin) / (outMax - outMin));
        return value + outMin;
    }
}
