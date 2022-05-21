package com.handong.oh318;

public class Diagram {
    private String id ;
    private float width ; 
    private float height ; 
    private Point point ;

    Diagram() {
        point = new Point() ; 
    }

    public void setId(String id)  {
        this.id = id ; 
    }

    public String getId() { 
        return this.id ; 
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
