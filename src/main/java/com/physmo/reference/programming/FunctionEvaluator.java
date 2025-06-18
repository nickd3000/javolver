package com.physmo.reference.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Scoring;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;

import java.awt.Color;

public class FunctionEvaluator implements ProgramEvaluator {

    @Override
    public void preEvaluateStep(SimpleMachine2 sm, Chromosome dna, double step) {
        sm.regA = (int) inputValue(step);
    }

    double inputValue(double step) {
        return step; // / 10.0;
    }

    @Override
    public double evaluate(SimpleMachine2 sm, Chromosome dna, double x) {
        double score;

        double targetVal = func(x);

        score = scoreForExpected(sm.regB, targetVal);

        return score;
    }

    double func(double x) {
        double a = x * 4;
        //return Math.pow(2*a,2)-Math.pow(1*a,3);

        //return 1 + x + (x * x) + (Math.sin(x * 10)+0.5) * 100;
        //return (Math.sin(x/50.0)+1)*100;
        //return x;/// * 0.5;
       return (x%150<70)?10:150; // notches
        //return x%100;
    }

    public static double scoreForExpected(double actual, double expected) {
//        double maxDiff = 400;
//        double diff = Math.abs(expected - actual);
//        if (diff > maxDiff) diff = maxDiff;
//        diff = 1.0-(diff / maxDiff);
//        double score = (diff * diff * diff) *10;
        return Scoring.scoreValue(actual, expected, 200);
    }

    @Override
    public int getNumberOfSteps() {
        return 50;
    }

    @Override
    public void render(SimpleMachine2 sm, Chromosome dna, BasicDisplay bd, double x) {
        double y = 0;
        double radius = 2;
        bd.setDrawColor(Color.BLUE);

        double inputValue = inputValue(x); //((double)x/(double)width)*2;
        bd.setDrawColor(Color.BLUE);
        y = func(inputValue);
        bd.drawFilledCircle(x, (int) y, radius);

        bd.setDrawColor(Color.WHITE);
        y = sm.regB;
        bd.drawFilledCircle(x-1, (int) y+2, radius);
    }

    @Override
    public int getOutputValueHash(SimpleMachine2 sm, Chromosome dna) {
        return sm.regB;
    }

    @Override
    public String report(SimpleMachine2 sm, Chromosome dna) {
        return null;
    }
}
