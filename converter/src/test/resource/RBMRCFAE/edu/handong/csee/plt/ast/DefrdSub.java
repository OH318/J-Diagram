package edu.handong.csee.plt.ast;

import java.util.ArrayList;

public class DefrdSub extends ClosureV{

	public String getASTCode() {
		if (this instanceof MtSub) {
			return ((MtSub)this).getASTCode() ; 
		}
		
		if (this instanceof ASub) { 
			return ((ASub)this).getASTCode(); 
		}
		
		return param;
	}
}
