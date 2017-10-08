package testJavolver;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javolver.Individual;
import com.physmo.toolbox.BasicDisplay;
//import toolbox.BasicDisplay;

//import toolbox.BasicDisplay;


/**
 * @author nick
 *	Implements a recursive tree representation derived from the Javolver Individual type.
 */
public class GeneTree extends Individual {


	// Id's used to identify purpose of each DNA element.
	public int dnaLength = 20;
	static int VAL_trunkScale = 0; // How much the next segment is scaled by.
	static int VAL_trunkAngle = 1;
	static int VAL_branch1Pos = 2;
	static int VAL_branch1Scale = 3;
	static int VAL_branch1Angle = 4;
	static int VAL_branch2Pos = 5;
	static int VAL_branch2Scale = 6;
	static int VAL_branch2Angle = 7;
	static int VAL_trunkScale2 = 8; // How much the next segment is scaled by.
	static int VAL_trunkAngle2 = 9;
	static int VAL_branch1Pos2 = 10;
	static int VAL_branch1Scale2 = 11;
	static int VAL_branch1Angle2 = 12;
	static int VAL_branch2Pos2 = 13;
	static int VAL_branch2Scale2 = 14;
	static int VAL_branch2Angle2 = 15;
	public static int VAL_configMutationAmount = 16;
	public static int VAL_configSelectionRange = 17;
	public static int VAL_blendIteration = 18;
	
	
	// Lists of points used when scoring tree.
	ArrayList<Point2D.Double> leafPoints = null;
	ArrayList<Point2D.Double> branchPoints = null;
	int branchCount = 0;
	
	
	Color col_leaf = new Color(172,138,40,150);
	Color col_sky = new Color(100,150,194);
	Color col_wood = new Color(101,93,74);
	
	int generation = 0;
	
	
	public GeneTree() {
		dna.init(dnaLength);
		init();
	}
	
