package edu.handong.csee.plt.ast;

public class Num extends AST {
	private String num = "0";
	
	public Num(String num){
		this.num = num;
	}
	
	public String getStrNum() {
		return num;
	}
	
	public String getASTCode() {
		return "(num " + num +")";
	}
}
