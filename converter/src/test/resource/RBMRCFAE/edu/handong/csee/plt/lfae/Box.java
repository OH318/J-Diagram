package edu.handong.csee.plt.lfae;

public class Box extends LFAEValue{
	private LFAEValue value = null; 
	
	public Box(LFAEValue value) { 
		this.value = value ; 
	}
	
	public boolean emptyBox() {
		if (value == null) {
			return true; 
		}else {
			return false; 
		}
	}
	
	public void setBox(LFAEValue value) {
		this.value = value ; 
	}
	
	public LFAEValue getBox() { 
		return value; 
	}
}
