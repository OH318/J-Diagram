package edu.handong.csee.plt.lfae;

import edu.handong.csee.plt.ast.AST;
import edu.handong.csee.plt.ast.DefrdSub;

public class ExprV extends LFAEValue{

	private AST expr ; 
	private DefrdSub ds ; 
	private Box box; 
	
	public ExprV (AST expr, DefrdSub ds, Box box) { 
		this.expr = expr; 
		this.ds = ds; 
		this.box = box ; 
	}
	
	public AST getExpr() {
		return this.expr; 
	}
	
	public DefrdSub getDefrdSub() { 
		return this.ds ; 
	}
	
	public Box getBox() {
		return this.box ; 
	}
	
	public String getASTCode() { 
		return "(exprV " + this.expr.getASTCode() + " " + this.ds.getASTCode() + ")"; 
	}
	
}
