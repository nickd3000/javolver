package com.physmo.javolverexamples2.picturesolver;

import com.physmo.javolver.Attenuator;
import com.physmo.javolver.Individual;
import com.physmo.javolver.Optimizer;
import com.physmo.javolver.Solver;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.utils.BasicGraph;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PictureSolver {

    BufferedImage targetImage = null;
    BufferedImage workImage = null;
    int populationSize = 50;
    ImageComparer imageComparer;
    Graphics2D dc;
    int numObjects = 55; //50;
    int renderObjectCount = 5;
    DnaDrawer drawerClass;

    Attenuator attenuator = new Attenuator();

    public static void main(String[] args) throws FileNotFoundException {
        PictureSolver pictureSolver = new PictureSolver();
        pictureSolver.run();
    }

    public void run() {
        drawerClass = new DnaDrawerPolys();
        numObjects = 30;
        //drawerClass = new DnaDrawerSimpleSquares();numObjects = 80;

        //drawerClass = new DnaDrawerString(); numObjects=400;
        //drawerClass = new DnaDrawerCircles(); numObjects=80;


        try {
            targetImage = ImageIO.read(new File(String.valueOf(PictureSolver.class.getResource("/odin.jpg").getFile())));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }
        workImage = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        dc = workImage.createGraphics();
        imageComparer = new ImageComparer(targetImage);

        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());
        BasicDisplay dispGraph = new BasicDisplayAwt(400, 200);
        BasicGraph graph = new BasicGraph(500);

        attenuator.setScoreRange(0.70, 1.0);
        attenuator.addParameter("testImageSize", 25, 200);
        attenuator.addParameter("mutationAmount", 0.4, 0.01);
        attenuator.addParameter("renderObjectCount", numObjects, numObjects);
        attenuator.addParameter("temperature", 0.02, 0.0);

        Solver javolver;

        MutationStrategySimple mutationStrategySimple = new MutationStrategySimple(1, 0.1);

//        javolver = Javolver.builder()
//                .populationTargetSize(populationSize)
//                .dnaSize(numObjects * drawerClass.getObjectSize())
//                .keepBestIndividualAlive(false)
//                .addMutationStrategy(mutationStrategySimple)
//                .addMutationStrategy(new MutationStrategyShuffle(2))
//                .setSelectionStrategy(new SelectionStrategyTournament(0.2))
//                .setBreedingStrategy(new BreedingStrategyCrossover())
//                .scoreFunction(i -> calculateScore(i))
//                .build();

        javolver = Optimizer.builder()
                .dnaSize(numObjects * drawerClass.getObjectSize())
                .addMutationStrategy(mutationStrategySimple)
                .scoreFunction(i -> calculateScore(i))
                .build();


        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {

            javolver.doOneCycle();

            Individual top = javolver.findBestScoringIndividual();
            double topScore = top.getScore();
            attenuator.setCurrentScore(topScore);
            mutationStrategySimple.setAmount(attenuator.getValue("mutationAmount"));
            javolver.setTemperature(attenuator.getValue("temperature"));

            if (j % 20 == 0) {
                //Individual top = javolver.findBestScoringIndividual();

                disp.drawImage(targetImage, 0, 0);
                Graphics2D dc = workImage.createGraphics();
                drawerClass.render(dc, top.getDna(), renderObjectCount, workImage.getWidth(), workImage.getHeight());

                disp.drawImage(workImage, targetImage.getWidth(), 0);
                disp.repaint();
                //System.out.println("Score: " + (int) (top.getScore()) + " Iteration: " + j + "  (uamnt " + uamnt + "  freq: " + ufreq + ")" + "  M:" + slidingMutationAmount);
//
                graph.addData(top.getScore());
                dispGraph.cls(Color.WHITE);
                graph.draw(dispGraph, 0, 0, 400, 200, Color.BLUE);
                dispGraph.repaint();

                String str = "topScore:" + topScore + "  testImageSize: " + (int) attenuator.getValue("testImageSize");
                str += "   mutationAmount: " + attenuator.getValue("mutationAmount");
                str += "   renderObjectCount: " + (int) attenuator.getValue("renderObjectCount");
                System.out.println(str);

            }

            renderObjectCount = (int) attenuator.getValue("renderObjectCount");
        }

    }

    public double calculateScore(Individual i) {
        dc.setBackground(Color.GRAY);
        dc.clearRect(0, 0, workImage.getWidth(), workImage.getHeight());
        drawerClass.render(dc, i.getDna(), renderObjectCount, workImage.getWidth(), workImage.getHeight());
        double score = imageComparer.compareScaled(workImage, (int) attenuator.getValue("testImageSize"), 1);

        double penalty = drawerClass.getScoreAdjustments(i.getDna(), renderObjectCount, workImage.getWidth(), workImage.getHeight());

        return score - penalty;
    }
}
