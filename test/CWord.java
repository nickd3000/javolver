package test;

import javolver.Individual;

/**
 * @author nick
 * Example individual type that evolves towards the supplied string.
 */
public class CWord extends Individual {

	public String targetWord = "EVOLUTION";

	public CWord(String target) {
		targetWord = target;
		dna.init(targetWord.length());
	}
	
	public Individual clone()
	{
		return (Individual)(new CWord(targetWord));
	}

	public String toString()
	{
		String str = "";
		for (int i=0;i<dna.getData().size();i++)
		{
			str = str + dna.getChar(i);
		}
		return str;
	}
	
	// Compare each character in the string to the target string and return a score.
	// Each character gets a higher score the closer it is to the target character.
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
