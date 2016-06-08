package test;

//import evo.Chromosome;
//import evo.IGene;

public class GeneWord {
}

/*
public class GeneWord implements IGene {


	public Chromosome dna = new Chromosome();
	
	//public String word = "";
	float score = 0.0f;
	boolean scored = false;
	//int generation = 0;
	int age = 0;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		//word = "JWIDPIQX";
		for (int i=0;i<8;i++) dna.add((char)('A'+(char)(Math.random()*20)));
		//for (int i=0;i<8;i++) dna.add('A');
	}

	@Override
	public IGene newInstance()
	{
		return new GeneWord();
	}

	@Override
	public void mutate() {
		int pos = (int)((double)dna.size()*Math.random());
		int change = (int)((Math.random()-0.5)*5);
		dna.set(pos, (char)(dna.get(pos)+change));
	}

	@Override
	public void calculateScore() {
		age++;
		if (scored==true) return;
		
		String targetWord = "GENETICS";
		float theScore = 0.0f;
		for (int i=0;i<targetWord.length();i++)
		{
			theScore += getScoreForCharacter(targetWord.charAt(i), dna.get(i));
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
		
		GeneWord child = new GeneWord();
		child.dna = ((GeneWord)p1).dna.crossover(((GeneWord)p2).dna, 4);
		
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

		String word = "";
		for (int i=0;i<dna.size();i++) word=word+dna.get(i);
		int generation = dna.getGeneration();
		
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
		return dna.getGeneration();
		
	}


	@Override
	public void randomise() {
		// TODO Auto-generated method stub
		
	}


}
*/