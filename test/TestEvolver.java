package test;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import javolver.*;
//import javolver.Javolver.SelectionType;


/*
 * Test / Example class.
 */
public class TestEvolver {


	//
	public static void main(String[] args) {

		/*
		 * A few example classes, each one sets up an evolver with
		 * the required individuals and starts the evolution running.
		 * Simply uncomment the one you want to try.
		 */
		
		/*
		 * CWord - A very simple example where the goal is to match the output to
		 * a supplied string.
		 */
		//testWord();
		
		/*
		 * CSpherePacker - Attempts to fit a number of arbitrarily sized circles
		 * into a square as tightly as possible with graphical output.
		 */
		//testSpherePacker();
		
		/*
		 * GeneTree - Attempts to evolve a tree that fits certain structural
		 * criteria: height, not escaping the bounding box. The score of each
		 * individual is reduced for leaves that are 'under' other leaves, in an
		 * attempt to simulate leaves requiring sunlight.
		 */
		testTree();
		
		//testPicSolver();
		
		//testProgram(); // Not working very well (or at all).
	}

	/**
	 * Anneal function is used to pass values to the evolver config settings.
	 * This is a helper function to blend two doubles between each other
	 * spanning the expected number of iterations required.
	 * 
	 * @param v1			First value in range.
	 * @param v2			Second value in range.
	 * @param maxIterations	Number of iterations during which return value will blend from v1 to v2
	 * @param i				Current iteration.
	 * @return				Blended value based on v1 and v2
	 */
	public static double anneal(double v1, double v2, int maxIterations, int i) {
		if (i>maxIterations) i=maxIterations;
		double p = (double)i / (double)maxIterations;
		return v1+((v2-v1)*p);
	}
	
	public static void testPicSolver() {
		
		
		int populationSize = 25;
		BufferedImage targetImage = null;
		try {
		    targetImage = ImageIO.read(new File("mona_lisa.jpg"));
			//targetImage = ImageIO.read(new File("odin.jpg"));
		} catch (IOException e) {
			System.out.println("Image not found.");
		}
		
		BasicDisplay disp = new BasicDisplay(targetImage.getWidth()*2, targetImage.getHeight());
		
		Javolver testEvolver = new Javolver(new CPicSolver(targetImage), populationSize);
		
		// Configure the engine (Not required).
		testEvolver.config.keepBestIndividualAlive = true;
		testEvolver.config.mutationCount=4;
		testEvolver.config.mutationAmount=0.025;
		testEvolver.config.allowSwapMutation=true;
		testEvolver.config.selectionType = JavolverSelection.SelectionType.TOURNAMENT;
		testEvolver.config.selectionRange = 0.2;
		testEvolver.config.selectionUseScoreRank = true;
		testEvolver.config.selectionUseDiversityRank = false;
		testEvolver.config.breedMethod = JavolverBreed.BreedMethod.CROSSOVER;
		testEvolver.config.parallelScoring = false;
		
		// Perform a few iterations of evolution.
		for (int j = 0; j < 30000; j++) {

			// Call the evolver class to perform one evolution step.
			testEvolver.doOneCycle();
			
			if (j%1==0) { 
				CPicSolver top = (CPicSolver)testEvolver.findBestScoringIndividual(null);
				disp.drawImage(targetImage, 0,0);
				disp.drawImage(top.getImage(), targetImage.getWidth(),0);
				disp.refresh();
			}
			
			// Print output every so often.
			System.out.println("Iteration " + j + "  " + testEvolver.report());
		}
		
		//disp.dr
	}
	
	/**
	 * Example testing the CWord class.
	 * Individuals of type CWord are scored by how well they spell
	 * out a string compared to the supplied target word.
	 */
	public static void testWord() {

		int populationSize = 100;
		String targetWord = "HELLOWORLD";
		Javolver testEvolver = new Javolver(new CWord(targetWord), populationSize);
		
		// Configure the engine (Not required).
		testEvolver.config.keepBestIndividualAlive = false;
		testEvolver.config.mutationCount = 1;
		testEvolver.config.mutationAmount = 1.0/20.0;
		testEvolver.config.selectionType = JavolverSelection.SelectionType.TOURNAMENT;
		testEvolver.config.selectionRange = 0.25;
		//testEvolver.setDiversityAmount(1.0);
		
		// Perform a few iterations of evolution.
		for (int j = 0; j < 30; j++) {
			
			// Call the evolver class to perform one evolution step.
			testEvolver.doOneCycle();
			
			// Print output every so often.
			System.out.println("Iteration " + j + "  " + testEvolver.report());
		}

		System.out.print("END ");
	}
	

	
	
