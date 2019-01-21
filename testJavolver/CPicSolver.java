package testJavolver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javolver.Individual;

/**
 * Created by nick on 20/07/2016.
 */
public class CPicSolver extends Individual {

	BufferedImage img;
	BufferedImage targetImage;
	int imgWidth=200;
	int imgHeight=200;
	int numPolys = 5;
	int numPoints = 30;					// Number of points per polygon.
	int stride = (numPoints*2)+4+1+2; 	// Number of data elements per poly.
	boolean enableTransparency = true;
	Graphics2D dc = null; //img.createGraphics();
	
	public CPicSolver(BufferedImage targetImage) {
		dna.init(numPolys*stride);
		
		this.targetImage = targetImage;
		imgWidth = targetImage.getWidth();
		imgHeight = targetImage.getHeight();
		img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		
		for (int i=0;i<numPolys;i++) {
			double x = Math.random() * 1.0;
			double y = Math.random() * 1.0;
			setPolyStart(i,x,y);
		}
		
		dc = img.createGraphics();
	}
	
	public void setPolyStart(int i, double x, double y) {
		int loc = i * stride;
		double arc = (Math.PI*2.0)/numPoints;
		double size = (1.0/200.0)*25.0;
		for (int j=0;j<numPoints;j++) {
			dna.set(loc+j, x + Math.sin(j*arc)*size);
			dna.set(loc+j+numPoints, y+ Math.cos(j*arc)*size);
		}
	}
	
	public BufferedImage getImage() {
		return img;
	}

	public void clampValues()
	{		
		for (int i=0;i<dna.getData().size();i++) {
			dna.clamp(i,0.0,1.0);
		}

	}

    private void drawPoly(Graphics2D dc, int index) {
    		// dna format xxx yyy rgb a
    		
    		int loc = index*stride;
    		
    		int [] xPoints = new int[numPoints];
    		int [] yPoints = new int[numPoints];
    		float [] cols = new float[4];
    		for (int i=0;i<numPoints;i++) {
    			xPoints[i] = (int)(dna.getDouble(loc+i)*imgWidth);
    			yPoints[i] = (int)(dna.getDouble(loc+i+numPoints)*imgHeight);
    		}
    		for (int i=0;i<4;i++) {
    			cols[i] = (float) dna.getDouble(loc+i+numPoints+numPoints);
    			if (cols[i]<0.0) cols[i] = 0.0f;
    			if (cols[i]>1.0) cols[i] = 1.0f;
    		}
    		
    		Color c = null;
    		if (enableTransparency) {
    			c = new Color(cols[0],cols[1],cols[2],cols[3]/2.0f); 
    		}
    		else {
    			c = new Color(cols[0],cols[1],cols[2]);
    		}
    		
    		dc.setColor(c);
    		
    		dc.fillPolygon(xPoints, yPoints, numPoints);
    }

	@Override
	public Individual clone() {
		return new CPicSolver(targetImage);
	}

	@Override
	public String toString() {
		return String.format("%.4f", score);
	}

	@Override
	public double calculateScore() {
		clampValues();
		
		//Graphics2D dc = img.createGraphics();
		
		dc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        dc.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        
		dc.setColor(Color.GRAY);
		dc.fillRect(0, 0, imgWidth, imgHeight);
		for (int i=0;i<numPolys;i++) {
			drawPoly(dc, i);
		}
	
		double total = 0;
		int count = 0;
		
		// Test random points.
		/*
		for (int i=0;i<10;i++) {
			total+=getScoreForPosition(
					(int)(Math.random()*imgWidth),
					(int)(Math.random()*imgHeight));
		}*/
		
//		int step=1;
//		for (int y=0;y<imgHeight;y+=step) {
//			for (int x=0;x<imgWidth;x+=step) {
//				total+=getScoreForPosition(x,y);
//				count++;
//			}
//		}
		
		
		//return (total/(double)count);
		
		double score = 0;
		score = testRandomPoints(10);
		//score = testGridOfPoints(3);
		
		return score;
	}
	
	public double testRandomPoints(int numPoints) {
		double total = 0;
		int count = 0;
		for (int i=0;i<numPoints;i++) {
			total+=getScoreForPosition(
					(int)(Math.random()*imgWidth),
					(int)(Math.random()*imgHeight));
			count++;
		}
		return (total/(double)count);
	}
	
	public double testGridOfPoints(int step) {
		double total = 0;
		int count = 0;;
		for (int y=0;y<imgHeight;y+=step) {
			for (int x=0;x<imgWidth;x+=step) {
				total+=getScoreForPosition(x,y);
				count++;
			}
		}
		return (total/(double)count);
	}
	
	public double getScoreForPosition(int x, int y) {
		int col1 = targetImage.getRGB(x, y);
		int col2 = img.getRGB(x, y);
		
	    int r1 = (col1>>16) & 0xff;
	    int g1 = (col1>>8) & 0xff;
	    int b1 = col1 & 0xff;
	    int r2 = (col2>>16) & 0xff;
	    int g2 = (col2>>8) & 0xff;
	    int b2 = col2 & 0xff;
		
	    r1=r1-r2;
	    g1=g1-g2;
	    b1=b1-b2;
	    
	    double dist = Math.sqrt((double)((r1*r1)+(g1*g1)+(b1*b1)));
	    if (dist<0) dist=0;
	    if (dist>100) dist=100;
	    
	    dist = (100.0 - dist) / 100.0;
	    
		return dist;
	}
}

