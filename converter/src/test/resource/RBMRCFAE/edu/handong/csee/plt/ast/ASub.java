package edu.handong.csee.plt.ast;

import edu.handong.csee.plt.lfae.LFAEValue;

public class ASub extends DefrdSub {
	private String name = ""; 
	private LFAEValue value ; 
	private DefrdSub ds = new MtSub(); 
	
	public ASub(String name, LFAEValue value, DefrdSub ds) {
		this.name = name; 
		this.value = value; 
		this.ds = ds; 
	}
	
	public String getName() { 
		return name; 
	}
	
	public LFAEValue getValue() { 
		return value; 
	}
	
	public DefrdSub getDs() { 
		return ds; 
	}
	
	public String getASTCode() { 
		return "(aSub \'" + this.name + " " + this.value.getASTCode() + " " + ds.getASTCode() + ")"; 
	}
}
