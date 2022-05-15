package com.handong.oh318 ;

import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.writeStringToFile; 

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class Coder extends UserInput implements Parse{
    private TempNameGenerator tempNameGenerator ; 
    // private ArrayList<JavaClassSource> classes ; 
    // ID, [Diagram, JavaClassSource]
    private HashMap<Integer, CoderClassDiagram > classes ;
    private Lines lines; 

    public Coder() { 
        classes = new HashMap<Integer, CoderClassDiagram>() ; 
        tempNameGenerator = new TempNameGenerator() ; 
        lines = new Lines() ;
    }

    public CoderClassDiagram createJavaClassSourceAndSetLocation(int id, Element element) {
        CoderClassDiagram classDiagram = new CoderClassDiagram() ; 

        JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);	 
        javaClassSource.setName(element.getAttribute("value"));

        classDiagram.setJavaClassSource(javaClassSource) ; 
        Diagram diagram = classDiagram.getDiagram() ; 
        classes.put(id, classDiagram) ;
        
        NodeList coordinate = element.getElementsByTagName("mxGeometry");  
        if ( coordinate.getLength() != 0 ) {

            for (int i = 0 ; i < coordinate.getLength(); i++) {
                Element node = (Element)coordinate.item(i) ; 

                if(node.getAttribute("width").length() != 0){
                    diagram.setWidth(Integer.parseInt(node.getAttribute("width")));
                }
                
                if(node.getAttribute("height").length() != 0){
                    diagram.setHeight(Integer.parseInt(node.getAttribute("height")));
                }
                
                if(node.getAttribute("x").length() != 0){
                    diagram.getPoint().setX(Integer.parseInt(node.getAttribute("x")));
                }
                
                if(node.getAttribute("y").length() != 0){
                    diagram.getPoint().setY(Integer.parseInt(node.getAttribute("y")));
                }
            }
        } 
        
        return classDiagram ; 
    }
    
    /**
     * @getDataFromXML
     * @param drawioFile
     * @return JavaClassSource
     * @throws IOException
     * @throws SAXException
     */

    public ArrayList<CoderClassDiagram> getDataFromXML(File drawioFile) throws IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null ;
        Document doc = null ; 

        ArrayList<CoderClassDiagram> classDiagrams = new ArrayList<CoderClassDiagram>() ; 

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            System.err.println("Cannot build the document!") ;
        }

        try {
            doc = dBuilder.parse(drawioFile); 
        } catch ( SAXException e) { 
            System.err.println("Cannot parse the XML file!") ; 
        }
 
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("mxCell");    
        
        int type = 0 ; // 0: field, 1: method

        for (int i = 0 ; i < nList.getLength(); i++)
        { 
            CoderClassDiagram classDiagram = null ; 
            Node node = nList.item(i) ; 

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                int id = Integer.parseInt(element.getAttribute("id")) ; 
                if ( id <= 1 ) continue ;

                if(element.getAttribute("parent").compareTo("1") == 0 
                        && !element.getAttribute("style").contains("endArrow")) {

                    type = 0 ; 
                    // Create JavaClassSource & Set Location and Size
                    classDiagram = createJavaClassSourceAndSetLocation(id, element) ; 

                    if ( classDiagram != null ) classDiagrams.add(classDiagram) ; 
                    continue ;
                } else if(element.getAttribute("style").contains("endArrow")) {
                    Edges edges = new Edges() ; 
                    // String style = element.getAttribute("style");

                    if(element.getAttribute("source").length() != 0) {
                        int sourceClassDiagramId = Integer.parseInt(element.getAttribute("source")) ;

                        // set source class diagram id in edges 
                        edges.setSourceClassDiagramId(sourceClassDiagramId) ; 
                    }
                    
                    if(element.getAttribute("target").length() != 0) {
                        int targetClassDiagramId = Integer.parseInt(element.getAttribute("target")) ; 

                        // set target class diagram id in edges
                        edges.setTargetClassDiagramId(targetClassDiagramId) ; 
                    }

                    // Identify Arrows check line method
                    // type, source, target point?
                    // edges.setArrowType(edges.identifyArrow(style)) ; 
                    
                    // Q. What is better between ArrayList<Edges> without Lines class between now state?  
                    // TODO: set SourcePoint and TargetPoint 
                    // lines.addArrow(edges.identifyArrow(style), source, target);
                } 

                //identify attributes and method
                if(element.getAttribute("style").contains("line")) {
                    type = 1 ;
                    continue ; 
                }

                String value = element.getAttribute("value");
                int parentId = Integer.parseInt(element.getAttribute("parent"));

                // TODO
                // Q. Connection check? 
                if ( classes.containsKey(parentId) ) {
                    String[] attrs = value.split(" ") ; 
                    
                    CoderClassDiagram ccd = classes.get(parentId) ;
                    ccd.addFieldAndMethodsInJavaClassSource(attrs, ccd.getJavaClassSource(), type, tempNameGenerator); 
                }
            }   
        }

    
        return classDiagrams;
    }

    /**
     * getJavaClassSources
     * @param path
     *      A path is directory path entered by user.  
     *      We can create the Java source codes in the directory. 
     * 
     * @return ArrayList<JavaClassSource>
     *      
     */
    @Override
    public ArrayList<JavaClassSource> getJavaClassSources(String path)  {
        // TODO 

        return null ;
    }

    /**
     * 
     * @param path
     *      A path is a directory for creating Java source codes
     * @return
     *      If Java files is created, it returns true. 
     *      Otherwise, it returns false ; 
     */
    public boolean createSourceCodes(String path) { 
        
        /**
         * TODO:
         *      We should be entered by user for parsing drawio file and making directory.  
         */
        File file = new File("/Users/jinil/Desktop/Drawio/car.drawio") ; 
        ArrayList<CoderClassDiagram> classDiagrams = null ; 
        try {
            classDiagrams = getDataFromXML(file) ;

            for (CoderClassDiagram ccd : classDiagrams) { 
                //output to file
                
                try {
                    JavaClassSource javaClass = ccd.getJavaClassSource() ; 
                    String className = javaClass.getName() ; 
                    String classPath = String.format("%s/%s.java", path, className); 
                    
                    //check if any syntax error
                    if(javaClass != null && javaClass.hasSyntaxErrors()){
                        System.err.println("SyntaxError: "+javaClass.getSyntaxErrors());
                        continue ; 
                    }

                    FileUtils.forceMkdir(new File(path));
                    writeStringToFile(new File(classPath), javaClass.toString(), Charset.defaultCharset(), false);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    System.out.println("IOException: "); 
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("IOException: cannot get the data from XML"); 
        } 

        if ( classDiagrams == null ) { 
            return false ; 
        } else { 
            return true ;  
        }
    }
}
