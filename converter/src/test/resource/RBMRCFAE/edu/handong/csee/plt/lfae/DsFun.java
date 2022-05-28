package edu.handong.csee.plt.lfae;

import edu.handong.csee.plt.ast.AST;

public class DsFun extends AST {
	private String param = ""; 
	private AST body = new AST();
	
	public DsFun(String param, AST body) {
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
		return "(dsfun \'" + param + " " + body.getASTCode() + ")";
	}
}
