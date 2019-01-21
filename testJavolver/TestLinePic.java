package testJavolver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.physmo.toolbox.BasicDisplay;

import javolver.Javolver;

public class TestLinePic {

	public static void main(String[] args) {

		testLinePic();

	}

	public static void testLinePic() {

		int populationSize = 25*3;
		BufferedImage targetImage = null;
		try {
		    //targetImage = ImageIO.read(new File("mona_lisa.jpg"));
			targetImage = ImageIO.read(new File("odin.jpg"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
		
		BasicDisplay disp = new BasicDisplay(targetImage.getWidth()*2, targetImage.getHeight());
		
		Javolver testEvolver = new Javolver(new CLinePic(targetImage), populationSize);
		testEvolver
		.keepBestIndividualAlive(false)
		.parallelScoring(false)
		.setDefaultStrategies();
		
		
		// Configure the engine (Not required).
//		testEvolver.config.keepBestIndividualAlive = false;
//		testEvolver.config.mutationCount=5;
//		testEvolver.config.mutationAmount=0.25;
//		testEvolver.config.allowSwapMutation=false;
//		testEvolver.config.selectionType = JavolverSelection.SelectionType.TOURNAMENT;
//		testEvolver.config.selectionRange = 0.12;
//		testEvolver.config.selectionUseScoreRank = true;
//		testEvolver.config.selectionUseDiversityRank = false;
//		//testEvolver.config.breedMethod = JavolverBreed.BreedMethod.CROSSOVER;
//		testEvolver.config.parallelScoring = true;
		
		// Perform a few iterations of evolution.
		for (int j = 0; j < 30000; j++) {

			// Call the evolver class to perform one evolution step.
			testEvolver.doOneCycle();
			
			if (j%10==0) { 
				CLinePic top = (CLinePic)testEvolver.findBestScoringIndividual(null);
				disp.drawImage(targetImage, 0,0);
				disp.drawImage(top.getImage(), targetImage.getWidth(),0);
				disp.refresh();
				System.out.println("Score: "+top.getScore());
			}
			
			// Print output every so often.
			//System.out.println("Iteration " + j + "  " + testEvolver.report());
		}
		
		}

	

}
