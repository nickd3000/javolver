package javolver;

// Import the basic graphics classes.
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class BasicDisplay extends JFrame {

	BufferedImage img;
	Color currentColor;
	int width, height;

	public BasicDisplay(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		setSize(width, height);
		setVisible(true);

		img = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB); 
			
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	public void close()
	{
		this.dispose();
	}

	public void paint(Graphics g){
		
		//g.drawLine(10,10,150,150); // Draw a line from (10,10) to (150,150)
		 g.drawImage(img,0,0,null);
	  }

	public void refresh()
	{
		repaint();
		//invalidate();
	}
	
	
	public void drawLine(int x1, int y1, int x2, int y2, Color c)
	{
		Graphics g = img.getGraphics();
		g.setColor(c);
        g.drawLine(x1,y1,x2,y2);
        g.dispose();
	}
	public void drawLine(float x1, float y1, float x2, float y2, Color c)
	{
		Graphics g = img.getGraphics();
		g.setColor(c);
        g.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
        g.dispose();
	}
	public void drawLine(double x1, double y1, double x2, double y2, Color c, double thickness)
	{
		Graphics g = img.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		
		//g.setColor(c);
		g2d.setColor(c);
		g2d.setStroke(new BasicStroke((float)thickness));
		g2d.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
		g2d.dispose();
		
        //g.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
        g.dispose();
	}
	public void drawCircle(double x, double y, double r, Color c)
	{
		Graphics g = img.getGraphics();
		g.setColor(c);
        //g.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
		g.fillOval((int)(x-(r/2)), (int)(y-(r/2)), (int)(r), (int)(r));
        g.dispose();
	}
	public void cls(Color c)
	{
		Graphics g = img.getGraphics();
		g.setColor(c);
        //g.drawRect(0,0,400,400);
		g.fillRect(0, 0, width, height);
        //g.clearRect(0, 0, 400,400);
		g.dispose();
	}
	
	public Image getImage()
	{
		return img;
	}
	
	/*
	 public static void main(String arg[]){
		    // create an identifier named 'window' and
		    // apply it to a new BasicFrame object
		    // created using our constructor, above.
		    BasicFrame frame = new BasicFrame();

		    // Use the setSize method that our BasicFrame
		    // object inherited to make the frame
		    // 200 pixels wide and high.
		    frame.setSize(200,200);

		    // Make the window show on the screen.
		    frame.setVisible(true);
		  }
		  */
}
