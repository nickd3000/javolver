package test;

import evo.Individual;

public class CWord extends Individual {

	static String targetWord = "ABCDEFGHIJKLMNOP";

	public CWord() {
		dna.init(targetWord.length());
	}
	
	public Individual clone()
	{
		return (Individual)(new CWord());
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
	
	public double calculateScore() {
		score = 0.0;
		
		for (int i=0;i<targetWord.length();i++)
		{
			score += getScoreForCharacter(dna.getChar(i), targetWord.charAt(i));
		}
		processed = true;
		return score;
	}
	
	
	// Helper methods
	double getScoreForCharacter(char a, char b)
	{
		int diff = Math.abs(a-b);
		if (diff>10) return 0.0;
		return (double)((10-diff)/10.0);
	}
	
		
		
}
