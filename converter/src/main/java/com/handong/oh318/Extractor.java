package com.handong.oh318;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.IllegalFormatWidthException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

// xml, DOM
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.jboss.forge.roaster.model.Visibility;
// JavaClassSource
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaUnit;

public class Extractor {
    private String destinationPath ; // Destination path that .drawio file will be created
    private String directoryPath ; // Java classes directory path by user input
    private ArrayList<ClassBox> javaClassBox ; // Rapper Class for JavaClassSource
    private Document document ;
    private Element root;
    private int maxHeight;
    private int maxWidth;
    private int id; 
    
    // Constants for drawio XML
    private final String inclassStyleConstant = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;";
    private final String classNameStyleConstant = "swimlane;fontStyle=0;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=0;marginBottom=0;html=1;";
    private final String speratorLineStyleConstant = "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;";
    private final String interfaceLineStyleConstant = "endArrow=block;dashed=1;endFill=0;endSize=12;html=1;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" ;
    private final String extendLineStyleConstant = "endArrow=block;endSize=16;endFill=0;html=1" ;
    
    public Extractor(String directoryPath, String drawioFilepath){
        this.destinationPath = drawioFilepath;
        this.directoryPath = directoryPath;
        this.javaClassBox = getJavaClassSources(".java");
        this.id = 0;
    }
    
    public ArrayList<ClassBox> getJavaClassSources(String fileExtension) { 
        javaClassBox = new ArrayList<>() ;
        try {
            ArrayList<Path> paths = (ArrayList<Path>) getJavaFilepaths(directoryPath);
            
            // Loop for make ClassBox objects
            for (Path p : paths) {
            	ClassBox cbox = new ClassBox(getJaveClassSource(p.toString()));
                javaClassBox.add(cbox); 
                
                // Set extends and interface relationship 
                if (!cbox.getJavaClassSource().getSuperType().contentEquals("java.lang.Object")) {
                	cbox.setExtends(cbox.getJavaClassSource().getSuperType());
                }
                for (String interf : cbox.getJavaClassSource().getInterfaces()) {
                	cbox.setInterface(interf);
                }
            }
            
            // Loop for get a max width and height of a Classbox
            for (int i=0; i<javaClassBox.size(); i++) {
                maxHeight = Math.max(maxHeight, javaClassBox.get(i).getHeight());
                maxWidth = Math.max(maxWidth, javaClassBox.get(i).getHeight());
            }
            
            // Loop for setCoordinate of Boxes(class, attributes, methods) in a ClassBox
            for (int i=0; i<javaClassBox.size(); i++) {
            	javaClassBox.get(i).setCoordinate(i, maxWidth, maxHeight);
            }
            
        } catch (IOException e) {
            System.err.println("Cannot find the path") ; 
        }

        return javaClassBox ;
    }

    // Get a .java file from a directoryPath recursively
    public static List<Path> getJavaFilepaths(String directoryPath) throws IOException {
        Path dirPath = Paths.get(directoryPath) ; 
        
        if (!Files.exists(dirPath)) {
            throw new IOException("Path must be a directory"); 
        }

        List<Path> result = new ArrayList<>(); 
        
        try (Stream<Path> walk = Files.walk(dirPath)) { 
            for (Iterator<Path> iter = walk.iterator() ; iter.hasNext() ; ) {
                Path p = iter.next() ; 

                if (Files.isRegularFile(p) && p.toString().endsWith(".java")) {
                    result.add(p) ;
                } 
            }
        }       
        return result ; 
    }
    
    // Make a .java file into a JavaClassSource object 
    public static JavaClassSource getJaveClassSource(String directoryPath) throws IOException { 
        BufferedReader br = new BufferedReader(new FileReader(directoryPath));

        String line ; 
        String javacode = ""; 
        while ( (line = br.readLine()) != null ){ 
            javacode += line ; 
        }   
        br.close(); 
        
        JavaUnit unit = Roaster.parseUnit(javacode);
        JavaClassSource javaClassSource = unit.getGoverningType();
        return javaClassSource;
    }

    // (Main) Draw a .drawio file
    public void createDrawio(){
    	
    	// create the xml file
        initXMLfile();
        
        // transform the DOM Object to an XML File
        createFile();
        
        System.out.println("Done creating XML File");
    }

