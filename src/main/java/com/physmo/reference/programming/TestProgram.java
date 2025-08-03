package com.physmo.reference.programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Spreader;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.reference.programming.simplemachinie.Decompiler;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class TestProgram {

    static int populationSize = 500;
    static int batchSize = 10;
    //ProgramEvaluator programEvaluator = new FunctionEvaluator();
    ProgramEvaluator programEvaluator = new ProblemMaxOfThree();
    //ProgramEvaluator programEvaluator = new ProblemSumTwo();
    //ProgramEvaluator programEvaluator = new ProblemOddsAndEvens();
    //ProgramEvaluator programEvaluator = new ProblemInvertNumber();
    //ProgramEvaluator programEvaluator = new ProblemFibonacci();
    Javolver evolver;
    boolean useOffsetLoader = true;
    Decompiler decompiler = new Decompiler();

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
                .addMutationOperator(new MutationOperatorSimple(10, 1.5, 0.99))
                .addMutationOperator(new MutationOperatorSimple(1, 10.0, 0.1))
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

    public void go() {
        init();

        for (int j = 0; j < 50000; j++) {

            runBatch(100);

            System.out.println("iteration: " + evolver.getIteration() + "  best score:" + evolver.getBestScoringIndividual().getScore());
            Individual ind = evolver.getBestScoringIndividual();
            System.out.println(showIndividual(ind));

            if (j % 5 == 0) {
                System.out.println("================ Spreading");
                Spreader spreader = new Spreader(0.00001, -1, 1);
                for (int i = 0; i < 10; i++) {
                    spreader.spread(evolver.getPool());
                }
            }

            if (j % 10 == 0) {
                printDecompilation();
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

    // Do a few runs to gather the best from a number of batches.
    public void runGatherBestBatches() {
        List<Individual> bestIndividuals = new ArrayList<>();
        init();
        int numBatches = 100;
        for (int i = 0; i < numBatches; i++) {
            evolver.getPool().clear();
            evolver.init();
            runBatch(20);
            bestIndividuals.add(evolver.getBestScoringIndividual());
            System.out.println("Batch gathering " + i + "/" + numBatches + " - best score " + evolver.getBestScoringIndividual().getScore());
            printDecompilation();
        }

        // Copy best individuals to pool
        evolver.getPool().clear();
        for (Individual bestIndividual : bestIndividuals) {
            evolver.getPool().add(bestIndividual);
        }

        for (int i = 0; i < 1000; i++) {
            runBatch(250);
            bestIndividuals.add(evolver.getBestScoringIndividual());

            System.out.println("  best score:" + evolver.getBestScoringIndividual().getScore());
            Individual ind = evolver.getBestScoringIndividual();
            System.out.println(showIndividual(ind));

            printDecompilation();
        }

    }

    public List<Individual> iterativeRun(int depth) {
        int maxDepth = 15;
        List<Individual> bestIndividuals = new ArrayList<>();
        if (depth < maxDepth) {
            bestIndividuals.addAll(iterativeRun(depth + 1));
            bestIndividuals.addAll(iterativeRun(depth + 1));
            fillPoolFromList(bestIndividuals);
            runBatch(50);
            System.out.println("depth: " + depth + " count:" + bestIndividuals.size() + "  score:" + evolver.getBestScoringIndividual().getScore());
            System.out.println(showIndividual(evolver.getBestScoringIndividual()));

            printDecompilation();
        } else {
            evolver.getPool().clear();
            evolver.init();
            runBatch(50);
        }

        bestIndividuals.add(evolver.getBestScoringIndividual());
        return bestIndividuals;
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
        int numberOfRuns = 25;
        double totalScore = 0, score = 0;
        sm = new SimpleMachine2();
        int numCycles = 0;
        for (int run = 0; run < numberOfRuns; run++) {
            sm.reset();
            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), 0);
            numCycles += runSimpleMachine(sm);
            score = programEvaluator.evaluate(sm, individual.getDna(), 0);
            totalScore += score;

        }

        totalScore -= numCycles * 0.001;

        // Check sameness - we don't want it to settle on the average expected value.
        int hash[] = new int[3];
        for (int i = 0; i < 3; i++) {
            sm.reset();
            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), 0);
            runSimpleMachine(sm);
            hash[i] = programEvaluator.getOutputValueHash(sm, individual.getDna());
        }
        if (hash[0] == hash[1] && hash[0] == hash[2]) totalScore /= 100.0;


        // increase score for fewer instructions
        double noOpScore = 0.001;
        for (int i = 0; i < sm.memory.length; i++) {
            if (sm.memory[i] == 0) totalScore += noOpScore;
        }


        return totalScore;
    }

    public String showIndividual(Individual individual) {

        SimpleMachine2 sm;
        int numberOfRuns = 3;
        double totalScore = 0, score = 0;
        String report = "";

        for (int run = 0; run < numberOfRuns; run++) {
            sm = new SimpleMachine2();

            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), 0);
            runSimpleMachine(sm);
            score = programEvaluator.evaluate(sm, individual.getDna(), 0);
            totalScore += score;
            report = report + programEvaluator.report(sm, individual.getDna()) + System.lineSeparator();
        }

        return report;
    }

    // Setup and also run simple machine, return console result.
    public void setupSimpleMachineFromDNA(SimpleMachine2 sm, Individual individual) {
        if (useOffsetLoader) {
            setupSimpleMachineFromDNA_Offset(sm, individual);
            return;
        }
        for (int i = 0; i < individual.getDna().getData().length; i++) {
            int instruction = (int) (individual.getDna().getDouble(i) * 0xff * 2);
            sm.memory[i++] = instruction;
        }
    }

    // Offset style
    // first section contains pointers to later sectins of code
    public void setupSimpleMachineFromDNA_Offset(SimpleMachine2 sm, Individual individual) {
        int controlLength = 20;
        int outputIndex = 10;
        for (int i = 0; i < controlLength; i += 2) {
            int position = (int) (individual.getDna().getDouble(i) * 10);
            int length = (int) (individual.getDna().getDouble(i + 1) * 5);
            //if (length < 1) length = 1;

            for (int j = position + controlLength; j < position + controlLength + length; j++) {
                if (j >= individual.getDna().getSize() || j < 0) continue;
                int instruction = (int) (Math.abs(individual.getDna().getDouble(j)) * 10);
                if (outputIndex >= sm.memory.length) continue;
                sm.memory[outputIndex++] = instruction;
            }

            if (outputIndex > sm.memSize) break;

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

    public void render(BasicDisplay bd, Individual individual) {

        SimpleMachine2 sm = null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score = 0, stepScore = 0;

        bd.getDrawingContext().cls(Color.lightGray);

        for (int step = 0; step < numberOfSteps * 10; step++) {
            sm = new SimpleMachine2();

            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), step / 10.0);
            runSimpleMachine(sm);

            programEvaluator.render(sm, individual.getDna(), bd, step / 10.0);

        }

        bd.repaint();

    }
}
