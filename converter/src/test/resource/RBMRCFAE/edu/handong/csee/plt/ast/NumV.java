package edu.handong.csee.plt.ast;

import edu.handong.csee.plt.lfae.LFAEValue;

public class NumV extends LFAEValue{
	private String num = ""; 
	
	public NumV(String num) {
		this.num = num; 
	}
	
	public String getStrNum() { 
		return num;
	}
	public String getASTCode() { 
		return "(numV " + num +")"; 
	}
}
