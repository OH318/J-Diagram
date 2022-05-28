package edu.handong.csee.plt.ast;

import edu.handong.csee.plt.lfae.LFAEValue;
import edu.handong.csee.plt.lfae.Strict;

public class NumOperator {
	
	public static LFAEValue numPlus(LFAEValue n1, LFAEValue n2) {
		if (n1 == null) {
			System.out.println("TEST1"); 
		}
		
		if (n2 == null) {
			System.out.println("TEST2"); 
		}

		NumV v = new NumV("" + (Integer.parseInt(Strict.strict(n1).getStrNum()) 
									+ Integer.parseInt(Strict.strict(n2).getStrNum()))); 

		
		return v; 
	}
	
	public static LFAEValue numMinus(LFAEValue n1, LFAEValue n2) { 

		NumV v = new NumV("" + (Integer.parseInt(Strict.strict(n1).getStrNum()) 
								- Integer.parseInt(Strict.strict(n2).getStrNum()))); 
		
		return v; 
	}
}
