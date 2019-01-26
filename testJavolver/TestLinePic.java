package testJavolver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.physmo.toolbox.BasicDisplay;

import javolver.Javolver;
import javolver.breedingstrategy.BreedingStrategyCrossover;
import javolver.mutationstrategy.MutationStrategySimple;
import javolver.mutationstrategy.MutationStrategySwap;
import javolver.selectionstrategy.SelectionStrategyTournament;

public class TestLinePic {

	public static void main(String[] args) {

		testLinePic();

	}

	public static void testLinePic() {

		int populationSize = 3 * 1;
		BufferedImage targetImage = null;
		try {
			// targetImage = ImageIO.read(new File("mona_lisa.jpg"));
			targetImage = ImageIO.read(new File("odin.jpg"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}

		BasicDisplay disp = new BasicDisplay(targetImage.getWidth() * 2, targetImage.getHeight());

		Javolver testEvolver = new Javolver(new CLinePic(targetImage), populationSize);
		testEvolver.keepBestIndividualAlive(true).parallelScoring(false)
				.addMutationStrategy(new MutationStrategySimple(0.001, 0.0512))
				.addMutationStrategy(new MutationStrategySwap(0.01, 2))
				.setSelectionStrategy(new SelectionStrategyTournament(0.15))
				.setBreedingStrategy(new BreedingStrategyCrossover());


		// Perform a few iterations of evolution.
		for (int j = 0; j < 30000; j++) {

			// Call the evolver class to perform one evolution step.
			//testEvolver.doOneCycle();
			testEvolver.doOneCycleOfDescent();

			if (j % 100 == 0) {
				CLinePic top = (CLinePic) testEvolver.findBestScoringIndividual(null);
				disp.drawImage(targetImage, 0, 0);
				disp.drawImage(top.getImage(), targetImage.getWidth(), 0);
				disp.refresh();
				System.out.println("Score: " + top.getScore());
			}

			// Print output every so often.
			// System.out.println("Iteration " + j + " " + testEvolver.report());
		}

	}

}
