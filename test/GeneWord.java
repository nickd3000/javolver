package test;

import java.net.NetworkInterface;

import evo.IGene;

public class GeneWord implements IGene {

	public String word = "";
	float score = 0.0f;
	boolean scored = false;
	int generation = 0;
	int age = 0;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		word = "JWIDPIQX";
	}

	@Override
	public IGene newInstance()
	{
		return new GeneWord();
	}

	@Override
	public void mutate() {
		char[] chars = word.toCharArray();
		int pos = (int)(Math.random()*(float)word.length());
		if (Math.random()<0.5) chars[pos] = (char) (chars[pos]-1);
		else chars[pos] = (char) (chars[pos]+1);
		word = new String(chars);
	}

	@Override
	public void calculateScore() {
		age++;
		if (scored==true) return;
			
		String targetWord = "GENETICS";
		float theScore = 0.0f;
		for (int i=0;i<targetWord.length();i++)
		{
			theScore += getScoreForCharacter(targetWord.charAt(i), word.charAt(i));
		}
		score = theScore;
		scored = true;
	}

	@Override
	public float getScore() {
		return score;
	}


	@Override
	public IGene createChild(IGene p1, IGene p2) {
		
		String DNA1 = ((GeneWord)p1).word;
		String DNA2 = ((GeneWord)p2).word;
		String DNA3 = ((GeneWord)p2).word;
		char[] chars = DNA3.toCharArray();
		
		boolean side = false;
		for (int i=0;i<DNA1.length();i++)
		{
			if (Math.random()<0.25) { // Randomly switch side.
				if (side==true) side=false;
				else side = true;
			}
				
			if (side)	chars[i] = DNA1.charAt(i);
			else		chars[i] = DNA2.charAt(i);
		}
		
		DNA3 = new String(chars);
		
		GeneWord child = new GeneWord();
		child.word = DNA3;
		
		child.generation = Math.max(p1.getGeneration(), p2.getGeneration()) + 1;
		
		
		return child;
	}

	float getScoreForCharacter(char a, char b)
	{
		int diff = Math.abs(a-b);
		if (diff>10) return 0.0f;
		return ((10-diff)/10.0f);
	}


	@Override
	public String asString() {
		
		StringBuilder sb = new StringBuilder(200);
		sb.append(String.format("Score:%f Gen:%d  Word:%s\n", score, generation, word));
		return sb.toString();
		
	}


	@Override
	public int getAge() {
		// TODO Auto-generated method stub
		return age;
	}


	@Override
	public boolean canMate(IGene partner, boolean excludeSimilar) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public int getGeneration() {
		return generation;
		
	}


	@Override
	public void randomise() {
		// TODO Auto-generated method stub
		
	}


}
