package edu.handong.csee.plt.ast;

public class App extends AST{
	private AST fun ;
	private AST arg ; 
	
	public App(AST fun, AST arg) {
		this.fun = fun;
		this.arg = arg;
	}
	
	public AST getFun() {
		return this.fun;
	}

	public AST getArg() {
		return this.arg;
	}

	public String getASTCode() {
		return "(app " + fun.getASTCode() + " " + arg.getASTCode() + ")";
	}
}
