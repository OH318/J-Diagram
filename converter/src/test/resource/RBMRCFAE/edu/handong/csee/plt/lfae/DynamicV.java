package edu.handong.csee.plt.lfae;

import edu.handong.csee.plt.ast.AST;

public class DynamicV extends LFAEValue{
	private String param ;
	private AST body ; 
	
	public DynamicV() {
		this.param = ""; 
		this.body = new AST() ;
	}
	
	public DynamicV(String param, AST body) {
		this.param = param ; 
		this.body = body;  
	}
	
	public String getParam() { 
		return this.param ;
	}
	
	public AST getBody() { 
		return this.body; 
	}
	
	public String getASTCode() {
		return "(dynamicV " + this.param + " " + this.body.getASTCode() + ")"; 
	}
}
