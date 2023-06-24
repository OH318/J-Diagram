package com.handong.oh318;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.EnumConstantSource;
import org.jboss.forge.roaster.model.source.ExtendableSource;
import org.jboss.forge.roaster.model.source.FieldHolderSource;
// JavaClassSource
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.InterfaceCapableSource;
import org.jboss.forge.roaster.model.source.JavaEnumSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MethodHolderSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Extractor<O extends JavaSource<O>> extends UserInput {
    private String drawioPath ; // drawio path that .drawio file will be created
    private String directoryPath ; // Java classes directory path by user input
    private ArrayList<ClassBox<O>> javaClassBox ; // Rapper Class for JavaClassSource
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
        this.directoryPath = directoryPath;
        this.drawioPath = drawioFilepath;
        this.javaClassBox = getJavaClassSources(".java");
        this.id = 0;
    }

    /**
     * Construct JavaClassSources from path received by user
     *
     * @param fileExtension
     *          fileExtenstion for read only .java files
     *
     * @return ArrayList<ClassBox>
     *          ArrayList for keep a classbox objects
     */
    public ArrayList<ClassBox<O>> getJavaClassSources(String fileExtension) {
        javaClassBox = new ArrayList<>() ;
        ArrayList<Path> paths = new ArrayList<>();

        try {
            paths = (ArrayList<Path>) getJavaFilepaths(directoryPath);
        } catch (IOException e) {
            System.err.println("Could not get .java files from " + directoryPath + ". " + e.getLocalizedMessage()) ;
        }

        // Loop for make ClassBox objects
        for (Path p : paths) {
            try {
                JavaSource<O> javaSource = (JavaSource<O>)getJavaClassSource(p.toString());
                ClassBox<O> cbox = new ClassBox<O>(javaSource);
                javaClassBox.add(cbox);

                // Set extends and interface relationship
                if(cbox.getJavaSource() instanceof ExtendableSource) {
                    ExtendableSource<O> es = (ExtendableSource<O>) cbox.getJavaSource();
                    if (!es.getSuperType().contentEquals("java.lang.Object")) {
                        cbox.setExtends(es.getSuperType());
                    }
                }

                if(cbox.getJavaSource() instanceof InterfaceCapableSource) {
                    InterfaceCapableSource<O> ics = (InterfaceCapableSource<O>) cbox.getJavaSource();
                    for (String interf : ics.getInterfaces()) {
                        cbox.setInterface(interf);
                    }
                }
            } catch (IOException e) {
                System.err.println("Cannot find the path " + p.toString() + ". " + e.getLocalizedMessage()) ;
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

        return javaClassBox ;
    }

    /**
     * Get .java files from a directoryPath recursively
     *
     * @param directoryPath
     *          directoryPath received by user
     * @return List<Path>
     *
     * @throws IOException
     */
    public List<Path> getJavaFilepaths(String directoryPath) throws IOException {
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

    /**
     * Make a .java file into a JavaClassSource object
     *
     * @param javaFilePath
     *          exact .java source code file path
     * @return JavaClassSource
     *          Contain various class information
     * @throws IOException
     */
    public JavaSource<?> getJavaClassSource(String javaFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(javaFilePath));

        String line ;
        String javacode = "";
        while ( (line = br.readLine()) != null ){
            javacode += line ;
        }
        br.close();

        JavaUnit unit = Roaster.parseUnit(javacode);
        JavaSource<?> javaClassSource = unit.getGoverningType();
        return javaClassSource;
    }

    /**
     * Main for draw a .drawio XML file
     */
    public void createDrawio(){

    	// create the xml file
        initXMLfile();

        // transform the DOM Object to an XML File
        createFile();

        System.out.println("Done creating XML File");
    }

    /**
     * create the xml file
     * Most parts are consist of constant value
     * You can fix or update this part by yourself.
     */
    public void initXMLfile(){
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

    /**
     * Create XML file form Classbox that Wrapper class of JavaClassSource object
     * First loop is for Class
     * Second loop is for Line (Extends, Interface, ... will be updated)
     */
    public void createFile(){
    	int i = 1;

    	// Loop for draw a classboxes
        for( ClassBox<O> classbox : javaClassBox){
            drawClass(classbox, i);
        	i++;
        }

        // Loop for draw a relationship lines
        for( ClassBox<O> classbox : javaClassBox){
            if (classbox.getExtends() != "") {

            	ClassBox<O> target = null;
            	// Loop for find extends relationship
                for( ClassBox<O> b : javaClassBox){
                	if(classbox.getClassId() == b.getClassId()) continue;

                	if(classbox.getExtends().endsWith(b.getJavaSource().getName())) {
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
            	ClassBox<O> target = null;
            	// Loop for find implements relationship
                for( ClassBox<O> b : javaClassBox){
                	if(classbox.getClassId() == b.getClassId()) continue;
                	if(classbox.getInterface().equals(b.getJavaSource().getName())) {
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
            StreamResult streamResult = new StreamResult(new File(drawioPath));
            transformer.transform(domSource, streamResult);

            // Standard Output
            // StreamResult stdoutResult = new StreamResult(System.out);
            // transformer.transform(domSource, stdoutResult);
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    /**
     * helper function for draw
     *
     * @param element
     *
     * @param attrName
     *
     * @param attrValue
     */
    public void addAttr(Element element, String attrName, String attrValue){
        Attr attr = document.createAttribute("id");
        attr.setValue(attrValue);
        element.setAttribute(attrName, attrValue);
    }

    // helper function for draw2 (Child)
    public void addmxGeometry(Element element, int x, int y, int width, int height){
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
    public void addLinemxGeometry(Element element, ClassBox<O> target, ClassBox<O> source){
        Element mxGeometry = document.createElement("mxGeometry");
        addAttr(mxGeometry, "width", "160");
        addAttr(mxGeometry, "relative", "1");
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }

    /**
     * Drawing a classboxes on XML file
     *
     * @param classbox
     *      Wapper class for JavaClassSoure contining class information
     * @param cid
     *      variable for prevent a duplicated XML
     */
    public void drawClass(ClassBox<O> classbox, int cid){

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
        addAttr(classNameBox, "value", classbox.getJavaSource().getName());
        addAttr(classNameBox, "style", classNameStyleConstant);
        addAttr(classNameBox, "vertex", "1");
        addAttr(classNameBox, "parent", "1");
        root.appendChild(classNameBox);

        addmxGeometry(classNameBox, classbox.getX(), classbox.getY(), classbox.getWidth(), classbox.getHeight()) ;

        // Draw a EnumConstantsBox
        int y = classbox.getFieldboxInfo().getY();
        if(classbox.getJavaSource() instanceof JavaEnumSource) {
            JavaEnumSource jes = (JavaEnumSource) classbox.getJavaSource();
            List<EnumConstantSource> enumConstants = jes.getEnumConstants();
            for (EnumConstantSource constant : enumConstants) {
                drawEnumConstant(constant, classID, y, classbox.getWidth());
                y +=26;
            }
        }

        // Draw a AttributesBox
        else if(classbox.getJavaSource() instanceof FieldHolderSource) {
            FieldHolderSource<O> fhs = (FieldHolderSource<O>) classbox.getJavaSource();
            List<FieldSource<O>> fieldList = fhs.getFields() ;
            for (FieldSource<O> field : fieldList) {
                drawField(field, classID, y, classbox.getWidth());
                y +=26;
            }
        }

        // Draw a SeperatorLine
        drawSeperatorLine(classID, y, classbox.getWidth());
        y += 8;

        // Draw a MethodsBox
        if(classbox.getJavaSource() instanceof MethodHolderSource) {
            MethodHolderSource<O> mhs = (MethodHolderSource<O>) classbox.getJavaSource();
            List<MethodSource<O>> methodLists = mhs.getMethods() ;
            for (MethodSource<O> method : methodLists ){
                // if(method.getName().equals(classbox.getJavaClassSource().getName()))
                //     continue;
                drawMethod(method, classID, y, classbox.getWidth());
                y +=26;
            }
        }

    }

    /**
     * Drawing a box for an enum constant
     *
     * @param f
     *      Objects with information for the field
     * @param classID
     *      parent class id of this field
     * @param y
     *      y-axis for field
     * @param width
     *      width for field
     */
    public void drawEnumConstant(EnumConstantSource f, int classID, int y, int width){
        Element fieldBox = document.createElement("mxCell");
        addAttr(fieldBox, "id", Integer.toString(id++));

        String valueString = f.getName();
        addAttr(fieldBox, "value", valueString);
        addAttr(fieldBox, "style", inclassStyleConstant);
        addAttr(fieldBox, "vertex", "1");
        addAttr(fieldBox, "parent", Integer.toString(classID));
        addmxGeometry(fieldBox, -1, y , width, 26);
        root.appendChild(fieldBox);
    }

    /**
     * Drawing a fieldbox
     *
     * @param f
     *      Objects with information for the field
     * @param classID
     *      parent class id of this field
     * @param y
     *      y-axis for field
     * @param width
     *      width for field
     */
    public void drawField(FieldSource<O> f, int classID, int y, int width){
        Element fieldBox = document.createElement("mxCell");
        addAttr(fieldBox, "id", Integer.toString(id++));

        String valueString = "";

        String typesArgs = "" ;
        int typeArgsSize = f.getType().getTypeArguments().size() ;

        for (int i = 0 ; i < typeArgsSize; i++ ) {
            if ( i > 0 ) {
                typesArgs += (f.getType().getTypeArguments().get(i).toString() + ", ") ;
            } else {
                typesArgs += f.getType().getTypeArguments().get(i).toString();
            }
        }

        if ( typeArgsSize > 0 )  {

            if(f.getVisibility() == Visibility.PUBLIC){
                valueString = ("+ " + f.getName() + " : " + f.getType() + "&lt;" + typesArgs + "&gt;");
            } else if (f.getVisibility() == Visibility.PROTECTED){
                valueString = ("# " + f.getName() + " : " + f.getType() + "&lt;" + typesArgs + "&gt;");
            } else if(f.getVisibility() == Visibility.PRIVATE){
                valueString = ("- " + f.getName() + " : " + f.getType() + "&lt;" + typesArgs + "&gt;");
            }
        } else {
            if(f.getVisibility() == Visibility.PUBLIC){
                valueString = "+ " + f.getName() + " : " + f.getType() + typesArgs;
            } else if (f.getVisibility() == Visibility.PROTECTED){
                valueString = "# " + f.getName() + " : " + f.getType() + typesArgs;
            } else if(f.getVisibility() == Visibility.PRIVATE){
                valueString = "- " + f.getName() + " : " + f.getType() + typesArgs;
            }
        }


        addAttr(fieldBox, "value", valueString);

        if (f.isStatic()){
            if ( !f.isFinal() ) {
                addAttr(fieldBox, "style", inclassStyleConstant + "fontStyle=4;");
            } else {
                addAttr(fieldBox, "style", inclassStyleConstant);
            }
        } else {
            addAttr(fieldBox, "style", inclassStyleConstant);
        }

        addAttr(fieldBox, "vertex", "1");
        addAttr(fieldBox, "parent", Integer.toString(classID));
        addmxGeometry(fieldBox, -1, y , width, 26);
        root.appendChild(fieldBox);
    }

    // Draw a SeperatorLine
    public void drawSeperatorLine(int classID, int y, int width){
        Element seperator = document.createElement("mxCell");
        addAttr(seperator, "id", Integer.toString(id++));
        addAttr(seperator, "style", speratorLineStyleConstant);
        addAttr(seperator, "vertex", "1");
        addAttr(seperator, "parent", Integer.toString(classID));
        addmxGeometry(seperator, -1, y , width, 8);
        root.appendChild(seperator);
    }

    // Draw a MethodsBox
    public void drawMethod(MethodSource<O> m, int classID, int y, int width){

        Element methodBox = document.createElement("mxCell");
        addAttr(methodBox, "id", Integer.toString(id++));

        String valueString = "";

        List<ParameterSource<O>> paraList = m.getParameters();
        String params = "";

        for (int i=0; i<paraList.size(); i++) {
           	if (i > 0) {
        		params = params.concat(", ");
        	}

        	params = params.concat(paraList.get(i).toString().split(" ")[0]);
        }

        if ( m.isConstructor() ) {

            if(m.getVisibility() == Visibility.PUBLIC){
                valueString = "+ " + m.getName() + "(" + params + ")";
            } else if (m.getVisibility() == Visibility.PROTECTED){
                valueString = "# " + m.getName() + "(" + params + ")";
            } else if(m.getVisibility() == Visibility.PRIVATE){
                valueString = "+ " + m.getName() + "(" + params + ")";
            }
        } else {
            String typesArgs = "" ;
            int typeArgsSize = m.getReturnType().getTypeArguments().size() ;

            for (int i = 0 ; i < typeArgsSize; i++ ) {
                if ( i > 0 ) {
                    typesArgs += (m.getReturnType().getTypeArguments().get(i).toString() + ", ") ;
                } else {
                    typesArgs += m.getReturnType().getTypeArguments().get(i).toString();
                }
            }

            if ( typeArgsSize > 0 )  {
                if(m.getVisibility() == Visibility.PUBLIC){
                    valueString = "+ " + m.getName() + "(" + params + "): " + m.getReturnType() + "&lt;" + typesArgs + "&gt;";
                } else if (m.getVisibility() == Visibility.PROTECTED){
                    valueString = "# " + m.getName() + "(" + params + "): " + m.getReturnType() + "&lt;" + typesArgs + "&gt;";
                } else if(m.getVisibility() == Visibility.PRIVATE){
                    valueString = "+ " + m.getName() + "(" + params + "): " + m.getReturnType() + "&lt;" + typesArgs + "&gt;";
                }
            } else {
                if(m.getVisibility() == Visibility.PUBLIC){
                    valueString = "+ " + m.getName() + "(" + params + "): " + m.getReturnType();
                } else if (m.getVisibility() == Visibility.PROTECTED){
                    valueString = "# " + m.getName() + "(" + params + "): " + m.getReturnType();
                } else if(m.getVisibility() == Visibility.PRIVATE){
                    valueString = "+ " + m.getName() + "(" + params + "): " + m.getReturnType();
                }
            }
        }

        addAttr(methodBox, "value", valueString);
        if (m.isStatic()){
            addAttr(methodBox, "style", inclassStyleConstant + "fontStyle=4;");
        } else {
            addAttr(methodBox, "style", inclassStyleConstant);
        }

        addAttr(methodBox, "vertex", "1");

        addAttr(methodBox, "parent", Integer.toString(classID));
        addmxGeometry(methodBox, -1, y , width, 26);
        root.appendChild(methodBox);
    }

    /**
     * Draw a Relationship lines
     * @param value
     *      Variable to distinguish types of relationship line
     * @param target
     *      End classbox of line
     * @param source
     *      Start classbox of line
     */
    public void drawLines(int value, ClassBox<O> target, ClassBox<O> source){

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

    public String getDrawioPath() {
        return this.drawioPath;
    }

    public String getDirectoryPath() {
        return this.directoryPath ;
    }

    public Document getDocument() {
        return this.document ;
    }

}