import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;


public class GeneTextPic implements IGene {

	int dnaSize = (20)*2; // Must be a multiple of 2.
	int [] dna = new int[dnaSize];
	
	Font font = null; 
    
    
	@Override
	public void init() {

		//GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//String []fontFamilies = ge.getAvailableFontFamilyNames();
		
		
		randomise();
		//font = new Font("Lucida Sans Regular", Font.TRUETYPE_FONT, 16);
		font = new Font("Arial Unicode MS", Font.TRUETYPE_FONT, 32);
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void calculateScore() {
		// TODO Auto-generated method stub

	}

	@Override
	public float getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IGene createChild(IGene p1, IGene p2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String asString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canMate(IGene partner, boolean excludeSimilar) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getGeneration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void randomise() {
		for (int i=0;i<dnaSize;i++)
		{
			dna[i] = (int)(Math.random()*(double)0xff);
		}
	}
	
	public String getDnaAsString()
	{
		String example = "\ubeef";
		String str = "";
		String start = ""+"\\"+'u';
		//Character.toString(c)
		for (int i=0;i<dnaSize;i+=2)
		{
			int val = (dna[i]<<8+dna[i+1]);
			val = (int)(Math.random()*((double)0x10FFFF));
			//val =dna[i+1];
			str=str+(char)(val);
		}
		
		return str;
	}
	
	public String getUstringFromInts(int i1, int i2)
	{
		if (i1>255) i1=255;
		if (i2>255) i2=255;
		String st1 = Integer.toHexString(i1);
		if (st1.length()<2) st1="0"+st1;
		String st2 = Integer.toHexString(i2);
		if (st2.length()<2) st2="0"+st2;
		
		return st1+st2;
	}
	
	public void draw(BasicDisplay disp, float offsx, float offsy)
	{
		String text = getDnaAsString();
		
		Image img = disp.getImage();
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		if (g2==null) return;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Lucida Sans Regular", Font.PLAIN, 32);
		g2.setFont(font);
		
        g2.setColor(new Color(0,0,0));
		g2.drawString(text, 40+offsx, 80);
		
		//g2.drawString("DNA:"+(char)dna[1], 40, 120+offsy);
		g2.drawString(".", 40+(offsy/10000), 120);
		
		g2.dispose();
	}
	
	

}
