package com.physmo.javolver.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.minvio.BasicDisplay;

import java.awt.*;

public class FunctionEvaluator implements ProgramEvaluator {

    double inputValue(double step) {
        return step/10.0;
    }

    double func(double x) {
        double a=x*4;
        return Math.pow(2*a,2)-Math.pow(1*a,3);

        //return 1+x+(x*x)+Math.sin(x*10)*1;
        //return (Math.sin(x*3)+0.6)*3;
    }

    double scoreForExpected(double actual, double expected) {
        double maxDiff = 10;
        double diff = Math.abs(expected-actual);
        if (diff>maxDiff) diff = maxDiff;
        double score = maxDiff - diff;
        return score/maxDiff;
    }


    @Override
    public void preEvaluateStep(SimpleMachine sm, Chromosome dna, double step) {
        sm.regA = inputValue(step);
    }

    @Override
    public double evaluate(SimpleMachine sm, Chromosome dna, double step) {
        double score = 0;

        double targetVal = func(inputValue(step));

        score = scoreForExpected(sm.regB, targetVal);

        return score;
    }


    @Override
    public int getNumberOfSteps() {
        return 10;
    }

    @Override
    public void render(SimpleMachine sm, Chromosome dna, BasicDisplay bd, double step) {
        int width = bd.getWidth();
        int height = bd.getHeight();
        double y=0;
        double radius=3;
        bd.setDrawColor(Color.BLUE);
        int x = (int)(step * 30);

            double inputValue = inputValue(step); //((double)x/(double)width)*2;
            bd.setDrawColor(Color.BLUE);
            y=func(inputValue);
            bd.drawFilledCircle(x,(int)((y+0.6)*height*0.1),radius);

            bd.setDrawColor(Color.WHITE);
            y=sm.regB;
            bd.drawFilledCircle(x,(int)((y+0.6)*height*0.1),radius);


    }
}
