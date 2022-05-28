package edu.handong.csee.plt.ast;

public class Sub extends AST {
	private AST lhs = new AST();
	private AST rhs = new AST();
	
	public Sub(AST lhs, AST rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public AST getLhs() {
		return lhs;
	}

	public AST getRhs() {
		return rhs;
	}

	public String getASTCode() {
		return "(sub " + lhs.getASTCode() + " " + rhs.getASTCode() + ")";
	}
}