	public Individual clone()
	{
		return (Individual)(new GeneTree());
	}

	
	public void clampValues()
	{
		dna.clamp(VAL_trunkScale, 0, 0.86);
			
		dna.clamp(VAL_branch1Scale, 0.25, 0.87); // 0.86
		dna.clamp(VAL_branch2Scale, 0.25, 0.87);
		
		dna.clamp(VAL_branch1Pos, 0.25, 1.0);
		dna.clamp(VAL_branch2Pos, 0.25, 1.0);
		
		dna.clamp(VAL_branch1Angle, 0.1, 20.0);
		dna.clamp(VAL_branch2Angle, 0.1, 20.0);
		
		dna.clamp(VAL_trunkAngle, -1.0, 1.0);
		//dna.clamp(VAL_trunkAngle, 0.0, 00);
		
		///
		
		dna.clamp(VAL_trunkScale2, 0, 0.86);
		
		dna.clamp(VAL_branch1Scale2, 0.25, 0.87); // 0.86
		dna.clamp(VAL_branch2Scale2, 0.25, 0.87);
		
		dna.clamp(VAL_branch1Pos2, 0.25, 1.0);
		dna.clamp(VAL_branch2Pos2, 0.25, 1.0);
		
		dna.clamp(VAL_branch1Angle2, 0.1, 20.0);
		dna.clamp(VAL_branch2Angle2, 0.1, 20.0);
		
		dna.clamp(VAL_trunkAngle2, -1.0, 1.0);
		//dna.clamp(VAL_trunkAngle2, 0.0, 0.0);
		
		dna.clamp(VAL_configMutationAmount,0.001,1.0);
		dna.clamp(VAL_configSelectionRange,0.001,1.0);
		dna.clamp(VAL_blendIteration,0.001,2.0);
	}
	
	
	public void init() {
		randomise();
		leafPoints = new ArrayList<Point2D.Double>();
		branchPoints = new ArrayList<Point2D.Double>();
		branchPoints.ensureCapacity(5000);
	}


	
	public double calculateScore() {
		
		double scaleHeight = 0.001f;
		double scaleAverageDist=0.1f*0.1f;
		double scaleLeafBonus=0.1f;
		double leafCost=0.05f*1.0f;
		
		// Make sure we haven't evolved something crazy.
		clampValues();
		
		// Recursively generate the points of the tree,
		branchPoints.clear();
		leafPoints.clear();
		branchCount = 0;
		tree(0.0f, 0.0f, 60.0f, 0.0f, 0);
		
		int numLeaves = leafPoints.size();
		double maxLeafHeight = 0;
		double minLeafHeight = 0;
		int numLeavesBelowGround = 0;
		
		// Find maximum leaf height.
		for (Point2D.Double h : leafPoints)
		{
			if (h.y>maxLeafHeight) maxLeafHeight = h.y;
			if (h.y<minLeafHeight) minLeafHeight = h.y;
			if (h.y<0.0f) numLeavesBelowGround++;
		}
		
		
		double idealDiff = Math.abs(maxLeafHeight - 300.0f);
		if (idealDiff<100.0f)
		{
			score += ((100.0f - idealDiff)/100.0f) * scaleHeight;
		}
		
		double totalDist = 0.0f;
		double totalMaxDist = 0.0f;
		
		for (int i=0;i<numLeaves;i++)
		{
			Point2D.Double l1 = leafPoints.get(i);
			double minDist = 500.0f;
			double maxDist = 0.0f;
			double minXDist = 100.0f;
			
			for (int j=0;j<numLeaves;j++)
			{
				if (i==j) continue;
				
				Point2D.Double l2 = leafPoints.get(j);
				double dx = l1.x-l2.x;
				double dy = l1.y-l2.y;
				double d =  Math.sqrt((double)((dx*dx)+(dy*dy)));
				if (d<minDist) minDist = d;
				if (d>maxDist) maxDist = d;
				if (Math.abs(dx)<minXDist) minXDist = Math.abs(dx);
				
				// Penalty for leaves being above one another.
				if (Math.abs(dx)<3.0) score-=(leafCost*0.015);
			}	 
			totalDist+=minDist;
			totalMaxDist+=maxDist;
			
			//if (minDist>0.10f)
			//	score+= minDist * 0.015f;
			
			//if (minDist<=0.50f)
			//	score-=0.001f;
			
			// Add points for not being in shadow.
			//if (minXDist>10) score+=leafCost*0.1;
			
			// Each leaf has a cost.
			score-=leafCost;
		}
		
		
		score += (totalDist/(float)(numLeaves*numLeaves))*scaleAverageDist;
		//score += (totalMaxDist/(float)(numLeaves*numLeaves))*scaleAverageDist;
		
		
		// Favour large branch angles.
		score += dna.getDouble(VAL_branch1Angle)*0.002;
		score += dna.getDouble(VAL_branch2Angle)*0.002;
		
		// Bonus for every leaf.
		score += (float)leafPoints.size()*scaleLeafBonus;
		
		// Penalty for leaves being below ground.
		score -= numLeavesBelowGround * 0.1f;
		
		//scored=true;
		
		return score;
	}


	
	public String toString() {
		//String ret = String.format("%d %f %f %f %f %f a%f %f %f",
		//		branchPoints.size(),score,dna[0],dna[1],dna[2],dna[3],
		//		dna[4]*57.29577,dna[5],dna[6]);
		
		StringBuilder sb = new StringBuilder(200);
		sb.append(String.format("Score:%f Gen:%d \n", score, generation));
		
		sb.append(String.format("TRS:%.2f TRA:%.2f  --  ", dna.getDouble(VAL_trunkScale),dna.getDouble(VAL_trunkAngle)));
		sb.append(String.format("B1P:%.2f B1S:%.2f B1A:%.2f  --  ", dna.getDouble(VAL_branch1Pos),dna.getDouble(VAL_branch1Scale),dna.getDouble(VAL_branch1Angle)));
		sb.append(String.format("B2P:%.2f B2S:%.2f B2A:%.2f ", dna.getDouble(VAL_branch2Pos),dna.getDouble(VAL_branch2Scale),dna.getDouble(VAL_branch2Angle)));
				
		
		
		return sb.toString();
	}
	


