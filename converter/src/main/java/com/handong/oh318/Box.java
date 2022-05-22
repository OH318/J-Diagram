package com.handong.oh318;

public class Box {
    private int x ;
    private int y ;
    private int width ;
    private int height ;
    public int lineCount ;

    public Box () { }
    public Box (int x , int y, int width, int height) {
        this.x = x ;
        this.y = y ;
        this.width = width ;
        this.height = height ;
        this.lineCount = 0 ;
    }

    public void setX (int x) { this.x = x ; }
    public void setY (int y) { this.y = y ; }
    public void setWidth (int width) { this.width = width ; }
    public void setHeight (int height) { this.height = height ; }
    public void setLineCount (int lineCount) { this.lineCount = lineCount ; }

    public int getX () { return x ; }
    public int getY () { return y ; }
    public int getWidth () { return width ; }
    public int getHeight () { return height ; }
    public int getLineCount() { return lineCount ; }
} 
