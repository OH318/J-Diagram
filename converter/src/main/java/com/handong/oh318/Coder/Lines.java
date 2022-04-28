package com.handong.oh318.Coder;

import java.util.ArrayList;

public class Lines {
    
    private ArrayList<Edges> generalizations ; //generalization, endArrow=block;endSize=16;endFill=0;html=1;
    private ArrayList<Edges> implementaions ; //realizations, endArrow=block;dashed=1;endFill=0;endSize=12;html=1;
    private ArrayList<Edges> dependencies ; // endArrow=open;endSize=12;dashed=1;html=1;

    private ArrayList<Edges> associations ; // endArrow=none;html=1;edgeStyle=orthogonalEdgeStyle;
    private ArrayList<Edges> directedAssociations ; // endArrow=open;endFill=1;endSize=12;html=1;

    private ArrayList<Edges> aggregations ; // endArrow=diamondThin;endFill=0;
    private ArrayList<Edges> aggregationRelations ; // endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=0;
                                                    // edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;
    private ArrayList<Edges> compositions ; // endArrow=diamondThin;endFill=1;
    private ArrayList<Edges> compositionRelations ; // endArrow=open;html=1;endSize=12;startArrow=diamondThin;startSize=14;startFill=1;
                                                    // edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;

}
