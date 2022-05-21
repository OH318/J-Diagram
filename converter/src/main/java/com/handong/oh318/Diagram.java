package com.handong.oh318;

public class Diagram {
    private String id ; 
    private String lastMethodId; 
    private int width ; 
    private int height ; 
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

    public void setWidth(int width) {
        this.width = width ;
    }

    public int getWidth() { 
        return this.width ; 
    }

    public void setHeight(int height) {
        this.height = height; 
    }

    public int getHeight() { 
        return this.height ; 
    }

    public Point getPoint() { 
        return this.point ; 
    }
}