	public static void testSpherePacker() {

		BasicDisplay disp = new BasicDisplay(300, 300);
		int populationSize = 2000;
		int numberOfSpheres = 6;
		Javolver testEvolver = new Javolver(new CSpherePacker(numberOfSpheres),populationSize);


		// Configure the engine (Not required).
		testEvolver.config.keepBestIndividualAlive = false;
		testEvolver.config.mutationCount=2;
		testEvolver.config.mutationAmount=0.15*20;
		testEvolver.config.allowSwapMutation=true;
		testEvolver.config.selectionType = JavolverSelection.SelectionType.TOURNAMENT;
		testEvolver.config.selectionRange = 0.15;
		testEvolver.config.selectionUseScoreRank = true;
		testEvolver.config.selectionUseDiversityRank = false;
		testEvolver.config.breedMethod = JavolverBreed.BreedMethod.UNIFORM;
		testEvolver.config.parallelScoring = true;
		
		int boxSize = 200;
		Color boxCol = Color.DARK_GRAY;
		BasicDisplay.startTimer();
		
		for (int j = 0; j < 5000000; j++) {
			
			// Change the mutation amount during the simulation.
			testEvolver.config.mutationAmount = anneal(25,0.1,5000,j);
			
			// The main evolution function.
			testEvolver.doOneCycle();
			
			
			// Draw fittest individual every n frames.
			if ((j%25)==0) {
				// Find the best individual for drawing.
				CSpherePacker top = (CSpherePacker)testEvolver.findBestScoringIndividual(null);
				
				int pad = 50;
				disp.cls(new Color(149, 183, 213));
				top.draw(disp, pad,pad);
				
				disp.drawRect(pad+0, pad+0, pad+boxSize, pad+boxSize, boxCol);
				
				disp.refresh();
			}
			
			if ((j%50)==0) { // Print report every few iterations.
				
				double coverage = ((CSpherePacker)(testEvolver.findBestScoringIndividual(null))).getCoverage();
				
				System.out.println("Coverage " + coverage / (200.0*200.0) + "  Time: " + BasicDisplay.getEllapsedTime());
				BasicDisplay.startTimer();
			}
			
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	}
	
	

	public static void testTree() {
		
		DecimalFormat doubleFormat = new DecimalFormat("#.000");
		BasicDisplay disp = new BasicDisplay(400, 400);

		int popTargetSize=10;
		
		System.out.print("START");
		for (int n = 0; n < 500; n++) {
			// Create the evolver:
			Javolver testEvolver = new Javolver(new GeneTree(), popTargetSize);
		
			// Configure the engine (Not required).			
			testEvolver.config.SetKeepBestIndividualAlive(true)
				.SetMutationCount(5)
				.SetMutationAmount(0.85)
				.SetAllowSwapMutation(false)
				.SetSelectionType(JavolverSelection.SelectionType.TOURNAMENT)
				.SetSelectionRange(0.05)
				.SetSelectionUseScoreRank(false)
				.SetSelectionUseDiversityRank(true)
				.SetBreedMethod(JavolverBreed.BreedMethod.CROSSOVER)
				.SetParallelScoring(true);
			
			int iteration=0;
			int runLength=400;
			for (int j = 0; j < runLength; j++) {
				
				
				BasicDisplay.startTimer();
				
				//for (int i = 0; i < 1; i++) {
					//double mutationAmount = best.dna.getDouble(GeneTree.VAL_configMutationAmount);
					//testEvolver.config.mutationAmount = mutationAmount;
					//double selectionRange = best.dna.getDouble(GeneTree.VAL_configSelectionRange);
					//testEvolver.config.selectionRange = selectionRange;
					testEvolver.config.mutationAmount = anneal(0.2,0.01,runLength,j);
					//testEvolver.setDiversityAmount = anneal(5,0.01,runLength,j);
					
					testEvolver.doOneCycle();
					iteration++;
				//}
				
				
				
				
				testEvolver.report();
				
				//GeneTree best = (GeneTree) testEvolver.findBestScoringIndividual(null);

				disp.cls(new Color(149, 183, 213));

				GeneTree best = (GeneTree) testEvolver.findBestScoringIndividual(null);
				best.draw(disp, 200.0f, 350.0f);

				disp.drawLine(j, 50, j, 70, Color.GREEN);
				disp.refresh();
				//
				System.out.println("Iterations "+iteration+"   Timer: " + BasicDisplay.getEllapsedTime());
				

			}
			System.out.print("END");
		}

	}
	
	
	
	public static void testProgram() {


		int popTargetSize = 10000;
		Javolver testEvolver = new Javolver(new GeneProgram(), popTargetSize);

		// Configure the engine (Not required).
		testEvolver.config.keepBestIndividualAlive = true;
		testEvolver.config.mutationCount = 5;
		testEvolver.config.mutationAmount = ((1.0/250.0)*2060.0);
		testEvolver.config.allowSwapMutation=true;	
		testEvolver.config.selectionType = JavolverSelection.SelectionType.TOURNAMENT;
		testEvolver.config.selectionRange = 0.15;
		testEvolver.config.selectionUseScoreRank = true;
		testEvolver.config.selectionUseDiversityRank = true;
		testEvolver.config.breedMethod = JavolverBreed.BreedMethod.CROSSOVER;
		testEvolver.config.parallelScoring = false;
		
		//testEvolver.setDiversityAmount(50);
	
		
		int iteration=0;

		for (int j = 0; j < 50000; j++) {
			
			BasicDisplay.startTimer();
			
			for (int i = 0; i < 100; i++) {
				testEvolver.doOneCycle();
				iteration++;
			}
			
			System.out.print("iteration: " + iteration + "  Time in ms: " + BasicDisplay.getEllapsedTime() + "  ");
			testEvolver.report();
			System.out.println(testEvolver.findBestScoringIndividual(null).toString());
			
		}

		System.out.print("END ");
	}
	
	
	/*
	public static void testTextPic() {
		
		
		BasicDisplay disp = new BasicDisplay(800, 400);
		GeneTextPic testGene = new GeneTextPic();

		int count = 0;
		for (int i = 0; i < 10000000; i++) {
			testGene.init();
			disp.cls(new Color(149, 183, 213));
			testGene.draw(disp, 0, i / 10);
			disp.refresh();
			count++;
			if (count > 300) {
				disp.refresh();
				count = 0;
			}
		}
		disp.close();

	}
*/
	
	


}
