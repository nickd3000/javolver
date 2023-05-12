package picturesolver;

import com.physmo.javolver.Attenuator;
import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategyShuffle;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Optimizer;
import com.physmo.javolver.solver.OptimizerES;
import com.physmo.javolver.solver.Solver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.utils.BasicGraph;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PictureSolver {

    BufferedImage targetImage = null;
    BufferedImage workImage = null;
    int populationSize = 40;
    ImageComparer imageComparer;
    Graphics2D dc;
    int numObjects = 55; //50;
    DnaDrawer drawerClass;
    Color graphBG = new Color(36, 68, 23);
    Color graphFG = new Color(171, 206, 64);

    Attenuator attenuator = new Attenuator();

    public static void main(String[] args) {
        PictureSolver pictureSolver = new PictureSolver();
        pictureSolver.run();
    }

    public void run() {
        //drawerClass = new DnaDrawerPolys(); numObjects = 50;
        //drawerClass = new DnaDrawerSimpleSquares();numObjects = 80;

        //drawerClass = new DnaDrawerString(); numObjects=400;
        drawerClass = new DnaDrawerCircles();numObjects = 250;



        try {
            targetImage = ImageIO.read(new File(String.valueOf(PictureSolver.class.getResource("/mona_lisa.jpg").getFile())));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }
        workImage = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dc = workImage.createGraphics();
        imageComparer = new ImageComparer(targetImage);

        BasicDisplay dispGraph = new BasicDisplayAwt(400, 200);
        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());

        BasicGraph graph = new BasicGraph(500);
        BasicGraph graph2 = new BasicGraph(500);

        MultiViewer multiViewer = new MultiViewer(800,800);

        attenuator.setScoreRange(0.60, 1.0);
        attenuator.addParameter("testImageSize", 60, 300);
        attenuator.addParameter("mutationAmount", 1, 0.5);
        attenuator.addParameter("renderObjectCount", numObjects, numObjects);

        Solver evolver;

        //MutationStrategySimple mutationStrategySimple = new MutationStrategySimple(1, 1);

        evolver = Javolver.builder()
                .populationTargetSize(populationSize)
                .dnaSize(numObjects * drawerClass.getObjectSize())
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySimple(2, 1))
                .addMutationStrategy(new MutationStrategySimple(8, 0.3))
//                .addMutationStrategy(new MutationStrategyShuffle(2))
//                .addMutationStrategy(new MutationStrategySwap(0.2,2))
                .setSelectionStrategy(new SelectionStrategyTournament(0.2))
                .setBreedingStrategy(new BreedingStrategyUniform())
                .scoreFunction(i -> calculateScore(i))
//                .setSpeciesCheck((i,j) -> isSameSpecies(i,j))
                .build();

//        evolver = Optimizer.builder()
//                .dnaSize(numObjects * drawerClass.getObjectSize())
//                .addMutationStrategy(mutationStrategySimple)
//                .scoreFunction(i -> calculateScore(i))
//                .build();


//        evolver = new OptimizerES();
//        evolver.setDnaSize(numObjects * drawerClass.getObjectSize());
//        evolver.setScoreFunction(this::calculateScore);
//        evolver.init();

        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {

            evolver.doOneCycle();

            Individual top = evolver.getBestScoringIndividual();
            double topScore = top.getScore();
            attenuator.setCurrentScore(topScore);
            double mutationAmount = attenuator.getValue("mutationAmount");
            evolver.setTemperature(mutationAmount);

            if (j % 10 == 0) {

                disp.drawImage(targetImage, 0, 0);
                Graphics2D dc = workImage.createGraphics();
                drawerClass.render(dc, top.getDna(), workImage.getWidth(), workImage.getHeight());

                disp.drawImage(workImage, targetImage.getWidth(), 0);
                disp.repaint();

                String str2 = String.format("i:%d topScore:%5.3f  testImageSize: %d   mutationAmount: %5.3f ", evolver.getIteration(), topScore, (int) attenuator.getValue("testImageSize"), attenuator.getValue("mutationAmount"));

                System.out.println(str2);

                if (multiViewer!=null) multiViewer.redraw(((Javolver)evolver).getPool(), drawerClass, workImage.getWidth(), workImage.getHeight());

            }


            if (j % 100 == 0) {
                graph2.addData(Math.pow(top.getScore(),2));
            }
            if (j % 10 == 0) {
                graph.addData(Math.pow(top.getScore(),2));
                dispGraph.cls(graphBG);
                graph.draw(dispGraph, 0, 0, 200, 200, graphFG);
                graph2.draw(dispGraph, 200, 0, 200, 200, graphFG);
                dispGraph.repaint();
            }


//            if (j % 30 == 0) {
//                System.out.println("Shake things up");
//                shakeThingsUp(((Javolver) evolver).getPool(), 0.02);
//            }
        }

    }

    public void shakeThingsUp(List<Individual> pool, double amount) {
        for (Individual individual : pool) {
            double[] data = individual.getDna().getData();
            for (int i=0;i<data.length;i++) {
                data[i]+=(Math.random()-0.5)*amount;
            }
        }

    }

    public double calculateScore(Individual i) {
        dc.setBackground(Color.GRAY);
        dc.clearRect(0, 0, workImage.getWidth(), workImage.getHeight());
        drawerClass.render(dc, i.getDna(), workImage.getWidth(), workImage.getHeight());
        double score = imageComparer.compareScaled(workImage, (int) attenuator.getValue("testImageSize"), 1);

        double penalty = drawerClass.getScoreAdjustments(i.getDna(), workImage.getWidth(), workImage.getHeight());

        return Math.max(score - penalty, 0);
    }

    private boolean isSameSpecies(Individual i1, Individual i2) {

        double d1[] = i1.getDna().getData();
        double d2[] = i2.getDna().getData();

        int differences = 0;
        double differenceThreshold = 0.001;
        int allowedDifferences = 3;

        for (int i = 0; i < d1.length; i++) {
            if (Math.abs(d1[i] - d2[i]) > differenceThreshold) differences++;
            if (differences > allowedDifferences) return false;
        }

        return true;
    }
}
