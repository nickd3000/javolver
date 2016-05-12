package javolver;

import java.util.ArrayList;

public class Utils {

	public static void test()
	{
		ArrayList<Object> iList = new ArrayList<Object>();
		
		System.out.println("Testing insert");
		
		for (int i=0;i<20;i++) iList.add(i);
		
		System.out.println(iList.toString());

		for (int i=0;i<10;i++) iList = mutateInsert(iList);
		
		System.out.println(iList.toString());
		
		System.out.println("Testing mutate delete");
		iList = new ArrayList<Object>();
		for (int i=0;i<20;i++) iList.add(i);
		System.out.println(iList.toString());
		for (int i=0;i<10;i++) iList = mutateDelete(iList);

		System.out.println(iList.toString());
	}
	 
	public static ArrayList<Object> mutateInsert(ArrayList<Object> d)
	{
		int skip = (int)(d.size()*Math.random());
		ArrayList<Object> nd = new ArrayList<>(d);
		for (int i=0;i<d.size()-1;i++)
		{
			int pos = i;
			if (i>skip) pos=i+1;
			nd.set(pos, d.get(i));
			
		}
			
		return nd;
	}
	
	public static ArrayList<Object> mutateDelete(ArrayList<Object> d)
	{
		int skip = (int)(d.size()*Math.random());
		ArrayList<Object> nd = new ArrayList<>(d);
		for (int i=0;i<d.size()-1;i++)
		{
			int pos = i;
			if (i>skip) pos=i+1;
			nd.set(i, d.get(pos));
			
		}
			
		return nd;
	}
}