    // create the xml file
    private void initXMLfile(){
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        
            Element diagram = document.createElement("diagram");
            addAttr(diagram, "id", "SNbYQcsz_Utg5FWgZMJS");
            addAttr(diagram, "name", "Page-1"); // 
            document.appendChild(diagram);
 
            // Employee element (Constant)
            Element mxGraphModel = document.createElement("mxGraphModel");
            addAttr(mxGraphModel, "dx", "332");
            addAttr(mxGraphModel, "dy", "241");
            addAttr(mxGraphModel, "grid", "1");
            addAttr(mxGraphModel, "gridSize", "10");
            addAttr(mxGraphModel, "guides", "1");
            addAttr(mxGraphModel, "tooltips", "1");
            addAttr(mxGraphModel, "connect", "1");
            addAttr(mxGraphModel, "arrows", "1");
            addAttr(mxGraphModel, "fold", "1");
            addAttr(mxGraphModel, "page", "1");
            addAttr(mxGraphModel, "pageScale", "1");
            addAttr(mxGraphModel, "pageWidth", "850");
            addAttr(mxGraphModel, "pageHeight", "1100");
            addAttr(mxGraphModel, "math", "0");
            addAttr(mxGraphModel, "shadow", "0");

            diagram.appendChild(mxGraphModel);
 
            root = document.createElement("root");
            mxGraphModel.appendChild(root);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }
    
    //transform the DOM Object to an XML File
    private void createFile(){
    	int i = 1;
 
    	// Loop for draw a classboxes 
        for( ClassBox classbox : javaClassBox){
            drawClass(classbox, i);
        	i++;
        }
        
        // Loop for draw a relationship lines
        for( ClassBox classbox : javaClassBox){
            if (classbox.getExtends() != "") {	 
            	ClassBox target = null;
            	// Loop for find extends relationship
                for( ClassBox b : javaClassBox){
                	if(classbox.getClassId() == b.getClassId()) continue;
                	if(classbox.getExtends().equals(b.getJavaClassSource().getName())) {
                		target = b;
                		break;
                	}
                }
                if (target != null) {
                	// Draw a extends line
                	drawLines(0, target, classbox);
                }
            }
            
            if (classbox.getInterface()!= "") {
            	ClassBox target = null;
            	// Loop for find implements relationship
                for( ClassBox b : javaClassBox){
                	if(classbox.getClassId() == b.getClassId()) continue;
                	if(classbox.getInterface().equals(b.getJavaClassSource().getName())) {
                		target = b;
                		break;
                	}
                }
                if (target != null) {
                	// Draw a interface line
                	drawLines(1, target, classbox);
                }
            }
        }
        //transform the DOM Object to an XML File        
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(destinationPath));
            transformer.transform(domSource, streamResult);

            StreamResult stdoutResult = new StreamResult(System.out);
            transformer.transform(domSource, stdoutResult);
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    // helper function for draw
    private void addAttr(Element element, String attrName, String attrValue){
        Attr attr = document.createAttribute("id");
        attr.setValue(attrValue);
        element.setAttribute(attrName, attrValue);
    }
    
    // helper function for draw2 (Child)
    private void addmxGeometry(Element element, int x, int y, int width, int height){
        Element mxGeometry = document.createElement("mxGeometry");
        if(x != -1)
            addAttr(mxGeometry, "x", Integer.toString(x));
        if(y != -1)
            addAttr(mxGeometry, "y", Integer.toString(y)) ;
        addAttr(mxGeometry, "width", Integer.toString(width));
        addAttr(mxGeometry, "height", Integer.toString(height));
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }
    
    // helper function for draw3 (Lines)
    private void addLinemxGeometry(Element element, ClassBox target, ClassBox source){
        Element mxGeometry = document.createElement("mxGeometry");
        addAttr(mxGeometry, "width", "160");
        addAttr(mxGeometry, "relative", "1");
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }

