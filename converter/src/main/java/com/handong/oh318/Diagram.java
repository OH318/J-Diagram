package com.handong.oh318;

import java.util.HashMap;

public class Diagram {
    private String id ;
    private HashMap<String, String> attributesId ;
    private float width ; 
    private float height ; 
    private Point point ;

    Diagram() {
        point = new Point() ; 
        attributesId = new HashMap<>() ;
    }

    public void setId(String id)  {
        this.id = id ; 
    }

    public String getId() { 
        return this.id ; 
    }

    public void setAttributesId(HashMap<String, String> attributesId)  {
        this.attributesId = attributesId ;
    }

    public HashMap<String, String> getAttributesId() { 
        return this.attributesId ; 
    }

    public void setWidth(float width) {
        this.width = width ;
    }

    public float getWidth() { 
        return this.width ; 
    }

    public void setHeight(float height) {
        this.height = height; 
    }

    public float getHeight() { 
        return this.height ; 
    }

    public Point getPoint() { 
        return this.point ; 
    }
}
