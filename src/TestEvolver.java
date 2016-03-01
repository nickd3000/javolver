import java.awt.Color;


public class TestEvolver {

	public static void main(String[] args) {

		//testTree();
		//testWord();
		testTextPic();
	}

	public static void testTextPic()
	{
		BasicDisplay disp = new BasicDisplay(800,400);
		GeneTextPic testGene = new GeneTextPic();
		
		int count = 0;
		for (int i=0;i<10000000;i++)
		{
			testGene.init();
			disp.cls(new Color(149,183,213));
			testGene.draw(disp, 0, i/10);
			disp.refresh();
			count++;
			if (count>300)
			{
				disp.refresh();
				count=0;
			}
		}
		disp.close();
		
	}
	
	public static void testTree()
	{
		BasicDisplay disp = new BasicDisplay(800,400);
		//disp.drawLine(20,200,200,20);
		

		
		System.out.print("START");
		for (int n=0;n<500;n++)
		{
			// Create the evolver:
			Evolver testEvolver = new Evolver();

			GeneTree adam = null;
			for (int i=0;i<1000;i++)
			{
				 adam = new GeneTree();
				adam.init();
				testEvolver.addGene(adam);
			}
				
			for (int j=0;j<500000000;j++)
			{
				for (int i=0;i<1;i++)
				{
					testEvolver.doOneCycle();
				}
				//testEvolver.cullHalf();
				if (testEvolver.GenePool.size()<1) continue;
				testEvolver.report();
				GeneTree best = (GeneTree) testEvolver.findBestScoringGene();
				GeneTree secondBest = null;
				float runningScore=0.0f;
				for (IGene gene : testEvolver.GenePool)
				{
					if (gene.canMate(best,false)==false && gene.getScore()>runningScore)
					{
						secondBest=(GeneTree) gene;
						runningScore=gene.getScore();
						break;
					}
				}
				
				disp.cls(new Color(149,183,213));
	
				if (secondBest!=null) 
					secondBest.draw(disp, 200.0f+300.0f, 350.0f);
				
				best.draw(disp, 200.0f, 350.0f);
				
				disp.drawLine(j, 50, j, 70,Color.GREEN);
				disp.refresh();
				// 
				
				System.out.print("---\n");
			}
			System.out.print("END");
			}
		
	}
	
	
	public static void testWord()
	{
		
		//BasicDisplay disp = new BasicDisplay(800,400);
		//disp.drawLine(20,200,200,20);
		
		// Create the evolver:
		Evolver testEvolver = new Evolver();

		GeneWord adam = new GeneWord();
		adam.word = "JSCH IE SPWW";
		testEvolver.addGene(adam);
		
		for (int j=0;j<10000;j++)
		{
			for (int i=0;i<10;i++)
			{
				testEvolver.doOneCycle();
			}
			//testEvolver.cullHalf();
			testEvolver.report();
		
		}
		
		System.out.print("END "); 
	}
}
