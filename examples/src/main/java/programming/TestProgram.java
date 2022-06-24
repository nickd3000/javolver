package programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.mutationstrategy.MutationStrategyShuffle;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class TestProgram {

    static int populationSize = 100;
    static int batchSize = 10;
    ProgramEvaluator programEvaluator = new FunctionEvaluator();

    public static void main(String[] args) throws Exception {
        TestProgram testProgram = new TestProgram();
        testProgram.go();
    }

    public void go() {
        BasicDisplay bd = new BasicDisplayAwt(400, 400);
        // FunctionEvaluator.class
        Javolver testEvolver = Javolver.builder()
                .dnaSize(150)
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySimple(2, 0.2))
                .addMutationStrategy(new MutationStrategySwap(0.1, 2))
                .addMutationStrategy(new MutationStrategyShuffle(2))
                //.addMutationStrategy(new MutationStrategyRandomize(0.1))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setBreedingStrategy(new BreedingStrategyCrossover())
                .scoreFunction(i -> calculateScore(i))
                .build();

        int iteration = 0;

        for (int j = 0; j < 50000; j++) {

            for (int i = 0; i < batchSize; i++) {
                testEvolver.doOneCycle();
                iteration++;
            }

            System.out.print("iteration: " + iteration + "  ");
            testEvolver.report();
            System.out.println(testEvolver.getBestScoringIndividual().toString());

            Individual ind = testEvolver.getBestScoringIndividual();
            //((GeneProgram)ind).render(bd);
            render(bd, ind);
        }

        System.out.print("END ");
    }

    public double calculateScore(Individual individual) {

        SimpleMachine sm = null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score = 0, stepScore = 0;

        for (int step = 0; step < numberOfSteps * 10; step++) {
            sm = new SimpleMachine();

            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), step / 10);
            runSimpleMachine(sm);
            stepScore = programEvaluator.evaluate(sm, individual.getDna(), step / 10);
            score += stepScore;

            //consoleOutput+=" "+stepScore;
        }

        return score;
    }

    // Setup and also run simple machine, return console result.
    public void setupSimpleMachineFromDNA(SimpleMachine sm, Individual individual) {

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


    public void runSimpleMachine(SimpleMachine sm) {
        double scorePenalty = 0;
        int maxCycles = 200 * 2;
        int cycleCount = 0;
        for (int i = 0; i < maxCycles; i++) {
            cycleCount++;
            int result = sm.runCycle();
            if (sm.getMaxHits() > 20) {
                scorePenalty = 20;
                break;
            }

            if (result == 1) break;
        }

        String consoleOutput = sm.console;

        double cyclePenalty = (double) cycleCount / (double) maxCycles;

        //return sm.console;
    }

    public void render(BasicDisplay bd, Individual individual) {

        SimpleMachine sm = null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score = 0, stepScore = 0;

        bd.cls(Color.lightGray);

        for (int step = 0; step < numberOfSteps * 10; step++) {
            sm = new SimpleMachine();

            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), step / 10.0);
            runSimpleMachine(sm);

            programEvaluator.render(sm, individual.getDna(), bd, step / 10.0);

        }

        bd.repaint();

    }
}
