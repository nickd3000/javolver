package com.physmo.javolver.programming;

import com.physmo.javolver.Individual;
import com.physmo.minvio.BasicDisplay;

import java.awt.*;


public class GeneProgram extends Individual  {

    //public String word = "";
    int programSize = 250;


    double cyclePenalty = 0.0; // number of cycles involved in running program.
    double scorePenalty = 0;
    String consoleOutput = "";

    public double location = 0;
    Class evaluatorClass = null;

    ProgramEvaluator programEvaluator=null;

    public GeneProgram(Class evaluator) {
        this.evaluatorClass = evaluator;
        dna.init(programSize); // 200

        try {
            programEvaluator = (ProgramEvaluator)evaluatorClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Individual clone() {
        return (Individual)(new GeneProgram(evaluatorClass));
    }


    // Setup and also run simple machine, return console result.
    public void setupSimpleMachineFromDNA(SimpleMachine sm) {

        for (int i = 0; i < dna.getData().size(); i++) {
            int val = (int) (dna.getDouble(i) * 30.0); //250
            sm.memory[i] = val;
        }
    }

    public void runSimpleMachine(SimpleMachine sm) {
        scorePenalty = 0;
        int maxCycles = 200*2;
        int cycleCount = 0;
        for (int i=0;i<maxCycles; i++)
        {
            cycleCount++;
            int result = sm.runCycle();
            if (sm.getMaxHits()>20) {
                scorePenalty = 20;
                break;
            }

            if (result==1) break;
        }

        consoleOutput = sm.console;

        cyclePenalty = (double)cycleCount / (double)maxCycles;

        //return sm.console;
    }

    @Override
    public double calculateScore() {

        SimpleMachine sm=null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score=0, stepScore=0;

        for (int step=0;step<numberOfSteps*10;step++) {
            sm = new SimpleMachine();

            setupSimpleMachineFromDNA(sm);
            programEvaluator.preEvaluateStep(sm,dna,step/10);
            runSimpleMachine(sm);
            stepScore = programEvaluator.evaluate(sm, dna, step/10);
            score+=stepScore;

            //consoleOutput+=" "+stepScore;
        }

        return score;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);

        sb.append(String.format("Score:%f  Word:%s\n", score, consoleOutput));

        return sb.toString();

    }

    public void render(BasicDisplay bd) {
//        SimpleMachine sm = new SimpleMachine();
//
//        setupSimpleMachineFromDNA(sm);
//        programEvaluator.render(sm, dna,bd,);


        SimpleMachine sm=null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score=0, stepScore=0;

        bd.cls(Color.lightGray);

        for (int step=0;step<numberOfSteps*10;step++) {
            sm = new SimpleMachine();

            setupSimpleMachineFromDNA(sm);
            programEvaluator.preEvaluateStep(sm,dna,step/10.0);
            runSimpleMachine(sm);

            programEvaluator.render(sm,dna,bd,step/10.0);

            //stepScore = programEvaluator.evaluate(sm, dna, step);
            //score+=stepScore;

            //consoleOutput+=" "+stepScore;
        }

        bd.refresh();

    }

}

