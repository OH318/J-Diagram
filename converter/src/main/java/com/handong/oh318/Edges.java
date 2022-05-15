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

    /**
     * class1(source) -> class2(target)
     * sourceClassDiagram: 
     * targetClassDiagram: 
     */
    private int sourceClassDiagramId ; 
    private int targetClassDiagramId ;

    public int identifyArrow(String style) {		//if there is no arrow type,it will return -1; 
    	int type = -1;
    	
    	if(style.contains("endArrow=none")) 
		{
			type = 3; // associations
		}
		else if(style.contains("endArrow=block")) 
		{
			if(style.contains("dashed=1")){
				type = 1; //realizations
			}else {
				type = 0; //generalizations
			}
		}
		else if(style.contains("endArrow=open")) 
		{
			if(style.contains("startFill=0")) {
				type = 6;//aggregationRelations
			}else if(style.contains("startFill=1")) {
				type = 8;//compositionRelations
			}else if(style.contains("dashed=1")) {
				type = 2;//dependancies
			}else {
				type = 4;//directedAssociations
			}
		}
		else if(style.contains("endArrow=diamondThin")) 
		{
			if(style.contains("endFill=0")) 
			{
				type = 5;//aggregations
			}
			else if(style.contains("endFill=1")) 
			{
				type = 7 ;//compositions
			}
		}
    	
    	return arrowType;
    }

    public void setSourceClassDiagramId(int sourceClassDiagramId) { 
        this.sourceClassDiagramId = sourceClassDiagramId ; 
    }

    public int getSourceClassDiagramId() { 
        return this.sourceClassDiagramId; 
    }

    public void setTargetClassDiagramId(int targetClassDiagramId)  {
        this.targetClassDiagramId = targetClassDiagramId ; 
    }

    public int getTargetClassDiagramId() {
        return this.targetClassDiagramId ; 
    }

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
