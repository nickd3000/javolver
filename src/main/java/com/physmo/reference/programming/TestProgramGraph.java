package com.physmo.reference.programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingoperator.BreedingOperatorUniform;
import com.physmo.javolver.mutationoperator.MutationOperatorSimple;
import com.physmo.javolver.mutationoperator.MutationOperatorSwap;
import com.physmo.javolver.selectionoperator.SelectionOperatorTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.reference.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestProgramGraph {

    static int populationSize = 500;
    static int batchSize = 10;
    ProgramEvaluator programEvaluator = new FunctionEvaluator();
    //ProgramEvaluator programEvaluator = new ProblemMaxOfThree();
    Javolver evolver;
    BasicDisplay bd;
    boolean useOffsetLoader = true;

    public static void main(String[] args) throws Exception {
        TestProgramGraph testProgram = new TestProgramGraph();
        testProgram.init();
        testProgram.go();
        //testProgram.iterativeRun(0);
    }

    public void init() {
        bd = new BasicDisplayAwt(400, 400);
        evolver = Javolver.builder()
                .dnaSize(300)
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(true)
                .addMutationOperator(new MutationOperatorSimple(10, 1.5, 0.5))
                .addMutationOperator(new MutationOperatorSimple(1, 5.0, 0.1))
                .addMutationOperator(new MutationOperatorSwap(0.1, 2))
                //.addMutationOperator(new MutationOperatorShuffle(2))
                //.addMutationOperator(new MutationStrategyRandomize(0.1))
                .setSelectionOperator(new SelectionOperatorTournament(0.25))
                //.setSelectionOperator(new SelectionStrategyRoulette())
                //.setBreedingOperator(new BreedingStrategyCrossover())
                .setBreedingOperator(new BreedingOperatorUniform())
                .scoreFunction(this::calculateScore)
                .build();
    }

    public void go() {


        int iteration = 0;

        for (int j = 0; j < 50000; j++) {

            for (int i = 0; i < batchSize; i++) {
                evolver.doOneCycle();
                iteration++;
            }

            Individual ind = evolver.getBestScoringIndividual();
            String report = "score: " + ind.getScore();

            System.out.print("iteration: " + iteration + "  " + report);
            //testEvolver.report();
            System.out.println(evolver.getBestScoringIndividual().toString());


            //((GeneProgram)ind).render(bd);
            render(bd, ind);
        }

        System.out.print("END ");
    }

    public List<Individual> iterativeRun(int depth) {
        int maxDepth = 10;
        List<Individual> bestIndividuals = new ArrayList<>();
        if (depth < maxDepth) {
            bestIndividuals.addAll(iterativeRun(depth + 1));
            bestIndividuals.addAll(iterativeRun(depth + 1));
            fillPoolFromList(bestIndividuals);
            runBatch(20);
            System.out.println("depth: " + depth + " count:" + bestIndividuals.size() + "  score:" + evolver.getBestScoringIndividual().getScore());
            //System.out.println(showIndividual(evolver.getBestScoringIndividual()));
            render(bd, evolver.getBestScoringIndividual());
        } else {
            evolver.getPool().clear();
            evolver.init();
            runBatch(20);
            render(bd, evolver.getBestScoringIndividual());
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

    public double calculateScore(Individual individual) {

        SimpleMachine2 sm = new SimpleMachine2();

        double score = 0, stepScore = 0;

        for (int x = 0; x < 400; x += 10) {
            //sm = new SimpleMachine();
            sm.reset();
            setupSimpleMachineFromDNA_Offset(sm, individual);
            int xx = (int) (x + (Math.random() * 10));
            programEvaluator.preEvaluateStep(sm, individual.getDna(), xx);
            runSimpleMachine(sm);
            stepScore = programEvaluator.evaluate(sm, individual.getDna(), xx);
            score += stepScore;

            //consoleOutput+=" "+stepScore;
        }

        // increase score for fewer instructions
        double noOpScore = 0.00;
        for (int i = 0; i < sm.memory.length; i++) {
            if (sm.memory[i] == 0) score += noOpScore;
        }

        return score;
    }

    public void runBatch(int batchSize) {
        for (int i = 0; i < batchSize; i++) {
            evolver.doOneCycle();
        }
    }

    // Setup and also run simple machine, return console result.
    public void setupSimpleMachineFromDNA(SimpleMachine2 sm, Individual individual) {
        if (useOffsetLoader) {
            setupSimpleMachineFromDNA_Offset(sm, individual);
            return;
        }

        List<Integer> list = new LinkedList<>();

        for (int i = 0; i < individual.getDna().getData().length / 2; i++) {
            int instruction = (int) (individual.getDna().getDouble(i * 2) * 30.0); //250
            double position = BasicDisplay.clamp(0, 1.0, (individual.getDna().getDouble((i * 2) + 1)));
            int iPosition = (int) (list.size() * position);
            list.add(iPosition, instruction);
        }

        int i = 0;
        for (Integer instruction : list) {
            sm.memory[i++] = instruction;
        }

    }

    // Offset style
    // first section contains pointers to later sectins of code
    public void setupSimpleMachineFromDNA_Offset(SimpleMachine2 sm, Individual individual) {
        int controlLength = 30;
        int outputInidex = 10;
        for (int i = 0; i < controlLength; i += 2) {
            int position = (int) (individual.getDna().getDouble(i) * 10);
            int length = (int) (individual.getDna().getDouble(i + 1) * 5);
            if (length < 1) length = 1;

            for (int j = position + controlLength; j < position + controlLength + length; j++) {
                if (j >= individual.getDna().getSize() || j < 0) continue;
                int instruction = (int) (Math.abs(individual.getDna().getDouble(j)) * 10);
                if (outputInidex >= sm.memory.length) continue;
                sm.memory[outputInidex++] = instruction;
            }

            if (outputInidex > sm.memSize) break;

        }
    }

    public void runSimpleMachine(SimpleMachine2 sm) {
        double scorePenalty = 0;
        int maxCycles = 200 * 2;
        int cycleCount = 0;
        for (int i = 0; i < maxCycles; i++) {
            cycleCount++;
            int result = sm.runCycle();
//            if (sm.getMaxHits() > 20) {
//                scorePenalty = 20;
//                break;
//            }

            if (result == 1) break;
        }

        //String consoleOutput = sm.console;

        double cyclePenalty = (double) cycleCount / (double) maxCycles;

        //return sm.console;
    }

    public void render(BasicDisplay bd, Individual individual) {

        SimpleMachine2 sm = null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score = 0, stepScore = 0;

        bd.cls(Color.lightGray);

        for (int x = 0; x < 400; x++) {
            sm = new SimpleMachine2();

            setupSimpleMachineFromDNA_Offset(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), x);
            runSimpleMachine(sm);

            programEvaluator.render(sm, individual.getDna(), bd, x);

        }

        bd.repaint();

    }
}
