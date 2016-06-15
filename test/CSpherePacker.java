package test;

import java.awt.Color;

import javolver.BasicDisplay;
import javolver.Individual;

/**
 * Nick Donnelly (Twitter: @nickd3000)
 * Example individual type that evolves towards the supplied string.
 */
public class CSpherePacker extends Individual {

	/**
	 * The word that we are trying to get our genetic algorithm to find.
	 */
	int numSpheres=9; // 9
	
	//static String targetWord = "ABCDEFGHIJKLMNOP";

	
	public CSpherePacker(int numSpheres) {
		this.numSpheres = numSpheres;
		dna.init(numSpheres*3);
		for (int i=0;i<numSpheres*3;i+=3) {
			dna.set(i, 20 + Math.random()*150);
			dna.set(i+1, 20 + Math.random()*150);
			dna.set(i+2, Math.random()*5);
		}
	}
	
	/**
	 * 
	 */
	public Individual clone()
	{
		return (Individual)(new CSpherePacker(this.numSpheres));
	}

	/**
	 * 
	 */
	public String toString()
	{
		String str = "";
		for (int i=0;i<dna.getData().size();i++)
		{
			str = str + dna.getChar(i);
		}
		return str;
	}
	
	/**
	 * Compare each character in the string to the target string and return a score.
	 * Each character gets a higher score the closer it is to the target character.
	 */
	public double calculateScore() {
		double total = 0.0;
		
		/*
		for (int i=0;i<targetWord.length();i++)
		{
			total += getScoreForCharacter(dna.getChar(i), targetWord.charAt(i));
		}
		processed = true;
		 */
		double x1,y1,r1,x2,y2,r2,d;
		int penalty=0;
		for (int i=0;i<numSpheres*3;i+=3) {
			x1=dna.getDouble(i);
			y1=dna.getDouble(i+1);
			r1=dna.getDouble(i+2);
			penalty+=getWallPenalty(x1, y1, r1);
			for (int j=0;j<numSpheres*3;j+=3) {
				if (i==j) continue;
				
				x2=dna.getDouble(j);
				y2=dna.getDouble(j+1);
				r2=dna.getDouble(j+2);
				d = getDistance(x1, y1, x2, y2);
				if (d<(r1+r2)) penalty+=(r1+r2)-d;
				//if (x1<r1 || y1<r1 || x1+r1>200 || y1+r1>200) penalty++;
				//if (x2<r2 || y2<r2 || x2+r2>200 || y2+r2>200) penalty++;
			}
		}

		for (int i=0;i<numSpheres*3;i+=3) {
			total += dna.getDouble(i+2); // add radii to score.
		}
		
		total -= (penalty*1.0);
		
		return total;
	}
	
	public double getWallPenalty(double x, double y, double r) {
		double w=200;
		double penalty = 0;
		double scale = 1.5;
		
		if (x<r) penalty+=(r-x)*scale;
		if (y<r) penalty+=(r-y)*scale;
		if (x>w-r) penalty+=((r+w)-x)*scale;
		if (y>w-r) penalty+=((r+w)-y)*scale;
		
		
		return penalty;
	}
	
	// Helper methods
	
	/**
	 * Returns a higher value the closer the characters are.
	 */
	double getScoreForCharacter(char a, char b)
	{
		int maxDiff = 15;
		int diff = Math.abs(a-b);
		if (diff>maxDiff) return 0.0;
		return (double)((maxDiff-diff)/10.0);
	}
	
	double getDistance(double x1, double y1, double x2, double y2) {
		double dx = x2-x1;
		double dy = y2-y1;
		double d = (dx*dx)+(dy*dy);
		if (d<0.001) return 0.0;
		return Math.sqrt(d);
	}
		
	
	public void draw(BasicDisplay disp, float offsx, float offsy)
	{
		Color col_leaf = new Color(60,60,140,150);
				
		for (int i=0;i<numSpheres*3;i+=3) {
			disp.drawCircle(dna.getDouble(i), 30 + dna.getDouble(i+1),dna.getDouble(i+2)*2, col_leaf );
		}
			
		/*
		int numPoints = branchPoints.size();
		//float offsx = 200.0f;
		//float offsy = 350.0f;
		for (int i=0;i<numPoints;i+=2)
		{
			double x1 = branchPoints.get(i).x + offsx;
			double y1 = offsy - branchPoints.get(i).y;
			double x2 = branchPoints.get(i+1).x + offsx;
			double y2 = offsy - branchPoints.get(i+1).y;
			
			double dx = x1-x2;
			double dy = y1-y2;
			double d = (double) Math.sqrt((dx*dx)+(dy*dy));
			double thickness = 1+((d*d)/500.0f);
			disp.drawLine(x1, y1, x2, y2, col_wood, thickness);
		}
		
		for (int i=0;i<leafPoints.size();i++)
		{
			double x = offsx + leafPoints.get(i).x;
			double y = offsy - leafPoints.get(i).y;
			disp.drawCircle(x, y, 7, col_leaf);
		}*/
	}
}
