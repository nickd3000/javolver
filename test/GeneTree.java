package test;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import evo.BasicDisplay;
import evo.IGene;


public class GeneTree implements IGene {

	public float dna[];
	public int dnaLength = 10;
	static int VAL_trunkScale = 0; // How much the next segment is scaled by.
	static int VAL_trunkAngle = 1;
	static int VAL_branch1Pos = 2;
	static int VAL_branch1Scale = 3;
	static int VAL_branch1Angle = 4;
	static int VAL_branch2Pos = 5;
	static int VAL_branch2Scale = 6;
	static int VAL_branch2Angle = 7;
	int age = 0;
	// Lists of points used when scoring tree.
	ArrayList<Point2D.Float> leafPoints = null;
	ArrayList<Point2D.Float> branchPoints = null;
	int branchCount = 0;
	boolean scored = false;
	
	Color col_leaf = new Color(172,138,40,150);
	Color col_sky = new Color(100,150,194);
	Color col_wood = new Color(101,93,74);
	
	float score = 0;
	int generation = 0;
	
	@Override
	public IGene newInstance()
	{
		return new GeneTree();
	}
	
	public void clampValues()
	{
		
		float max = 0.86f;
		float bam = 10.0f;
		if (dna[VAL_trunkScale]>max) dna[VAL_trunkScale]=max;
		if (dna[VAL_branch1Scale]>max) dna[VAL_branch1Scale]=max;
		if (dna[VAL_branch2Scale]>max) dna[VAL_branch2Scale]=max;
		if (dna[VAL_branch1Pos]>1.0f) dna[VAL_branch1Pos]=1.0f;
		if (dna[VAL_branch2Pos]>1.0f) dna[VAL_branch2Pos]=1.0f;
		if (dna[VAL_branch1Pos]<0.0f) dna[VAL_branch1Pos]=0.0f;
		if (dna[VAL_branch2Pos]<0.0f) dna[VAL_branch2Pos]=0.0f;
		if (dna[VAL_branch1Angle]<0.10f) dna[VAL_branch1Angle]=0.10f;
		if (dna[VAL_branch2Angle]<0.10f) dna[VAL_branch2Angle]=0.10f;
		if (dna[VAL_branch1Angle]>bam) dna[VAL_branch1Angle]=bam;
		if (dna[VAL_branch2Angle]>bam) dna[VAL_branch2Angle]=bam;
		if (dna[VAL_trunkAngle]>1.0f) dna[VAL_trunkAngle]=1.0f;
		if (dna[VAL_trunkAngle]<-1.0f) dna[VAL_trunkAngle]=-1.0f;
		
	}
	
	@Override
	public void init() {
		dna = new float[dnaLength];
		randomise();
		leafPoints = new ArrayList<Point2D.Float>();
		branchPoints = new ArrayList<Point2D.Float>();
		
		branchPoints.ensureCapacity(5000);
		//generation = 0;
	}

	@Override
	public void mutate() {
		/*
		int id = (int)((float)(dnaLength) * Math.random());
		float amount = ((float)Math.random()-0.5f) * 0.1f;
		dna[id] += amount;
		clampValues();
		*/
		float scale = 0.001f;
		scale = (float) Math.random() * 10.0f; //10.125f;
		for (int i=0;i<VAL_branch2Angle;i++)
		{
			dna[i] = (float) (dna[i] + ((Math.random()-0.5f)*scale));
		}
		clampValues();
	}

	@Override
	public void calculateScore() {
		
		float scaleHeight = 0.001f;
		float scaleAverageDist=0.1f;
		float scaleLeafBonus=0.1f;
		float leafCost=0.01f;
		
		age++;
		
		if (scored==true) return;
		
		branchPoints.clear();
		leafPoints.clear();
		branchCount = 0;
		tree(0.0f, 0.0f, 60.0f, 0.0f);
		
		int numLeaves = leafPoints.size();
		
		float maxLeafHeight = 0;
		float minLeafHeight = 0;
		int numLeavesBelowGround = 0;
		for (Point2D.Float h : leafPoints)
		{
			if (h.y>maxLeafHeight) maxLeafHeight = h.y;
			if (h.y<minLeafHeight) minLeafHeight = h.y;
			if (h.y<0.0f) numLeavesBelowGround++;
		}
		
		score -= numLeavesBelowGround * 0.1f;
		
		float idealDiff = Math.abs(maxLeafHeight - 300.0f);
		if (idealDiff<100.0f)
		{
			score += ((100.0f - idealDiff)/100.0f) * scaleHeight;
		}
		
		float totalDist = 0.0f;
		float totalMaxDist = 0.0f;
		//for (Point2D.Float l1 : leafPoints)
		for (int i=0;i<numLeaves;i++)
		{
			Point2D.Float l1 = leafPoints.get(i);
			float minDist = 500.0f;
			float maxDist = 0.0f;
			//for (Point2D.Float l2 : leafPoints)
			for (int j=0;j<numLeaves;j++)
			{
				if (i==j) continue;
				
				Point2D.Float l2 = leafPoints.get(j);
				float dx = l1.x-l2.x;
				float dy = l1.y-l2.y;
				float d = (float) Math.sqrt((double)((dx*dx)+(dy*dy)));
				if (d<minDist) minDist = d;
				if (d>maxDist) maxDist = d;
			}	 
			totalDist+=minDist;
			totalMaxDist+=maxDist;
			
			//if (minDist>0.10f)
			//	score+= minDist * 0.015f;
			
			if (minDist<=0.50f)
				score-=0.001f;
			
			score-=leafCost;
		}
		
		score += (totalDist/(float)(numLeaves*numLeaves))*scaleAverageDist;
		//score += (totalMaxDist/(float)(numLeaves*numLeaves))*scaleAverageDist;
		
		
		// Favour large branch angles.
		score += dna[VAL_branch1Angle]*0.002;
		score += dna[VAL_branch2Angle]*0.002;
		
		// Bonus for every leaf.
		score += (float)leafPoints.size()*scaleLeafBonus;
		
		scored=true;
	}

