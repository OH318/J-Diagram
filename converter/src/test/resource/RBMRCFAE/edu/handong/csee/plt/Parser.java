package edu.handong.csee.plt;

import java.util.ArrayList;

import edu.handong.csee.plt.ast.AST;
import edu.handong.csee.plt.ast.Add;
import edu.handong.csee.plt.ast.App;
import edu.handong.csee.plt.ast.Fun;
import edu.handong.csee.plt.ast.Id;
import edu.handong.csee.plt.ast.Num;
import edu.handong.csee.plt.ast.Sub;
import edu.handong.csee.plt.lfae.DsFun;

public class Parser {

	public AST parse(String exampleCode) {
		ArrayList<String> subExpressions = splitExpressionAsSubExpressions(exampleCode);
		
		//(? number?) 
		if(subExpressions.size() == 1 && isNumeric(subExpressions.get(0))) {
			
			return new Num(subExpressions.get(0));
		}
		// (list '+ l r) 
		if(subExpressions.get(0).equals("+")) {
			
			return new Add(parse(subExpressions.get(1)),parse(subExpressions.get(2)));
		}
		
		// (list '- l r) 
		if(subExpressions.get(0).equals("-")) {
			
			return new Sub(parse(subExpressions.get(1)), parse(subExpressions.get(2))); 
		}

		// (? symbol?) 
		if(subExpressions.size() == 1) {
			return new Id(subExpressions.get(0)); 
		}
		
		if(subExpressions.size() == 2) { 
			
			return new App(parse(subExpressions.get(0)), parse(subExpressions.get(1))); 
		}
		
		// (list 'with (list i v) e)  
		if(subExpressions.get(0).equals("with")) {
			ArrayList<String> sub = splitExpressionAsSubExpressions(subExpressions.get(1)); 
			
			return new App(new Fun(sub.get(0), parse(subExpressions.get(2))), parse(sub.get(1))); 
		}
		
		// (list 'fun (list p) b) -> (fun p (parse b))
		if(subExpressions.get(0).equals("fun")) {
			ArrayList<String> sub = splitExpressionAsSubExpressions(subExpressions.get(1));
			
			return new Fun(sub.get(0), parse(subExpressions.get(2))); 
		}
		
		if(subExpressions.get(0).equals("dsfun")) {
			ArrayList<String> sub = splitExpressionAsSubExpressions(subExpressions.get(1));
			
			return new DsFun(sub.get(0), parse(subExpressions.get(2))); 
		}
		
		return null; // syntax error!
	}

	public ArrayList<String> splitExpressionAsSubExpressions(String exampleCode) {

		// deal with brackets first.
		if((exampleCode.startsWith("{") && !exampleCode.endsWith("}"))
				|| (!exampleCode.startsWith("{") && exampleCode.endsWith("}"))) {
			System.out.println("Syntax error");
			System.exit(0);
		}

		if(exampleCode.startsWith("{"))
			exampleCode = exampleCode.substring(1, exampleCode.length()-1);


		return getSubExpressions(exampleCode);
	}



	/**
	 * This method return a list of sub-expression from the given expression.
	 * For example, {+ 3 {+ 3 4}  -> +, 2, {+ 3 4}
	 * TODO JC was sleepy while implementing this method...it has complex logic and might be buggy...
	 * You can do better or find an external library.
	 * @param exampleCode
	 * @return list of sub expressions 
	 */
	public ArrayList<String> getSubExpressions(String exampleCode) {

		ArrayList<String> sexpressions = new ArrayList<String>();
		int openingParenthesisCount = 0;
		String strBuffer = "";
		
		// {{fun {f2} {+ f2 y}} 2}
		for(int i=0; i < exampleCode.length() ;i++) {
			if(i == 0 && (i == 0 && exampleCode.charAt(i) == '{')) {
				openingParenthesisCount++;
				strBuffer = strBuffer + exampleCode.charAt(i);
				continue;
			}else if(i == 0 && !(i == 0 && exampleCode.charAt(i) == '{')) {
				strBuffer = strBuffer + exampleCode.charAt(i);
				continue;
			}else if(exampleCode.charAt(i)==' ' && openingParenthesisCount==0){
				// buffer is ready to be a subexpression
				if(!strBuffer.isEmpty()) {
					sexpressions.add(strBuffer);
					strBuffer = ""; // Ready to start a new buffer
				} 
				continue;
			} else {
				if(exampleCode.charAt(i)=='{' && openingParenthesisCount==0){
					openingParenthesisCount++;
					// Ready to start a new buffer
					strBuffer = "" + exampleCode.charAt(i);
					continue;
				} else if(exampleCode.charAt(i)=='{') {
					openingParenthesisCount++;
					strBuffer = strBuffer + exampleCode.charAt(i);
					continue;
				} else if(exampleCode.charAt(i)=='}' && openingParenthesisCount>0) {
					openingParenthesisCount--;
					strBuffer = strBuffer + exampleCode.charAt(i);
					continue;
				} else if(exampleCode.charAt(i)=='}') {
					// buffer is ready to be a subexpression
					sexpressions.add(strBuffer);
					continue;
				}
			}
			strBuffer = strBuffer + exampleCode.charAt(i);
		}
		
		sexpressions.add(strBuffer);

		return sexpressions;
	}

	public static boolean isNumeric(String str)
	{
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

}
