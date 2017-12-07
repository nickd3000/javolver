package testJavolver;

import java.awt.Color;
import javolver.Individual;
import com.physmo.toolbox.BasicDisplay;

/**
 * Nick Donnelly (Twitter: @nickd3000)
 * Example individual type that packs 2d circles tightly into a bounding box.
 */
public class CSpherePacker extends Individual {

	/**
	 * The word that we are trying to get our genetic algorithm to find.
	 */
	int numSpheres=9; // 9
	double coverage = 0;
	//static String targetWord = "ABCDEFGHIJKLMNOP";
	double overlapPenaltyScale = 0.25;
	
	public CSpherePacker(int numSpheres) {
		this.numSpheres = numSpheres;
		dna.init(numSpheres*3);
		for (int i=0;i<numSpheres*3;i+=3) {
			dna.set(i, 20 + Math.random()*100);
			dna.set(i+1, 20 + Math.random()*100);
			dna.set(i+2, Math.random()*50);
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
		double cover = 0.0;
		
		double x1,y1,r1,x2,y2,r2,d;
		double penalty=0;
		
		for (int i=0;i<numSpheres*3;i+=3) {	// Sphere loop 1
			x1=dna.getDouble(i);
			y1=dna.getDouble(i+1);
			r1=dna.getDouble(i+2);
			penalty+=getWallPenalty(x1, y1, r1);
			
			for (int j=0;j<numSpheres*3;j+=3) {	// Sphere loop 2
				if (i==j) continue;	// Don't compare against self.
				
				x2=dna.getDouble(j);
				y2=dna.getDouble(j+1);
				r2=dna.getDouble(j+2);
				d = getDistance(x1, y1, x2, y2);
				
				if (d<(r1+r2)) penalty+=((r1+r2)-d)*overlapPenaltyScale;
				
			}
			cover += Math.PI * (r1*r1);
		}

		for (int i=0;i<numSpheres*3;i+=3) {
			total += dna.getDouble(i+2); // add radii to score.
		}
		
		total -= (penalty*1.0);
		coverage = cover;
		
		return total;
	}
	
	public double getCoverage() {
		return coverage;
	}
	
	public double getWallPenalty(double x, double y, double r) {
		double w=200;
		double penalty = 0;
		double scale = 5; //12.5;
		
		if (x<r) penalty+=Math.abs((r-x)*scale);
		if (y<r) penalty+=Math.abs((r-y)*scale);
		if (x>w-r) penalty+=Math.abs(((r+w)-x)*scale);
		if (y>w-r) penalty+=Math.abs(((r+w)-y)*scale);
		
		return penalty;
	}
	
	// Helper methods
	
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
			disp.setDrawColor(disp.getDistinctColor(i, 0.8f));
			disp.drawFilledCircle(
					offsx + dna.getDouble(i), 
					offsy + dna.getDouble(i+1),
					dna.getDouble(i+2)*2);
		}
			
	}
}
