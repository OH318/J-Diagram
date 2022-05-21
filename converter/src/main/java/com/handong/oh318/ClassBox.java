package com.handong.oh318;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class ClassBox extends Box{
    public JavaClassSource src ;
    public Box nameBoxInfo = new Box();
    public Box fieldsBoxInfo = new Box();
    public Box methodsBoxInfo = new Box();
    private int horizontalLineX ;
    private int horizontalLineY ;
    
    private int maxLength = 0 ;
    
    // relationship info.
    /**
     * get maxLength from name, fields, methods 
     * => this.setWidth() 
     * 
     * count of name, fields, method
     * => this.setHeight()
     * 
     * with, height of name, filed, method
     */
    
    public ClassBox (JavaClassSource src) {
        this.src = src ;
        this.setWidthAndHeight();
    }
    
    
    public void setWidthAndHeight() {
    	maxLength = src.getName().length() ;
        this.lineCount+=1;
        nameBoxInfo.lineCount+=1;
        
        for (FieldSource<JavaClassSource> field : src.getFields()) {
        	maxLength = Math.max(maxLength, field.getName().length());
        	this.lineCount+=1;
        	fieldsBoxInfo.lineCount += 1 ;
        }
        
        for (MethodSource<JavaClassSource> method : src.getMethods()){
        	maxLength = Math.max(maxLength, method.getName().length());
        	this.lineCount+=1;
        	methodsBoxInfo.lineCount += 1 ;
        }
        
        int nameBoxH = nameBoxInfo.lineCount * 26 ;
        int fiedlBoxH = fieldsBoxInfo.lineCount * 26 ;
        int methodBoxH = methodsBoxInfo.lineCount * 26 ;
        		
        nameBoxInfo.setHeight(nameBoxH);
        fieldsBoxInfo.setHeight(fiedlBoxH);
        methodsBoxInfo.setHeight(methodBoxH);
        this.setHeight(nameBoxH + fiedlBoxH + methodBoxH + 8);

        int width = maxLength * 10 + 40  ;
        nameBoxInfo.setWidth(width);
        fieldsBoxInfo.setWidth(width);
        methodsBoxInfo.setWidth(width);
        this.setWidth(width);
	}
  

    public void setHorisontalLine (int x, int y) {
        horizontalLineX = x ;
        horizontalLineY = y ;
    }
    
    public void setCoordinate (int idx, int maxWidth, int maxHeight) {
    	this.setX((idx%3+1) * 80 + (idx%3) * maxWidth);
    	this.setY((idx/3+1) * 80 + (idx/3) * maxHeight);
    	
    	nameBoxInfo.setX(this.getX());
    	nameBoxInfo.setY(this.getY());
    	
    	fieldsBoxInfo.setX(this.getX());
    	fieldsBoxInfo.setY(nameBoxInfo.getHeight());
    	
    	methodsBoxInfo.setX(this.getX());
    	methodsBoxInfo.setY(fieldsBoxInfo.getY() + nameBoxInfo.getHeight()+8);
    }
    
}