package com.handong.oh318;

public class Diagram {
    private int width ; 
    private int height ; 
    private Point point ;

    Diagram() {
        point = new Point() ; 
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