    // Drawing a classboxes
    private void drawClass(ClassBox classbox, int cid){
    	
    	// for XML header
    	if(cid == 1) {
    		Element biggestBox = document.createElement("mxCell");
    		addAttr(biggestBox, "id", Integer.toString(id++));
    		root.appendChild(biggestBox);

    		int biggerBoxID = id;
    		Element biggerBox = document.createElement("mxCell");
    		addAttr(biggerBox, "id", Integer.toString(id++));
    		addAttr(biggerBox, "parent", "0");
    		root.appendChild(biggerBox);
    	}

    	int classID = id;
        classbox.setID(id);

        // Draw a ClassNameBox
        Element classNameBox = document.createElement("mxCell");
        addAttr(classNameBox, "id", Integer.toString(id++));
        addAttr(classNameBox, "value", classbox.getJavaClassSource().getName());
        addAttr(classNameBox, "style", classNameStyleConstant);
        addAttr(classNameBox, "vertex", "1");
        addAttr(classNameBox, "parent", "1");
        root.appendChild(classNameBox);
        
        addmxGeometry(classNameBox, classbox.getX(), classbox.getY(), classbox.getWidth(), classbox.getHeight()) ;

        // Draw a AttributesBox
        int y = classbox.getFieldboxInfo().getY();
        List<FieldSource<JavaClassSource>> fieldList = classbox.getJavaClassSource().getFields() ; 
        for (FieldSource<JavaClassSource> field : fieldList) { 
            drawField(field, classID, y, classbox.getWidth());
            y +=26;
        }
        
        // Draw a SeperatorLine
        drawSeperatorLine(classID, y, classbox.getWidth());
        y += 8;

        // Draw a MethodsBox
        List<MethodSource<JavaClassSource>> methodLists = classbox.getJavaClassSource().getMethods() ;
        for (MethodSource<JavaClassSource> method : methodLists ){
            if(method.getName().equals(classbox.getJavaClassSource().getName()))
                continue;
            drawMethod(method, classID, y, classbox.getWidth());
            y +=26;
        }
       
    }

    // Draw a AttributesBox
    private void drawField(FieldSource<JavaClassSource> f, int classID, int y, int width){
        Element fieldBox = document.createElement("mxCell");
        addAttr(fieldBox, "id", Integer.toString(id++));
    
        String valueString = "";
        
        if(f.getVisibility() == Visibility.PUBLIC){ 
            valueString = "+ " + f.getName() + " : " + f.getType();
        } else if(f.getVisibility() == Visibility.PRIVATE){
            valueString = "- " + f.getName() + " : " + f.getType();
        }
        
        addAttr(fieldBox, "value", valueString); 
        addAttr(fieldBox, "style", inclassStyleConstant);
        addAttr(fieldBox, "vertex", "1"); 
        addAttr(fieldBox, "parent", Integer.toString(classID));
        addmxGeometry(fieldBox, -1, y , width, 26);
        root.appendChild(fieldBox);
    }
    
    // Draw a SeperatorLine
    private void drawSeperatorLine(int classID, int y, int width){
        Element seperator = document.createElement("mxCell");
        addAttr(seperator, "id", Integer.toString(id++));
        addAttr(seperator, "style", speratorLineStyleConstant);
        addAttr(seperator, "vertex", "1"); 
        addAttr(seperator, "parent", Integer.toString(classID));
        addmxGeometry(seperator, -1, y , width, 8);
        root.appendChild(seperator);
    }
    
    // Draw a MethodsBox
    private void drawMethod(MethodSource<JavaClassSource> m, int classID, int y, int width){

        Element methodBox = document.createElement("mxCell");
        addAttr(methodBox, "id", Integer.toString(id++));
        
        String valueString = "";
       
        List<ParameterSource<JavaClassSource>> paraList = m.getParameters();
        String params = "";
        
        for (int i=0; i<paraList.size(); i++) {
           	if (i > 0) {
        		params = params.concat(", ");
        	}

        	params = params.concat(paraList.get(i).toString().split(" ")[0]);
        }
                
        if(m.getVisibility() == Visibility.PUBLIC){ 
            valueString = "+ " + m.getName() + "(" + params + "): " + m.getReturnType();
        } else if(m.getVisibility() == Visibility.PRIVATE){
            valueString = "+ " + m.getName() + "(" + params + "): " + m.getReturnType();
        }
        addAttr(methodBox, "value", valueString);
        addAttr(methodBox, "style", inclassStyleConstant);
        addAttr(methodBox, "vertex", "1");
        
        addAttr(methodBox, "parent", Integer.toString(classID));
        addmxGeometry(methodBox, -1, y , width, 26);
        root.appendChild(methodBox);
    }

    // Draw a Relationship lines
    private void drawLines(int value, ClassBox target, ClassBox source){

        Element lines = document.createElement("mxCell");
        addAttr(lines, "id", Integer.toString(id++));
      

        if(value == 0) {
            addAttr(lines, "value", "Extends");
        	addAttr(lines, "style", extendLineStyleConstant);         
        }
        else {
            addAttr(lines, "value", "");
        	addAttr(lines, "style", interfaceLineStyleConstant);       
        }
        	
        addAttr(lines, "edge", "1"); 
        addAttr(lines, "parent", "1"); 

        addAttr(lines, "source", source.getClassId() + "");
        addAttr(lines, "target", target.getClassId() + ""); 
        
        addLinemxGeometry(lines, target, source);
        root.appendChild(lines);
    }


}