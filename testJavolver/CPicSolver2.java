package testJavolver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javolver.Individual;

/**
 * Created by nick on 20/07/2016.
 */
public class CPicSolver2 extends Individual {

	BufferedImage img;
	BufferedImage targetImage;
	int imgWidth=200;
	int imgHeight=200;
	int numPolys = 200;
	int numPoints = 1;					// Number of points per polygon.
	int stride = 9; 	// Number of data elements per poly.
	boolean enableTransparency = true;
	double radiusDivider = 6.0;
	
	double overlapPenaltyMultiplier = 0.2; // 0.02
	double positionPenaltyMultiplier = 1.5; // 0.01
	
	public CPicSolver2(BufferedImage targetImage) {
		dna.init(numPolys*stride);
		
		this.targetImage = targetImage;
		imgWidth = targetImage.getWidth();
		imgHeight = targetImage.getHeight();
		img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		
		for (int i=0;i<numPolys;i++) {
			double x = Math.random() * 1.0;
			double y = Math.random() * 1.0;
			//setPolyStart(i,x,y);
		}
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

    public double checkOverlap(int index1, int index2) {
		// dna format xxx yyy rgb a
		
		int loc1 = index1*stride;
		int loc2 = index2*stride;
		
		int xpos = 0, ypos = 0, rad = 0;
		float [] cols = new float[4];
		
		double x1 = (dna.getDouble(loc1)*imgWidth);
		double y1 = (dna.getDouble(loc1+1)*imgHeight);
		double r1 = (dna.getDouble(loc1+2)*(imgWidth/5.0));

		double x2 = (dna.getDouble(loc1)*imgWidth);
		double y2 = (dna.getDouble(loc1+1)*imgHeight);
		double r2 = (dna.getDouble(loc1+2)*(imgWidth/5.0));
		
		double dist = ((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2));
		
		if (dist<0.001) return 1.0;
		
		dist = Math.sqrt(dist);
		
		if (dist>r1+r2) return 0;
		
		double norm = (dist - (r1+r2))/(r1+r2);
		 
		
		return norm;
    }
		
		
    private void drawPoly(Graphics2D dc, int index) {
    		// dna format xxx yyy rgb a
    		
    		int loc = index*stride;
    		
    		int xpos = 0, ypos = 0, rad = 0;
    		float [] cols = new float[4];
    		
			xpos = (int)(dna.getDouble(loc)*imgWidth);
			ypos = (int)(dna.getDouble(loc+1)*imgHeight);
			rad = (int)(dna.getDouble(loc+2)*(imgWidth/radiusDivider));
		
    		
    		for (int i=0;i<4;i++) {
    			cols[i] = (float) dna.getDouble(loc+3+i);
    			if (cols[i]<0.0) cols[i] = 0.0f;
    			if (cols[i]>1.0) cols[i] = 1.0f;
    		}
    		
    		Color c = null;
    		if (enableTransparency) {
    			c = new Color(cols[0],cols[1],cols[2],cols[3]); ///2.0f); 
    		}
    		else {
    			c = new Color(cols[0],cols[1],cols[2]);
    		}
    		
    		dc.setColor(c);
    		
    		//dc.fillPolygon(xPoints, yPoints, numPoints);
    		dc.fillOval(xpos-(rad/2), ypos-(rad/2), rad,rad);
    		//dc.setColor(Color.WHITE);
    		//dc.drawString(""+index, xpos,ypos);
    }

	@Override
	public Individual clone() {
		return new CPicSolver2(targetImage);
	}

	@Override
	public String toString() {
		return String.format("%.4f", score);
	}

	@Override
	public double calculateScore() {
		clampValues();
		
		Graphics2D dc = img.createGraphics();
		
		dc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        dc.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        
		dc.setColor(Color.GRAY);
		dc.fillRect(0, 0, imgWidth, imgHeight);
		double totalRadius = 0;
		double minOrder = 9999;
		double maxOrder = -9999;
		for (int i=0;i<numPolys;i++) {
			double order = dna.getDouble((i*stride)+7);
			if (order>maxOrder) maxOrder=order;
			if (order<minOrder) minOrder=order;
			totalRadius += dna.getDouble((i*stride)+2);
		}
		totalRadius /= (double)numPolys;
		
		double lastDrawnOrder = minOrder-0.1;
		
		for (int n=0;n<100;n++) {
			double order=0;
			double matchOrder=10000;
			int matchIndex=0;
			for (int i=0;i<numPolys;i++) {
				order = dna.getDouble((i*stride)+7);
				if (order>lastDrawnOrder && order<matchOrder) {
					matchOrder = order;
					matchIndex = i;
				}
			}
			drawPoly(dc, matchIndex);
			lastDrawnOrder = matchOrder;
		}
		
		/*
		for (int i=0;i<numPolys;i++) {
			drawPoly(dc, i);	
		}*/
		
	
		double total = 0;
		int count = 0;
		
		// Test random points.
		/*
		for (int i=0;i<100;i++) {
			total+=getScoreForPosition(
					(int)(Math.random()*imgWidth),
					(int)(Math.random()*imgHeight));
			count++;
		}*/
		
		int step=3;
		for (int y=0;y<imgHeight;y+=step) {
			for (int x=0;x<imgWidth;x+=step) {
				total+=getScoreForPosition(x,y);
				count++;
			}
		}
		

		
		double positionPenalty = 0;
		for (int i=0;i<numPolys;i++) {
			double x = dna.getDouble((i*stride))*imgWidth;
			double y = dna.getDouble((i*stride)+1)*imgHeight;
			double r = dna.getDouble((i*stride)+2)*(imgWidth/radiusDivider);
			if (x-r < 0) positionPenalty+=1;
			if (y-r < 0) positionPenalty+=1;
			if (x+r > imgWidth) positionPenalty+=1;
			if (y+r > imgHeight) positionPenalty+=1;
		}
		positionPenalty/=(double)numPolys;
		positionPenalty*=positionPenaltyMultiplier;
		
		
		
		double overlapPenalty = 0;
		for (int i=0;i<numPolys;i++) {
			for (int j=0;j<numPolys;j++) {
				if (i==j) continue;
				double overlap = checkOverlap(i,j);
				overlapPenalty+=overlap;
			}
		}
		overlapPenalty/=(double)numPolys;
		overlapPenalty*=overlapPenaltyMultiplier;
		
		double averaged = (total/(double)count)*500.0;
		double sizePenalty = totalRadius * 0.25;
		
				
		return averaged-sizePenalty-positionPenalty-overlapPenalty;
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
	    
	    double max=450;
	    double dist = Math.sqrt((double)((r1*r1)+(g1*g1)+(b1*b1)));
	    if (dist<0) dist=0;
	    if (dist>max) dist=max;
	    
	    dist = (max - dist) / max;
	    
		return dist;
	}
}

