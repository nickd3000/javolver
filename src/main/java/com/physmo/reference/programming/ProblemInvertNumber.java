package com.physmo.reference.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Scoring;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;

import java.util.Random;

public class ProblemInvertNumber implements ProgramEvaluator {

    Random random = new Random();
    int maxValueRange = 100;
    int input1 = 0;
    int expectedResult = 0;

    int inputIndex = 0;

    @Override
    public void preEvaluateStep(SimpleMachine2 sm, Chromosome dna, double step) {
        input1 = random.nextInt(maxValueRange);

        sm.memory[inputIndex + 1] = input1;
        expectedResult = maxValueRange - input1;

        sm.pc = 10;
    }

    @Override
    public double evaluate(SimpleMachine2 sm, Chromosome dna, double step) {
        double output = sm.regD;
        double target = expectedResult;
        return Scoring.scoreValue(output, target, 100);
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
        return sm.regD;
    }

    @Override
    public String report(SimpleMachine2 sm, Chromosome dna) {
        double output = sm.regD;
        double target = expectedResult;

        //String str = "["+sm.memory[inputIndex+0]+","+sm.memory[inputIndex+1]+","+sm.memory[inputIndex+2]+"]";
        String result = "input:"+input1+"  expecting:" + target + "  got:" + output;
        return result;
    }
}
