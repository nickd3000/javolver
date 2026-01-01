package com.physmo.reference.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Scoring;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;

import java.util.Random;

public class ProblemMaxOfThree implements ProgramEvaluator {

    //Random random = new Random();
    int[] inputs = new int[3];
    int maxValueRange = 99;
    int maxValue = 0;

    int inputIndex = 0;

    @Override
    public void preEvaluateStep(SimpleMachine2 sm, Chromosome dna, double step, long randomSeed) {

        Random random = new Random( randomSeed);

        for (int i=0;i<3;i++) {
            inputs[i] = random.nextInt(maxValueRange);
            sm.memory[inputIndex+i] = inputs[i];
        }

        maxValue=0;
        for (int i=0;i<3;i++) {
            if (inputs[i]>maxValue) maxValue=sm.memory[inputIndex+i];
        }

        sm.pc=10;
    }

    @Override
    public double evaluate(SimpleMachine2 sm, Chromosome dna, double step) {
        double output = sm.regD;
        double expected = maxValue;

        return Scoring.scoreValue(output, expected, 50);
    }



    @Override
    public int getNumberOfSteps() {
        return 0;
    }

    @Override
    public void render(SimpleMachine2 sm, Chromosome dna, BasicDisplay bd, double step) {

    }

    @Override
    public int getOutputValueHash(SimpleMachine2 sm, Chromosome dna) {
        return (int) sm.regD;
    }

    @Override
    public String report(SimpleMachine2 sm, Chromosome dna) {
        String str = "["+inputs[0]+","+inputs[1]+","+inputs[2]+"]";
        String result = " expecting "+maxValue+" got "+sm.regD+ (maxValue==sm.regD?"   *":"");
        return str+result;
    }
}
