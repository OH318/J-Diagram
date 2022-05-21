package com.handong.oh318;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Lines {

    private ArrayList<Edges> arrows ; 

    public void addArrow(int arrow_type, Point source, Point target) {
    	Edges edge = new Edges();
    	
    	edge.setArrowType(arrow_type);
    	edge.setSource(source);
    	edge.setTarget(target);
    	
    	arrows.add(edge);
    }

    public ArrayList<Edges> getArrows() {

        return this.arrows ; 
    }
}