package com.handong.oh318;

public class Edges {
    private int arrowType ; 
    /*
        - ArrowType - 
        generalization, endArrow=block;endSize=16;endFill=0;html=1;
        realizations, endArrow=block;dashed=1;endFill=0;endSize=12;html=1;
        dependencies, endArrow=open;endSize=12;dashed=1;html=1;

        associations, endArrow=none;html=1;edgeStyle=orthogonalEdgeStyle;
        directedAssociations, endArrow=open;endFill=1;endSize=12;html=1;

        aggregations, endArrow=diamondThin;endFill=0;
        aggregationRelations, endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=0;
                                edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;
        
        compositions, endArrow=diamondThin;endFill=1;
        compositionRelations, endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;
                                edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;
    */

    private Point source ; 
    private Point target ;

    public void setArrowType(int arrowType) {
        this.arrowType = arrowType ; 
    }

    public int getArrowType() {
        return this.arrowType ; 
    }
    
    public void setSource(Point source) {
        this.source = source; 
    }

    public void setTarget(Point target)  {
        this.target = target ; 
    }

    public Point getSource() {
        return this.source ;
    }

    public Point getTaregt() { 
        return this.target ;
    }
}
