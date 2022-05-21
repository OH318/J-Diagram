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
    // private ArrayList<JavaClassSource> classes ; 
    // ID, [Diagram, JavaClassSource]
    private HashMap<String, CoderClassDiagram > classes ;
    private ArrayList<Edges> lines; 

    public Coder() { 
        classes = new HashMap<String, CoderClassDiagram>() ; 
        lines = new ArrayList<Edges>() ;
    }

    public CoderClassDiagram createJavaClassSourceAndSetLocation(int id, Element element) {
        CoderClassDiagram classDiagram = new CoderClassDiagram() ; 

        JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);	 
        javaClassSource.setName(element.getAttribute("value"));

        classDiagram.setJavaClassSource(javaClassSource) ; 
        Diagram diagram = classDiagram.getDiagram() ; 
        
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

    public void setDataFromXML(File drawioFile) throws IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null ;
        Document doc = null ; 

        // ArrayList<CoderClassDiagram> classDiagrams = new ArrayList<CoderClassDiagram>() ; 

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

                // Diagram id 
                
                if(element.getAttribute("parent").compareTo("1") == 0 
                        && !element.getAttribute("style").contains("endArrow")) {

                    type = 0 ; 
                    // Create JavaClassSource & Set Location and Size
                    classDiagram = createJavaClassSourceAndSetLocation(id, element) ; 

                    classDiagram.getDiagram().setId(element.getAttribute("id"));

                    if ( classDiagram != null ) classes.put(element.getAttribute("id"), classDiagram) ;
                    //  classDiagrams.add(classDiagram) ; 
                    continue ;
                } else if(element.getAttribute("style").contains("endArrow")) {
                    Edges edges = new Edges() ; 
                    String style = element.getAttribute("style");

                    if(element.getAttribute("source").length() != 0) {
                        // set source class diagram id in edges 
                        edges.setSourceClassDiagramId(element.getAttribute("source")) ; 
                    }
                    
                    if(element.getAttribute("target").length() != 0) {
                        // set target class diagram id in edges
                        edges.setTargetClassDiagramId(element.getAttribute("target")) ; 
                    }

                    // JavaClassSource jss = classDiagram.getJavaClassSource() ;

                    // Identify Arrows check line method
                    // type, source, target point?
                    edges.setArrowType(edges.identifyArrow(style)) ; 
                    
                    // Q. What is better between ArrayList<Edges> without Lines class between now state?  
                    // TODO: set SourcePoint and TargetPoint 
                    lines.add(edges) ; 
                } 

                //identify attributes and method
                if(element.getAttribute("style").contains("line")) {
                    type = 1 ;
                    continue ; 
                }

                String value = element.getAttribute("value");
                String parentId = element.getAttribute("parent");

                // TODO
                // Q. Connection check? 
                if ( classes.containsKey(parentId) ) {
                    String[] attrs = value.split(" ") ; 
                    
                    if ( type == 1 ) { 
                        
                    }
                    CoderClassDiagram ccd = classes.get(parentId) ;
                    ccd.addFieldAndMethodsInJavaClassSource(attrs, ccd.getJavaClassSource(), type); 
                }
            }   
        }

        // TODO: Inheritance & Interface 
        for ( String key : classes.keySet() ) { 
            CoderClassDiagram ccd = classes.get(key) ; 

            System.out.println("ID: " +key) ; 
            for (int j = 0 ; j < lines.size(); j++) { 

                // Start Point is child. 
                String sourceId = lines.get(j).getSourceClassDiagramId() ;
                
                // Target point is parent. 
                if ( ccd.getDiagram().getId().equals(sourceId)) { 
                    String parentId = lines.get(j).getTargetClassDiagramId();   
                    
                    System.out.println(sourceId + " " +  parentId) ;

                    // Arrow Type (generalization) 
                    if ( lines.get(j).getArrowType() == 0 ) { 
                        if ( classes.get(parentId)  == null ) {
                            System.err.println("Debug1") ; 
                        }
                        else ccd.getJavaClassSource().extendSuperType(classes.get(parentId).getJavaClassSource().getClass()) ;  // ?
                    }
                    // Arrow Type (implementation) 
                    else if ( lines.get(j).getArrowType() == 1) { 
                        if ( classes.get(parentId).getJavaClassSource() == null ) {
                            System.err.println("Debug2") ; 
                        }
                        ccd.getJavaClassSource().implementInterface(classes.get(parentId).getJavaClassSource().getClass()) ; // ? 
                    }
                }   
            }
        }
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
    public boolean createSourceCodes(String javaPath, String drawioPath) { 
        
        /**
         * TODO:
         *      We should be entered by user for parsing drawio file and making directory.  
         */
        // "/Users/jinil/Desktop/Drawio/car.drawio"
        File file = new File(drawioPath) ; 

        try {
            setDataFromXML(file) ;

            if ( classes == null ) { 
                return false ; 
            } 

            for (String id : classes.keySet()) { 
                CoderClassDiagram ccd = classes.get(id) ;
                try {
                    JavaClassSource javaClass = ccd.getJavaClassSource() ; 
                    String className = javaClass.getName() ; 
                    String classPath = String.format("%s/%s.java", javaPath, className); 
                    
                    //check if any syntax error
                    if(javaClass != null && javaClass.hasSyntaxErrors()){
                        System.err.println("SyntaxError: "+javaClass.getSyntaxErrors());
                        continue ; 
                    }

                    FileUtils.forceMkdir(new File(javaPath));
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

        return true; 
    }
}
