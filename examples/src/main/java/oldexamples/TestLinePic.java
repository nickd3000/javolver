package com.physmo.javolverexamples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestLinePic {

	public static void main(String[] args) {

		testLinePic();

	}

	public static void testLinePic() {

		int populationSize = 3 * 5;
		BufferedImage targetImage = null;

		try {
			// targetImage = ImageIO.read(new File("mona_lisa.jpg"));
			targetImage = ImageIO.read(new File(String.valueOf(TestLinePic.class.getResource("/odin.jpg").getFile())));
			//targetImage = ImageIO.read(new File("odin.jpg"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}

		BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());

		Javolver testEvolver = new Javolver(new GeneLinePic(targetImage), populationSize);
		testEvolver.keepBestIndividualAlive(true).parallelScoring(true)
				.addMutationStrategy(new MutationStrategySimple(0.001, 0.0512))
				.addMutationStrategy(new MutationStrategySwap(0.01, 2))
				.setSelectionStrategy(new SelectionStrategyTournament(0.15))
				.setBreedingStrategy(new BreedingStrategyCrossover());


		// Perform a few iterations of evolution.
		for (int j = 0; j < 30000; j++) {

			// Call the evolver class to perform one evolution step.
			testEvolver.doOneCycle();
			//testEvolver.doOneCycleOfDescent();

			if (j % 10 == 0) {
				GeneLinePic top = (GeneLinePic) testEvolver.findBestScoringIndividual();
				disp.drawImage(targetImage, 0, 0);
				disp.drawImage(top.getImage(), targetImage.getWidth(), 0);
				disp.repaint(30);
				System.out.println("Score: " + top.getScore());
			}

			// Print output every so often.
			// System.out.println("Iteration " + j + " " + testEvolver.report());
		}

	}

}
