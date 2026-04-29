package com.physmo.reference.programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorCrossover;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import com.physmo.javolver.selectionoperator.SelectionOperatorRoulette;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Warmup;
import com.physmo.minvio.BasicDisplay;
import com.physmo.reference.programming.simplemachinie.Decompiler;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// idea: create iterative solver -
// a tree of fresh GA's the leaf returns the evolved pool to the parent
// hmm - it should not have to be a tree

public class TestProgram {

    static int populationSize = 1000;
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

    boolean reduceCode = false;
    double reduceCodeScoreThreshold = 45;


    public static void main(String[] args) throws Exception {
        TestProgram testProgram = new TestProgram();
        testProgram.init();
        testProgram.go();
        //testProgram.runGatherBestBatches();
        //testProgram.iterativeRun(0);
    }

    public void init() {
        evolver = Javolver.builder()
                .dnaSize(200)
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(true)
                .parallelScoring(false)
                .addMutationOperator(new MutationOperatorSimple(1, 0.3, 0.5))
                .addMutationOperator(new MutationOperatorSimple(10, 0.1, 0.1))
                .addMutationOperator(new MutationOperatorSwap(0.02, 1))
                //.setSelectionOperator(new SelectionOperatorTournament(0.25))
                .setSelectionOperator(new SelectionOperatorRoulette())
                //.setBreedingOperator(new BreedingOperatorCrossover())
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(this::calculateScore)
                .build();

        Warmup.warmup(evolver, 2);
    }

    // experiment to do multiple quick runs from scratch to use for the initial population.
    public void preRun(int count, int batchSize) {
        System.out.println("Starting pre-run.");

        List<Individual> preRunPool = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            randomSeed = (long) (Math.random() * 1000);
            evolver.getPool().clear();
            evolver.increasePopulation(populationSize);
            runBatch(batchSize);
            System.out.println("Pre-run " + i + " - score: " + evolver.getBestScoringIndividual().getScore());
            preRunPool.add(evolver.getBestScoringIndividual());

        }

        evolver.getPool().clear();
        evolver.getPool().addAll(preRunPool);
        evolver.increasePopulation(populationSize);
    }

    public void doRecursivePreRun() {

        List<Individual> preRunPool = recursivePreRun(100,3,5,0);
        evolver.getPool().clear();
        evolver.getPool().addAll(preRunPool);
        evolver.increasePopulation(populationSize);
    }

    public List<Individual> recursivePreRun(int count, int batchSize, int maxDepth, int depth) {

        List<Individual> preRunPool = new ArrayList<>();
        randomSeed = (long) (Math.random() * 1000);

        if (depth!=maxDepth) {
            preRunPool.addAll(recursivePreRun(count, batchSize, maxDepth, depth + 1));
            preRunPool.addAll(recursivePreRun(count, batchSize, maxDepth, depth + 1));
            preRunPool.sort(Comparator.comparingDouble(Individual::getScore));
            evolver.getPool().clear();
            for (int i=0;i<populationSize;i++) {
                if (i>=preRunPool.size()) continue;
                evolver.getPool().add(preRunPool.get(i));
            }
            runBatch(batchSize);
            System.out.println("Depth:"+depth+" combinedSize:"+evolver.getPool().size()+" - score: " + evolver.getBestScoringIndividual().getScore());
            return evolver.getPool();
        } else {
            for (int i = 0; i < count; i++) {
                evolver.getPool().clear();
                evolver.increasePopulation(populationSize);
                runBatch(batchSize);
                System.out.println("Depth:"+depth+" Pre-run:" + i + " - score: " + evolver.getBestScoringIndividual().getScore());
                preRunPool.add(evolver.getBestScoringIndividual());
            }
            return preRunPool;
        }

    }

    public void go() {
        init();

        //preRun(100, 5);
        //doRecursivePreRun();

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

            if (ind.getScore() > reduceCodeScoreThreshold) {
                if (!reduceCode) System.out.println("******************* REDUCE CODE NOW");
                reduceCode = true;
            } else {
                if (reduceCode) System.out.println("******************* DONT REDUCE CODE");
                reduceCode = false;
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
        List<Integer> regDList = new ArrayList<>();
        for (int run = 0; run < numberOfRuns; run++) {
            sm.reset();
            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), 0, randomSeed + (run * 123));
            numCycles += runSimpleMachine(sm);
            score = programEvaluator.evaluate(sm, individual.getDna(), 0);
            totalScore += score;
            regDList.add(sm.regD);
        }

        totalScore -= numCycles * 0.001;

        if (regDList.get(0).equals(regDList.get(1)) && regDList.get(0).equals(regDList.get(2))) {
            totalScore /= 2;
        }

        // increase score for fewer instructions
        double noOpScore = 0.00001;
        if (reduceCode) noOpScore = 0.0001;
        totalScore -= (sm.countRealInstructions() * noOpScore) / numberOfRuns;

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
            programEvaluator.preEvaluateStep(sm, individual.getDna(), 0, randomSeed + (run * 123));
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
            int instruction = (int) (Math.abs(dnaVal) * sm.getMicrocode().maxOps);

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
