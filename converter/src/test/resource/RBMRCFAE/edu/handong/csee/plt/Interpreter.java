package edu.handong.csee.plt;

import java.util.ArrayList;

import edu.handong.csee.plt.ast.AST;
import edu.handong.csee.plt.ast.ASub;
import edu.handong.csee.plt.ast.Add;
import edu.handong.csee.plt.ast.App;
import edu.handong.csee.plt.ast.ClosureV;
import edu.handong.csee.plt.ast.DefrdSub;
import edu.handong.csee.plt.ast.Fun;
import edu.handong.csee.plt.ast.Id;
import edu.handong.csee.plt.ast.MtSub;
import edu.handong.csee.plt.ast.Num;
import edu.handong.csee.plt.ast.NumOperator;
import edu.handong.csee.plt.ast.NumV;
import edu.handong.csee.plt.ast.Sub;
import edu.handong.csee.plt.lfae.DsFun;
import edu.handong.csee.plt.lfae.DynamicV;
import edu.handong.csee.plt.lfae.ExprV;
import edu.handong.csee.plt.lfae.LFAEValue;
import edu.handong.csee.plt.lfae.Strict;

public class Interpreter {

	public static LFAEValue lookup(String id, DefrdSub ds) {
		if (ds instanceof MtSub) { 
			System.out.println("lookup \"free identifier\""); 
			System.exit(1); 
		}
		
		if (ds instanceof ASub) {
			if ( id.equals(((ASub) ds).getName()) ) {
				return ((ASub) ds).getValue(); 
			}else {
				if ( ((ASub) ds).getDs() != null ) {
					return lookup(id, ((ASub) ds).getDs());
				}	
			}
		}
		
		return ((ASub)ds).getDs(); 
	}
	
	public static LFAEValue interp(AST ast, DefrdSub ds) {
		
		if(ast instanceof Num) {
			Num num = (Num)ast ; 
			NumV numV = new NumV(num.getStrNum());
			return numV; 
		}
		
		if(ast instanceof Id) {
			Id id = (Id)ast; 
			return lookup(id.getStrId(), ds);  
		}
		
		if(ast instanceof Add) {
			Add add = (Add)ast;
			return NumOperator.numPlus(interp(add.getLhs(), ds), interp(add.getRhs(), ds));
		}
		
		if(ast instanceof Sub) {
			Sub sub = (Sub)ast ; 
			return NumOperator.numMinus(interp(sub.getLhs(), ds), interp(sub.getRhs(), ds));
		}
		
		if(ast instanceof Fun) { 
			Fun fun = (Fun)ast; 
			ClosureV closureV = new ClosureV(fun.getParam(), fun.getBody(), ds); 
			
			return closureV; 
		}
		
		if(ast instanceof DsFun) {
			DsFun dsFun = (DsFun)ast; 
			DynamicV dynamicV = new DynamicV(dsFun.getParam(), dsFun.getBody()); 
			
			return dynamicV; 
		}
		
		if(ast instanceof App) {
			App app = (App)ast ; 
			
			LFAEValue arg_value = new ExprV(app.getArg(),ds, null); 
			
			if ( Strict.strict(interp(app.getFun(), ds)) instanceof DynamicV) {
				DynamicV ftn_value = (DynamicV) Strict.strict(interp(app.getFun(), ds));  
				return interp(ftn_value.getBody(), new ASub(ftn_value.getParam(), arg_value, ds)); 
			} 
			
			ClosureV ftn_value = (ClosureV) Strict.strict(interp(app.getFun(), ds));  
				
			return interp(
				ftn_value.getBody(), 
				new ASub(ftn_value.getParam(), 
				arg_value, 
				ftn_value.getDefrdSub())); 		
		}
		
		return null;
	}
}
