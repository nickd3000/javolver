package com.physmo.javolver.examples;

import java.awt.Color;

import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.mutationstrategy.MutationStrategySingle;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

public class SpherePacker {

	public static void main(String[] args) {


		BasicDisplay disp = new BasicDisplayAwt(300, 300);
		int populationSize = 500;
		int numberOfSpheres = 7;
		Javolver testEvolver = new Javolver(new GeneSpherePacker(numberOfSpheres),populationSize)
				.keepBestIndividualAlive(false)
				.parallelScoring(true)
				.setDefaultStrategies()
				.enableCompatability(0.1,0.6)
				.addMutationStrategy(new MutationStrategySingle(1.1))
				.addMutationStrategy(new MutationStrategySimple(1.1,0.1))
				.addMutationStrategy(new MutationStrategySwap(0.1, 1));

		
		int boxSize = 200;
		Color boxCol = Color.DARK_GRAY;
		disp.startTimer();
		
		for (int j = 0; j < 5000000; j++) {
			
			// Change the mutation amount during the simulation.
			//testEvolver.config.mutationAmount = anneal(5,0.1,15000,j);
			
			// The main evolution function.
			testEvolver.doOneCycle();
			
			
			// Draw fittest individual every n frames.
			//if ((j%25)==0) {
			if (disp.getEllapsedTime()>1000/30) {
				disp.startTimer();
				// Find the best individual for drawing.
				GeneSpherePacker top = (GeneSpherePacker)testEvolver.findBestScoringIndividual(null);
				
				int pad = 50;
				disp.cls(new Color(64, 64, 64));
				top.draw(disp, pad,pad);
				
				disp.setDrawColor(Color.white);
				disp.drawRect(pad, pad, pad+boxSize, pad+boxSize);
				
				disp.refresh();
			}
			
			if ((j%50)==0) { // Print report every few iterations.
				
				double coverage = ((GeneSpherePacker)(testEvolver.findBestScoringIndividual(null))).getCoverage();
				
				System.out.println("Coverage " + coverage / (200.0*200.0) + "  Time: " + disp.getEllapsedTime());
				//disp.startTimer();
			}
			
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	
	}
}



