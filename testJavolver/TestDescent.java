package testJavolver;

import java.awt.Color;
import java.util.ArrayList;

import com.physmo.toolbox.BasicDisplay;
import com.physmo.toolbox.BasicDisplayAwt;

public class TestDescent {

	public static void main(String[] args) {
	
		BasicDisplay disp = new BasicDisplayAwt(400, 400);
		
		GeneTree tree = new GeneTree();
		tree.init();
		double delta = 0.001;
		int dnaLength = tree.dnaLength;
		
		ArrayList<Double> changeList = new ArrayList<>(dnaLength);
		for (int i=0;i<dnaLength;i++) changeList.add(0.0); 
		
		for (int iter = 0; iter< 10000; iter++) {

			// Do gradient descent.
			if (iter%1==0) {
				for (int i=0;i<dnaLength;i++) {
					double scoreOrig = tree.calculateScore();
					double dnaValue = tree.dna.getDouble(i);
					
					changeList.set(i, 0.0);
					
					tree.dna.set(i, dnaValue+delta);
					changeList.set(i,0.0);
					
					double diff = tree.calculateScore()-scoreOrig;
					if (diff>0) changeList.set(i, Math.abs(diff));
					else {
						tree.dna.set(i, dnaValue-delta);
						diff = tree.calculateScore()-scoreOrig;
						if (diff>0) changeList.set(i, -Math.abs(diff));
					}
					
					tree.dna.set(i, dnaValue); // Set back to normal.
				}
			}
			
			// Apply changes
			for (int i=0;i<dnaLength;i++) {
				//if (Math.random()>0.3) continue;
				
				double dnaValue = tree.dna.getDouble(i);
				double chVal = changeList.get(i);
				
				if (chVal>0.25) chVal=0.25;
				if (chVal<-0.25) chVal=-0.25;
				chVal *= Math.random() * 0.1;
				chVal += (Math.random()-0.5)*0.01;
				
				tree.dna.set(i, dnaValue+chVal);
			}
			
			tree.calculateScore();
			disp.cls(new Color(183, 213, 149 ));
			tree.draw(disp, 200.0f, 350.0f);
			disp.refresh();
		}
		
		
		
		
	}
	
}
