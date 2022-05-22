package com.handong.oh318;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class ClassBox extends Box {
    private JavaClassSource src ;// 
    private Box nameBoxInfo ;    // 
    private Box fieldsBoxInfo ;  // 
    private Box methodsBoxInfo ; // 

    private String _extends = "";
    private String _interface = "";
    private int classId; // 
    
    private int maxLength = 0 ;
    
    ClassBox() { 
        nameBoxInfo = new Box();
        fieldsBoxInfo = new Box();
        methodsBoxInfo = new Box();
    }

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
        nameBoxInfo = new Box();
        fieldsBoxInfo = new Box();
        methodsBoxInfo = new Box();
        this.setWidthAndHeight();
    }
    
    
    public void setWidthAndHeight() {
    	maxLength = src.getName().length() ;
        // box.setLineCount(box.getLineCount() + 1);
        this.lineCount += 1 ; 

        nameBoxInfo.setLineCount(nameBoxInfo.getLineCount() + 1) ; 
        
        for (FieldSource<JavaClassSource> field : src.getFields()) {
        	maxLength = Math.max(maxLength, field.getName().length());
            // box.setLineCount(box.getLineCount() + 1); 
            this.lineCount += 1 ; 
            fieldsBoxInfo.setLineCount(fieldsBoxInfo.getLineCount() + 1);
        }
        
        for (MethodSource<JavaClassSource> method : src.getMethods()){
        	maxLength = Math.max(maxLength, method.getName().length());
        	// box.setLineCount(box.getLineCount() + 1);
            this.lineCount += 1 ;
            methodsBoxInfo.setLineCount(methodsBoxInfo.getLineCount() + 1);
        }
        
        int nameBoxH = nameBoxInfo.getLineCount() * 26 ;
        int fiedlBoxH = fieldsBoxInfo.getLineCount() * 26 ;
        int methodBoxH = methodsBoxInfo.getLineCount() * 26 ;
        		
        nameBoxInfo.setHeight(nameBoxH);
        fieldsBoxInfo.setHeight(fiedlBoxH);
        methodsBoxInfo.setHeight(methodBoxH);
        this.setHeight(nameBoxH + fiedlBoxH + methodBoxH + 8);

        int width = maxLength * 10 + 60 ;
        nameBoxInfo.setWidth(width);
        fieldsBoxInfo.setWidth(width);
        methodsBoxInfo.setWidth(width);
        this.setWidth(width);
	}
  
    
    public void setCoordinate (int idx, int maxWidth, int maxHeight) {
    	this.setX((idx%3+1) * 70 + (idx%3) * maxWidth / 2);
    	this.setY((idx/3+1) * 80 + (idx/3) * maxHeight);
    	
    	nameBoxInfo.setX(this.getX());
    	nameBoxInfo.setY(this.getY());
    	
    	fieldsBoxInfo.setX(this.getX());
    	fieldsBoxInfo.setY(nameBoxInfo.getHeight());
    	
    	methodsBoxInfo.setX(this.getX());
    	methodsBoxInfo.setY(fieldsBoxInfo.getY() + nameBoxInfo.getHeight()+8);
    }

    public void setExtends (String ext) {
    	this._extends = ext;
    }
    
    public void setInterface (String interf) {
    	this._interface = this._interface.concat(interf);
    	// TODO Several interfaces
    }
    
    public void setID (int id) {
    	this.classId = id;
    }
    
    public String getExtends() {
    	return this._extends;
    }
    
    public String getInterface() {
    	return this._interface;
    }

    public void setJavaClassSource(JavaClassSource src) { 
        this.src = src ; 
    }

    public JavaClassSource getJavaClassSource()  {
        return this.src ; 
    }

    public void setClassId(int classId) { 
        this.classId = classId ; 
    } 

    public int getClassId() { 
        return this.classId;  
    }
    
    public void setFieldsBoxInfo(Box fieldsBoxInfo ) { 
        this.fieldsBoxInfo = fieldsBoxInfo ; 
    }

    public Box getFieldboxInfo() { 
        return this.fieldsBoxInfo ; 
    }

    public void setMethodsBoxInfo(Box methodsBoxInfo) { 
        this.methodsBoxInfo = methodsBoxInfo ; 
    }

    public Box getMethodsBoxInfo() { 
        return this.methodsBoxInfo ; 
    }
}