	public void draw(BasicDisplay disp, float offsx, float offsy)
	{

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
			disp.setDrawColor(col_wood);
			disp.drawLine(x1, y1, x2, y2, thickness);
		}
		
		disp.setDrawColor(col_leaf);
		
		for (int i=0;i<leafPoints.size();i++)
		{
			double x = offsx + leafPoints.get(i).x;
			double y = offsy - leafPoints.get(i).y;
			
			disp.drawCircle(x, y, 7);
		}
	}
	
	public void tree(double x, double y, double len, double angle, int iteration)
	{
		branchCount ++;
		//if (branchCount>4000) return;
		
		if (len<23.0) //23.0 25
		{
			// Record leaf point and exit iteration.
			Point2D.Double leafPos = new Point2D.Double(x,y);
			leafPoints.add(leafPos);
			return;
		}
			
		double x2 = (float) (x + Math.sin((double)angle) * len);
		double y2 = (float) (y + Math.cos((double)angle) * len);

		double br1x = (float) (x + Math.sin((double)angle) * (len*getBlended(VAL_branch1Pos, iteration)));
		double br1y = (float) (y + Math.cos((double)angle) * (len*getBlended(VAL_branch1Pos, iteration)));
		
		double br2x = (float) (x + Math.sin((double)angle) * (len*getBlended(VAL_branch2Pos, iteration)));
		double br2y = (float) (y + Math.cos((double)angle) * (len*getBlended(VAL_branch2Pos, iteration)));

		
		// Record position for later scoring.
		Point2D.Double branchPos = new Point2D.Double(x,y);
		branchPoints.add(branchPos);
		branchPos = new Point2D.Double(x2,y2);
		branchPoints.add(branchPos);

		tree(x2,y2,len*getBlended(VAL_trunkScale, iteration),angle+getBlended(VAL_trunkAngle, iteration), iteration+1);
		tree(br1x,br1y,len*getBlended(VAL_branch1Scale, iteration),angle-getBlended(VAL_branch1Angle, iteration), iteration+1);
		tree(br2x,br2y,len*getBlended(VAL_branch2Scale, iteration),angle+getBlended(VAL_branch2Angle, iteration), iteration+1);
		
	}
	
	
	// Blend between a and b variation of the dna element based on the iteration number.
	public double getBlended(int id, int iteration) {
		
		int indexDiff = VAL_trunkScale2-VAL_trunkScale;
		
		double maxIterations = 10.0;
		maxIterations = dna.getDouble(VAL_blendIteration) * 10.0;
		if (maxIterations<1) maxIterations=1;
		if (maxIterations>15) maxIterations=15;
		
		double d1 = dna.getDouble(id);
		double d2 = dna.getDouble(id+indexDiff);
		double d = 0;
		double f = (double)iteration/maxIterations;
		if (f>1.0) f=1.0;
		
		d = d1+((double)(d2-d1)*f);
		
		
		return d;
	}
	
	

	
	public boolean canMate(Individual partner, boolean excludeSimilar) {

		/*
		float diff = 0.0f;
		
		for (int i=0;i<VAL_branch2Angle;i++)
		{
			diff += Math.abs(dna.getDouble(i)-((GeneTree)partner).dna.getDouble(i));
		}
		
		//if (excludeSimilar==true && diff<0.10f) return false;
		//if (diff>1.50f) return false;
		
		int generationDifference = Math.abs(this.getGeneration()-partner.getGeneration());
		if (generationDifference>10) return false;
		*/
		
		return true;
	}

	
	public int getGeneration() {
		return generation;
	}

	
	public void randomise() {
		for (int i=0;i<dnaLength;i++)
		{
			dna.set(i, (Math.random()-0.5)*2.0);
		}
		clampValues();
	}
	
}



