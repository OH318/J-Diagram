package edu.handong.csee.plt.ast;

public class Fun extends AST {
	private String param = ""; 
	private AST body = new AST();
	
	public Fun(String param, AST body) {
		this.param = param;
		this.body = body;
	}
	
	public String getParam() {
		return this.param;
	}

	public AST getBody() {
		return this.body;
	}

	public String getASTCode() {
		return "(fun \'" + param + " " + body.getASTCode() + ")";
	}
}
