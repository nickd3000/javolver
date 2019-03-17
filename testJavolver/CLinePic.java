package testJavolver;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.physmo.javolver.Individual;

/**
 * Created by nick on 20/07/2016.
 */
public class CLinePic extends Individual {

	static BufferedImage img = null;
	BufferedImage targetImage;
	int imgWidth=200;
	int imgHeight=200;
	int numPolys = 200;//200*1;
	int numPoints = 1;					// Number of points per polygon.
	int stride = 4; 	// Number of data elements per poly.
	boolean enableTransparency = true;
	double radiusDivider = 6.0;
	
	double overlapPenaltyMultiplier = 0.2; // 0.02
	double positionPenaltyMultiplier = 1.5; // 0.01
	
	Color col1 = new Color(0xaf,0x80,0x40,0x50);
	Color col2 = new Color(0x00,0x00,0x00,0x50);
	Color colBlank = new Color(0x00,0x00,0x00,0x00);
	
	public CLinePic(BufferedImage targetImage) {
		dna.init(numPolys*stride);
		
		this.targetImage = targetImage;
		imgWidth = targetImage.getWidth();
		imgHeight = targetImage.getHeight();
		
		if (img==null)
			img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);

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

	private double quantize(double v) {
		int q=50*5;
		v = (int)(v*q);
		return v/(double)q;
	}

	// New version to draw lines.
    private void drawPoly(Graphics2D dc, int index) {
    		int loc = index*stride;

    		int x1,y1;
    		int x2,y2;
    		double p1,p2,p3;
    		
    		p1=quantize(dna.getDouble(loc));
    		p2=quantize(dna.getDouble(loc+1));
    		p3=quantize(dna.getDouble(loc+2));
    		
    		if (p1<0.5) {
    			// top 
    			x1=(int) (p1*2*imgWidth);
    			y1=0;
    		} else {
    			// right 
    			y1=(int) (((p1-0.5)*2)*imgHeight);
    			x1=imgWidth;
    		}
    		
    		
    		if (p2<0.5) {
    			// bottom 
    			x2=(int) (p2*2*imgWidth);
    			y2=imgHeight;
    		} else {
    			// left
    			y2=(int) (((p2-0.5)*2)*imgHeight);
    			x2=0;
    		}
    		
    		if (p3<0.5) {
    			dc.setColor(col1);
    		} else  {
    			dc.setColor(col2);
    		} 
    			

    		dc.drawLine(x1, y1, x2, y2);
    }


	@Override
	public Individual clone() {
		return new CLinePic(targetImage);
	}

	@Override
	public String toString() {
		return String.format("%.4f", score);
	}

	@Override
	public double calculateScore() {
		clampValues();
		
		Graphics2D dc = img.createGraphics();
		dc.setStroke(new BasicStroke(5));
		
		dc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        dc.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        dc.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        
		dc.setColor(Color.WHITE);
		dc.fillRect(0, 0, imgWidth, imgHeight);

		
		for (int i=0;i<numPolys;i++) {
			drawPoly(dc, i);
		}

		double total = 0;
		int count = 0;

		total = testGridOfPoints(2);
		total = testRandomPoints(50);
		
		
		double averaged = (total)*500.0;
	
		return averaged; //-sizePenalty;//-positionPenalty;//-overlapPenalty;
	}
	
	public double testGridOfPoints(int step) {
		double total = 0;
		int count = 0;
		int ystart = (int)(Math.random()*step);
		int xstart = (int)(Math.random()*step);
		for (int y=ystart;y<imgHeight;y+=step) {
			for (int x=xstart;x<imgWidth;x+=step) {
				total+=Math.abs(getScoreForPosition(x,y));
				count++;
			}
		}
		return (total/(double)count);
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

