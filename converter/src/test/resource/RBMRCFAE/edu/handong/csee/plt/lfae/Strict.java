package edu.handong.csee.plt.lfae;

import edu.handong.csee.plt.Interpreter;

public class Strict {
	
	public static LFAEValue strict(LFAEValue v) {
		if (v instanceof ExprV) {
			if (v instanceof Box) {
				// if not empty 
				if (((Box) v).emptyBox()) {
					LFAEValue value = strict(Interpreter.interp(((ExprV) v).getExpr(), ((ExprV) v).getDefrdSub()));
					Box box = new Box(value); 
					return box; 
				}else {
					return ((Box)v).getBox();
				}
			}
			return strict(Interpreter.interp(((ExprV) v).getExpr(), ((ExprV) v).getDefrdSub())); 
		}
		return v;
	}
}
