package edu.handong.csee.plt.lfae;

import edu.handong.csee.plt.ast.AST;
import edu.handong.csee.plt.ast.ClosureV;
import edu.handong.csee.plt.ast.NumV;

public class LFAEValue extends AST{
	private NumV num ;
	private ClosureV closureV ; 
	private DynamicV dynamicV ; 
	private ExprV exprV ; 
	
	public LFAEValue() { }
	
	public LFAEValue(NumV num, ClosureV closureV, ExprV exprV){
		this.num = num;
		this.closureV = closureV; 
		this.exprV = exprV; 
	}
	
	public LFAEValue(NumV num, DynamicV dynamicV, ExprV exprV) {
		this.num = num;
		this.dynamicV = dynamicV; 
		this.exprV = exprV; 
	}
	
	public void setNumV(NumV num) {
		this.num = num ; 
	}
	
	public NumV getNumV() {
		return num ; 
	}
	
	public void setClosureV(ClosureV closureV) {
		this.closureV = closureV; 
	}
	
	public ClosureV getClosureV() { 
		return this.closureV; 
	}
	
	public void setDynamicV(DynamicV dynamicV) {
		this.dynamicV = dynamicV; 
	}
	
	public DynamicV getDynamicV() { 
		return this.dynamicV; 
	}
	
	public void setExprV(ExprV exprV) { 
		this.exprV = exprV; 
	}
	
	public ExprV getExprV() {
		return this.exprV; 
	}
	
	public String getStrNum() {
		return num.getASTCode();
	}
	
}
