package com.physmo.reference.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.javolver.Scoring;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;

import java.util.Random;

public class ProblemFibonacci implements ProgramEvaluator {

    int[] seq = new int[20];

    Random random = new Random();
    int maxValueRange = 200;
    int input1 = 0;
    int input2 = 0;

    int inputIndex = 0;
    int expectedResult = 0;

    public ProblemFibonacci() {
        seq[0]=0;
        seq[1]=1;
        seq[2]=1;
        for (int i=3;i<seq.length;i++) {
            seq[i]=seq[i-1]+seq[i-2];
        }
    }

    @Override
    public void preEvaluateStep(SimpleMachine2 sm, Chromosome dna, double step, long randomSeed) {
        int seqPos = random.nextInt(seq.length-8)+2;
        input1=seq[seqPos];
        input2=seq[seqPos+1];
        expectedResult=input1+input2;

        sm.memory[inputIndex+1] = input1;
        sm.memory[inputIndex+2] = input2;

        sm.pc=10;
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
        return (int) sm.regD;
    }

    @Override
    public String report(SimpleMachine2 sm, Chromosome dna) {
        double output = sm.regD;
        double target = expectedResult;

        //String str = "["+sm.memory[inputIndex+0]+","+sm.memory[inputIndex+1]+","+sm.memory[inputIndex+2]+"]";
        String result = " expecting "+(target)+" got "+output;
        return result;
    }
}
