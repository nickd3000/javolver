package com.physmo.javolverexamples;

import com.physmo.javolver.Individual;

/**
 * @author nick
 * Example individual type that evolves towards the supplied string.
 */
public class GeneWord extends Individual {

	public String targetWord = "EVOLUTION";

	public GeneWord(String target) {
		targetWord = target;
		dna.init(targetWord.length());
	}
	
	public Individual clone()
	{
		return (Individual)(new GeneWord(targetWord));
	}

	public String toString()
	{
		String str = "";
		for (int i=0;i<dna.getData().length;i++)
		{
			str = str + dna.getChar(i);
		}
		//str += " score:"+score;
		return str;
	}
	
	// Compare each character in the string to the target string and return a score.
	// Each character gets a higher score the closer it is to the target character.
	@Override
	public double calculateScore() {
		double total = 0.0;
		
		for (int i=0;i<targetWord.length();i++)
		{
			total += getScoreForCharacter(dna.getChar(i), targetWord.charAt(i));
		}

		return total;
	}
	
	
	// Helper methods
	// Returns a higher value the closer the characters are.
	double getScoreForCharacter(char a, char b)
	{
		int maxDiff = 15;
		int diff = Math.abs(a-b);
		if (diff>maxDiff) return 0.0;
		return (double)((maxDiff-diff)/10.0);
	}
	
		
		
}
