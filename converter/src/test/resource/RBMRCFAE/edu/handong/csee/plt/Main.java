package edu.handong.csee.plt;

import edu.handong.csee.plt.ast.*;
import edu.handong.csee.plt.lfae.LFAEValue;

public class Main {
	
	public static void main(String[] args) {
		boolean onlyParser = false; // for -p option
		// This is just an example code. Use args to get -p option and actuall code from CLI

		String exampleCode = null ; 
		
		if (args[0] != null && args[0].equals("-p")) {
			onlyParser = true; 
			exampleCode = args[1]; 
		} else {
			exampleCode = args[0] ; 
		}
		
		// Parser
		Parser parser = new Parser();
		AST ast = parser.parse(exampleCode);
		
		if(ast == null) {
			System.out.println("Syntax Error!");
			System.exit(1);
		}
		
		
//		onlyParser = true ; 
		if(onlyParser)
			System.out.println(ast.getASTCode());
		else {
			// interpreter
			DefrdSub mtSub = new MtSub(); 
			LFAEValue result = Interpreter.interp(ast, mtSub);
			
			System.out.println(result.getASTCode());
		}
	}
}
