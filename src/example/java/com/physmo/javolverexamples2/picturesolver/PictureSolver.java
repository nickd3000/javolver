package com.physmo.javolverexamples2.picturesolver;

import com.physmo.javolver.Descent;
import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.mutationstrategy.MutationStrategy;
import com.physmo.javolver.mutationstrategy.MutationStrategyRandomize;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.javolverexamples.TestLinePic;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.utils.BasicGraph;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PictureSolver {

    int populationSize = 50; //100; //60; //0;//100;

    int numberOfDrawingElements = 30; //50;
    Class drawerClass = DnaDrawerPolys.class;
    //Class drawerClass = DnaDrawerSimpleSquares.class;
    //Class drawerClass = DnaDrawerString.class;
    //Class drawerClass = DnaDrawerCircles.class;


    public static void main(String[] args) throws FileNotFoundException {
        PictureSolver pictureSolver = new PictureSolver();
        pictureSolver.run();
    }

    public void run() {

        BufferedImage targetImage = null;
        try {
            targetImage = ImageIO.read(new File(String.valueOf(PictureSolver.class.getResource("/odin.jpg").getFile())));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }

        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());
        BasicDisplay dispGraph = new BasicDisplayAwt(400, 200);
        BasicGraph graph = new BasicGraph(500);


        Javolver javolver = Javolver.builder()
                .populationTargetSize(populationSize)
                .dnaSize(100)
                .keepBestIndividualAlive(false)
                .addMutationStrategy(new MutationStrategySimple(0.1, 0.2))
                .addMutationStrategy(new MutationStrategySwap(0.01, 1))
                .addMutationStrategy(new MutationStrategyRandomize(0.1))
                .setSelectionStrategy(new SelectionStrategyTournament(0.2))
                .setBreedingStrategy(new BreedingStrategyCrossover())
                .scoreFunction(i -> calculateScore(i))
                .build();


        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {
            javolver.doOneCycle();

//            if (j % 20 == 0) {
//                GenePicSolver top = (GenePicSolver) javolver.findBestScoringIndividual();
//                disp.drawImage(targetImage, 0, 0);
//                disp.drawImage(top.getImage(), targetImage.getWidth(), 0);
//                disp.repaint();
//                System.out.println("Score: " + (int)(top.getScore()) + " Iteration: " + j +  "  (uamnt " + uamnt + "  freq: " + ufreq + ")" + "  M:" + slidingMutationAmount);
//
//                graph.addData(top.getScore());
//                dispGraph.cls(Color.WHITE);
//                graph.draw(dispGraph, 0, 0, 400, 200, Color.BLUE);
//                dispGraph.repaint();
//            }

        }

    }

    public double calculateScore(Individual i) {
        return 0;
    }
}
