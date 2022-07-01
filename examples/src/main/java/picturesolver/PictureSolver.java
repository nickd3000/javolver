package picturesolver;

import com.physmo.javolver.Attenuator;
import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.OptimizerES;
import com.physmo.javolver.Solver;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.utils.BasicGraph;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureSolver {

    BufferedImage targetImage = null;
    BufferedImage workImage = null;
    int populationSize = 145;
    ImageComparer imageComparer;
    Graphics2D dc;
    int numObjects = 55; //50;
    DnaDrawer drawerClass;

    Attenuator attenuator = new Attenuator();

    public static void main(String[] args) {
        PictureSolver pictureSolver = new PictureSolver();
        pictureSolver.run();
    }

    public void run() {
        drawerClass = new DnaDrawerPolys(); numObjects = 45;
        //drawerClass = new DnaDrawerSimpleSquares();numObjects = 80;

        //drawerClass = new DnaDrawerString(); numObjects=400;
        //drawerClass = new DnaDrawerCircles();numObjects = 80;



        try {
            targetImage = ImageIO.read(new File(String.valueOf(PictureSolver.class.getResource("/odin.jpg").getFile())));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }
        workImage = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dc = workImage.createGraphics();
        imageComparer = new ImageComparer(targetImage);

        BasicDisplay dispGraph = new BasicDisplayAwt(400, 200);
        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());

        BasicGraph graph = new BasicGraph(500);

        MultiViewer multiViewer = null;//new MultiViewer(800,800);

        attenuator.setScoreRange(0.70, 0.9);
        attenuator.addParameter("testImageSize", 25, 300);
        attenuator.addParameter("mutationAmount", 1, 0.3);
        attenuator.addParameter("renderObjectCount", numObjects, numObjects);

        Solver evolver;

        //MutationStrategySimple mutationStrategySimple = new MutationStrategySimple(1, 1);

//        evolver = Javolver.builder()
//                .populationTargetSize(populationSize)
//                .dnaSize(numObjects * drawerClass.getObjectSize())
//                .keepBestIndividualAlive(false)
//                .addMutationStrategy(new MutationStrategySimple(2, 1))
//                .addMutationStrategy(new MutationStrategyShuffle(2))
//                .addMutationStrategy(new MutationStrategySwap(0.2,2))
//                .setSelectionStrategy(new SelectionStrategyTournament(0.1))
//                .setBreedingStrategy(new BreedingStrategyCrossover())
//                .scoreFunction(i -> calculateScore(i))
//                .build();

//        evolver = Optimizer.builder()
//                .dnaSize(numObjects * drawerClass.getObjectSize())
//                .addMutationStrategy(mutationStrategySimple)
//                .scoreFunction(i -> calculateScore(i))
//                .build();


        evolver = new OptimizerES();
        evolver.setDnaSize(numObjects * drawerClass.getObjectSize());
        evolver.setScoreFunction(this::calculateScore);
        evolver.init();

        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {

            evolver.doOneCycle();

            Individual top = evolver.getBestScoringIndividual();
            double topScore = top.getScore();
            attenuator.setCurrentScore(topScore);
            double mutationAmount = attenuator.getValue("mutationAmount");
            evolver.setTemperature(mutationAmount);

            if (j % 50 == 0) {

                disp.drawImage(targetImage, 0, 0);
                Graphics2D dc = workImage.createGraphics();
                drawerClass.render(dc, top.getDna(), workImage.getWidth(), workImage.getHeight());

                disp.drawImage(workImage, targetImage.getWidth(), 0);
                disp.repaint();

                graph.addData(top.getScore());
                dispGraph.cls(Color.WHITE);
                graph.draw(dispGraph, 0, 0, 400, 200, Color.BLUE);
                dispGraph.repaint();

                String str2 = String.format("i:%d topScore:%5.3f  testImageSize: %d   mutationAmount: %5.3f ", evolver.getIteration(), topScore, (int) attenuator.getValue("testImageSize"), attenuator.getValue("mutationAmount"));

                System.out.println(str2);

                if (multiViewer!=null) multiViewer.redraw(((Javolver)evolver).getPool(), drawerClass, workImage.getWidth(), workImage.getHeight());
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
}
