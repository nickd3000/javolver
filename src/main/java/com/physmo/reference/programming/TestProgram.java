package com.physmo.reference.programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Spreader;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.reference.programming.simplemachinie.Decompiler;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;

import java.awt.Color;
import java.util.List;

public class TestProgram {

    static int populationSize = 1500;
    static int batchSize = 10;
    //ProgramEvaluator programEvaluator = new FunctionEvaluator();
    ProgramEvaluator programEvaluator = new ProblemMaxOfThree();
    //ProgramEvaluator programEvaluator = new ProblemSumTwo();
    //ProgramEvaluator programEvaluator = new ProblemOddsAndEvens();
    //ProgramEvaluator programEvaluator = new ProblemInvertNumber();
    //ProgramEvaluator programEvaluator = new ProblemFibonacci();
    Javolver evolver;

    Decompiler decompiler = new Decompiler();

    long randomSeed = 0;

    public static void main(String[] args) throws Exception {
        TestProgram testProgram = new TestProgram();
        testProgram.init();
        testProgram.go();
        //testProgram.runGatherBestBatches();
        //testProgram.iterativeRun(0);
    }

    public void init() {
        evolver = Javolver.builder()
                .dnaSize(100)
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationOperator(new MutationOperatorSimple(1, 0.2, 0.99))
                .addMutationOperator(new MutationOperatorSwap(0.1, 2))
                //.addMutationOperator(new MutationOperatorShuffle(2))
                //.addMutationOperator(new MutationStrategyRandomize(0.1))
                .setSelectionOperator(new SelectionOperatorTournament(0.15))
                //.setSelectionOperator(new SelectionStrategyRoulette())
                //.setBreedingOperator(new BreedingStrategyCrossover())
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(this::calculateScore)
                .build();
    }

    boolean reduceCode = false;
    double reduceCodeScoreThreshold = 42;

    public void go() {
        init();

        for (int j = 0; j < 50000; j++) {

            if (j % 2 == 0) {
                randomSeed = (long) (Math.random() * 1000);
            }

            runBatch(100);

            System.out.println("iteration: " + evolver.getIteration() + "  best score:" + evolver.getBestScoringIndividual().getScore());
            Individual ind = evolver.getBestScoringIndividual();
            System.out.println(showIndividual(ind, randomSeed));

            if (j % 10 == 0) {
                printDecompilation();
            }

            if (ind.getScore()>reduceCodeScoreThreshold) {
                if (!reduceCode) System.out.println("******************* REDUCE CODE NOW");
                reduceCode=true;
            } else {
                if (reduceCode) System.out.println("******************* DONT REDUCE CODE");
                reduceCode=false;
            }
        }

        System.out.print("END ");
    }

    public void printDecompilation() {
        SimpleMachine2 sm = new SimpleMachine2();
        sm.reset();
        setupSimpleMachineFromDNA(sm, evolver.getBestScoringIndividual());
        String de = decompiler.decompile(sm, 10);
        System.out.println(de);
    }


    public void fillPoolFromList(List<Individual> individuals) {
        evolver.getPool().clear();
        for (int i = 0; i < populationSize; i++) {
            evolver.getPool().add(individuals.get(i % individuals.size()));
        }
    }

    public void runBatch(int batchSize) {
        for (int i = 0; i < batchSize; i++) {

            evolver.doOneCycle();
        }
    }

    public double calculateScore(Individual individual) {

        SimpleMachine2 sm = null;
        int numberOfRuns = 50;// 25;
        double totalScore = 0, score = 0;
        sm = new SimpleMachine2();
        int numCycles = 0;
        for (int run = 0; run < numberOfRuns; run++) {
            sm.reset();
            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), 0, randomSeed + run);
            numCycles += runSimpleMachine(sm);
            score = programEvaluator.evaluate(sm, individual.getDna(), 0);
            totalScore += score;

        }

        totalScore -= numCycles * 0.001;

        // increase score for fewer instructions
        double noOpScore = 0.00001;
        if (reduceCode) noOpScore = 0.002;
        totalScore += sm.countNoOps()*noOpScore;

        return totalScore;
    }

    public String showIndividual(Individual individual, long randomSeed) {

        SimpleMachine2 sm;
        int numberOfRuns = 3;
        double totalScore = 0, score = 0;
        String report = "";

        for (int run = 0; run < numberOfRuns; run++) {
            sm = new SimpleMachine2();

            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), 0, randomSeed + run);
            runSimpleMachine(sm);
            score = programEvaluator.evaluate(sm, individual.getDna(), 0);
            totalScore += score;
            report = report + programEvaluator.report(sm, individual.getDna()) + System.lineSeparator();
        }

        return report;
    }

    // Setup and also run simple machine, return console result.
    public void setupSimpleMachineFromDNA(SimpleMachine2 sm, Individual individual) {
        int dnaSize = individual.getDna().getSize();
        int memSize = sm.memory.length;

        for (int i = 0; i < dnaSize && i < memSize; i++) {
            // Get DNA value (usually 0.0 to 1.0 or similar)
            double dnaVal = individual.getDna().getDouble(i);

            // Use Math.abs to ensure positive, and wrap around the maxOps
            // defined in Microcode to increase the density of valid instructions.
            int instruction = (int) (Math.abs(dnaVal) * 60);

            sm.memory[i] = instruction;
        }
    }


    public int runSimpleMachine(SimpleMachine2 sm) {

        int maxCycles = 150;
        int cycleCount = 0;

        for (int i = 0; i < maxCycles; i++) {
            cycleCount++;
            int result = sm.runCycle();
            if (result == 1) break;
        }

        return cycleCount;
    }

    public void render(BasicDisplay bd, Individual individual, long randomSeed) {

        SimpleMachine2 sm = null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score = 0, stepScore = 0;

        bd.getDrawingContext().cls(Color.lightGray);

        for (int step = 0; step < numberOfSteps * 10; step++) {
            sm = new SimpleMachine2();

            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), step / 10.0, randomSeed);
            runSimpleMachine(sm);

            programEvaluator.render(sm, individual.getDna(), bd, step / 10.0);

        }

        bd.repaint();

    }
}