	@Override
	public float getScore() {
		

		return score;
	}

	@Override
	public IGene createChild(IGene p1, IGene p2) {
		
		GeneTree child = new GeneTree();
		child.init();
		
		boolean side=false;
		for (int i=0;i<dnaLength;i++)
		{
			if (Math.random()<0.25) { // Randomly switch side.
				if (side==true) side=false;
				else side = true;
			}
			
			if (side==true) child.dna[i] = ((GeneTree)p1).dna[i];
			else child.dna[i] = ((GeneTree)p2).dna[i];
		}
		
		child.generation = Math.max(p1.getGeneration(), p2.getGeneration()) + 1;
		
		return child;
	}

	@Override
	public String asString() {
		//String ret = String.format("%d %f %f %f %f %f a%f %f %f",
		//		branchPoints.size(),score,dna[0],dna[1],dna[2],dna[3],
		//		dna[4]*57.29577,dna[5],dna[6]);
		
		StringBuilder sb = new StringBuilder(200);
		sb.append(String.format("Score:%f Gen:%d \n", score, generation));
		
		sb.append(String.format("TRS:%.2f TRA:%.2f  --  ", dna[VAL_trunkScale],dna[VAL_trunkAngle]));
		sb.append(String.format("B1P:%.2f B1S:%.2f B1A:%.2f  --  ", dna[VAL_branch1Pos],dna[VAL_branch1Scale],dna[VAL_branch1Angle]));
		sb.append(String.format("B2P:%.2f B2S:%.2f B2A:%.2f ", dna[VAL_branch2Pos],dna[VAL_branch2Scale],dna[VAL_branch2Angle]));
				
		
		
		return sb.toString();
	}
	
	/*
	 * 	static int VAL_trunkScale = 0; // How much the next segment is scaled by.
	static int VAL_trunkAngle = 1;
	static int VAL_branch1Pos = 2;
	static int VAL_branch1Scale = 3;
	static int VAL_branch1Angle = 4;
	static int VAL_branch2Pos = 5;
	static int VAL_branch2Scale = 6;
	static int VAL_branch2Angle = 7;
	*/

	public void draw(BasicDisplay disp, float offsx, float offsy)
	{

		
		int numPoints = branchPoints.size();
		//float offsx = 200.0f;
		//float offsy = 350.0f;
		for (int i=0;i<numPoints;i+=2)
		{
			float x1 = branchPoints.get(i).x + offsx;
			float y1 = offsy - branchPoints.get(i).y;
			float x2 = branchPoints.get(i+1).x + offsx;
			float y2 = offsy - branchPoints.get(i+1).y;
			
			float dx = x1-x2;
			float dy = y1-y2;
			float d = (float) Math.sqrt((dx*dx)+(dy*dy));
			float thickness = 1+((d*d)/500.0f);
			disp.drawLine(x1, y1, x2, y2, col_wood, thickness);
		}
		
		for (int i=0;i<leafPoints.size();i++)
		{
			float x = offsx + leafPoints.get(i).x;
			float y = offsy - leafPoints.get(i).y;
			disp.drawCircle(x, y, 7, col_leaf);
		}
	}
	
	public void tree(float x, float y, float len, float angle)
	{
		branchCount ++;
		//if (branchCount>4000) return;
		
		
		if (len<25.0f)
		{
			// Record leaf point and exit iteration.
			Point2D.Float leafPos = new Point2D.Float(x,y);
			leafPoints.add(leafPos);
			return;
		}

		

			
		float x2 = (float) (x + Math.sin((double)angle) * len);
		float y2 = (float) (y + Math.cos((double)angle) * len);

		float br1x = (float) (x + Math.sin((double)angle) * (len*dna[VAL_branch1Pos]));
		float br1y = (float) (y + Math.cos((double)angle) * (len*dna[VAL_branch1Pos]));
		
		float br2x = (float) (x + Math.sin((double)angle) * (len*dna[VAL_branch2Pos]));
		float br2y = (float) (y + Math.cos((double)angle) * (len*dna[VAL_branch2Pos]));

		
		// Record position for later scoring.
		Point2D.Float branchPos = new Point2D.Float(x,y);
		branchPoints.add(branchPos);
		branchPos = new Point2D.Float(x2,y2);
		branchPoints.add(branchPos);


		
		tree(x2,y2,len*dna[VAL_trunkScale],angle+dna[VAL_trunkAngle]);
		tree(br1x,br1y,len*dna[VAL_branch1Scale],angle-dna[VAL_branch1Angle]);
		tree(br2x,br2y,len*dna[VAL_branch2Scale],angle+dna[VAL_branch2Angle]);
		
		
	}

	@Override
	public int getAge() {
		return age;
	}

	@Override
	public boolean canMate(IGene partner, boolean excludeSimilar) {
		
		
		
		float diff = 0.0f;
		
		for (int i=0;i<VAL_branch2Angle;i++)
		{
			diff += Math.abs(dna[i]-((GeneTree)partner).dna[i]);
		}
		
		//if (excludeSimilar==true && diff<0.10f) return false;
		//if (diff>1.50f) return false;
		
		int generationDifference = Math.abs(this.getGeneration()-partner.getGeneration());
		if (generationDifference>10) return false;
		
		return true;
	}

	@Override
	public int getGeneration() {
		return generation;
	}

	@Override
	public void randomise() {
		for (int i=0;i<dnaLength;i++)
		{
			dna[i] = (float)Math.random();
		}
		clampValues();
	}
	
}





