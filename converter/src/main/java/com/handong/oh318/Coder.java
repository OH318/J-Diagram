package com.handong.oh318 ;

import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class Coder extends UserInput {
    // private ArrayList<JavaClassSource> classes ; 
    // ID, [Diagram, JavaClassSource]
    private HashMap<String, CoderClassDiagram > classes ;
    private ArrayList<Edges> lines; 

    public Coder() { 
        classes = new HashMap<String, CoderClassDiagram>() ; 
        lines = new ArrayList<Edges>() ;
    }

    public HashMap<String, CoderClassDiagram> getClasses() { 
        return this.classes ; 
    }

    public ArrayList<Edges> getLines() {
        return this.lines ; 
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

    public boolean setDataFromXML(File drawioFile) throws IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null ;
        Document doc = null ; 

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("Cannot build the document!") ;
        }

        try {
            doc = dBuilder.parse(drawioFile); 
        } catch ( SAXException e) { 
            System.err.println("Cannot parse the XML file!") ; 
        }
        
        if ( doc == null ) return false;
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("mxCell");    
        
        int type = 0 ; // 0: field, 1: method

        for (int i = 0 ; i < nList.getLength(); i++)
        { 
            CoderClassDiagram classDiagram = null ; 
            Node node = nList.item(i) ; 

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                // Diagram id 
                int id = Integer.parseInt(element.getAttribute("id")) ; 
                if ( id <= 1 ) continue ;

                
                // Class name
                if( element.getAttribute("parent").compareTo("1") == 0 
                        && !element.getAttribute("style").contains("endArrow") ) {
                        

                    type = 0 ; 
                    // Create JavaClassSource & Set Location and Size
                    classDiagram = createJavaClassSourceAndSetLocation(id, element) ; 

                    classDiagram.getDiagram().setId(element.getAttribute("id"));

                    if ( classDiagram != null ) classes.put(element.getAttribute("id"), classDiagram) ;
                    continue ;

                // Arrow 
                } else if( element.getAttribute("style").contains("endArrow") ) {
                    Edges edges = new Edges() ; 
                    String style = element.getAttribute("style");

                    if( element.getAttribute("source").length() != 0 ) {
                        // set source class diagram id in edges 
                        edges.setSourceClassDiagramId(element.getAttribute("source")) ; 
                    }
                    
                    if( element.getAttribute("target").length() != 0 ) {
                        // set target class diagram id in edges
                        edges.setTargetClassDiagramId(element.getAttribute("target")) ; 
                    }

                    // Identify Arrows check line method
                    // type, source, target point?
                    edges.setArrowType(edges.identifyArrow(style)) ; 
                    
                    setPointsOfEdges(element, edges); 
                    
                    lines.add(edges) ; 
                } 

                // final (all characters is uppercase and split words to _ character)
                
                // Identify attributes and method
                // If the line is met, next attribute is method part. 
                if(element.getAttribute("style").contains("line")) {
                    type = 1 ;
                    continue ; 
                }

                String myId = element.getAttribute("id"); 
                String value = element.getAttribute("value");
                String parentId = element.getAttribute("parent");

                if ( classes.containsKey(parentId) ) {

                    String[] attrs = null;
                   
                    Pattern pattern = Pattern.compile("^([\\+|\\-])\\s(.*):\\s(.*)$"); 
                    Matcher matcher = pattern.matcher(value);

                    if( matcher.find() ) {
                        attrs = new String[ matcher.groupCount() ];
                        for(int j = 1; j <= matcher.groupCount(); j++) {
                            attrs[ j - 1 ] = matcher.group( j );
                        }
                    }

                    if ( attrs != null ) {
                        CoderClassDiagram ccd = classes.get(parentId) ;

                        ccd.getDiagram().getAttributesId().put(myId, parentId);
                        ccd.addFieldAndMethodsInJavaClassSource(attrs, ccd.getJavaClassSource(), type, element); 
                    } 
                }
            }   
        }

        // Add Inheritance & Interface to the source codes 
        for ( Edges edges : lines ) { 

            for ( CoderClassDiagram ccd : classes.values() ) { 
                String sourceId = ccd.getDiagram().getAttributesId().get(edges.getSourceClassDiagramId()) ; 
                String targetId = ccd.getDiagram().getAttributesId().get(edges.getTargetClassDiagramId()) ; 

                if ( sourceId != null ) { 
                    edges.setSourceClassDiagramId(sourceId);
                } 
    
                if ( targetId != null) {
                    edges.setTargetClassDiagramId(targetId);
                }
            }
    
            setInheritanceAndInterface(edges) ; 
        } 

        return true ;
    }

    /**
     * 
     * @param element
     *          Get the source point and target point of edges by parsing element information 

     * @param edges
     *          Set the x, y points which are gained by element to the edges 
     */
    public void setPointsOfEdges(Element element, Edges edges) { 
        if(element.getFirstChild() != null) {
            NodeList GeometryList = element.getChildNodes();
            
            for(int j = 0; j < GeometryList.getLength(); j++) {
                Node cNode = GeometryList.item(j);
               
                if(cNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element cElement = (Element) cNode;
                  
                    if(cElement.getFirstChild() != null) 
                    {
                        NodeList pointList = cElement.getChildNodes();
                     
                        for(int k = 0; k < pointList.getLength(); k++) 
                        {
                            Node pNode = pointList.item(k);
                            if(pNode.getNodeType() == Node.ELEMENT_NODE) 
                            {
                           
                                if(!pNode.getNodeName().equals("mxPoint")) 
                                {
                                    continue;
                                }
                           
                                Element pElement = (Element)pNode;

                                if(pElement.getAttribute("as").equals("sourcePoint")) 
                                {
                                    edges.getSource().setX(Float.parseFloat(pElement.getAttribute("x")));
                                    edges.getSource().setY(Float.parseFloat(pElement.getAttribute("y")));
                                }
                                else if(pElement.getAttribute("as").equals("targetPoint")) 
                                {
                                    edges.getTarget().setX(Float.parseFloat(pElement.getAttribute("x")));
                                    edges.getTarget().setY(Float.parseFloat(pElement.getAttribute("y")));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * @param edges
     *          Find source and target class diagram from edges including points which are the source and target. 
     *          
     *          if arrowType equals to 0 and find the class diagrams which are source and target, 
     *              add the inheritance relationship to the class. 
     *          
     *          if arrowType equals to 1 and find the class diagrams which are source and target, 
     *              add the implementation relationship to the class. 
     */
    public void setInheritanceAndInterface( Edges edges ) { 
        CoderClassDiagram source = null ; 
        CoderClassDiagram target = null ;  

        String sourceId = edges.getSourceClassDiagramId() ; 
        String targetId = edges.getTargetClassDiagramId() ; 

        if ( sourceId != null && targetId != null ) {
            source = classes.get(sourceId) ; 
            target = classes.get(targetId) ; 
        } 

        else {
            if ( targetId != null ) target = classes.get(targetId) ; 
            else if ( sourceId != null ) source = classes.get(sourceId) ;  

            for ( String key : classes.keySet() ) {
                CoderClassDiagram ccd = classes.get(key) ; 
    
                int check = isRange(edges, ccd) ; 
                
                // source 
                if ( check == -1 && source == null) 
                { 
                    source = ccd ; 
                } 
                // target
                else if ( check == 1 && target == null) 
                { 
                    target = ccd ; 
                }
            }
        }
        
        // When finding the class diagrams which are source and target

        if ( source != null && target != null ) { 
            int arrowType = edges.getArrowType() ; 

            // Inheritance 
            if ( arrowType == 0 )
            {
                source.getJavaClassSource().extendSuperType(target.getJavaClassSource()) ; 
            }
            // Implementation
            if ( arrowType == 1 )
            {
                source.getJavaClassSource().addInterface(target.getJavaClassSource().getName()) ; 
            }
        } 
    }   

    

    /**
     * 
     * @param edges
     *          
     * @param ccd 
     *          
     * @return     
     *          
     */     
    public int isRange(Edges edges, CoderClassDiagram ccd) { 

        float sourceX = edges.getSource().getX() ; 
        float sourceY = edges.getSource().getY() ; 

        float targetX = edges.getTarget().getX() ; 
        float targetY = edges.getTarget().getY() ; 
        
        float diagramX = ccd.getDiagram().getPoint().getX() ; 
        float diagramY = ccd.getDiagram().getPoint().getY() ; 

        float diagramWidth = ccd.getDiagram().getWidth() ; 
        float diagramHeight = ccd.getDiagram().getHeight() ; 

        // Check points near the class diagram 

        if ( sourceX >= diagramX - 15 && sourceX <= diagramX + diagramWidth + 15 
            && sourceY >= diagramY - 15 && sourceY <= diagramY + diagramHeight + 15) { 
            return -1; // source 
        }

        if ( targetX >=  diagramX - 15 && targetX <= diagramX + diagramWidth + 15 
            && targetY >= diagramY - 15 && targetY <= diagramY + diagramHeight + 15) { 
            return 1; // target
        }

        return 0; 
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
        boolean success ; 
        try {
            // XML Parsing
            success = setDataFromXML(file) ;

            if ( !success ) { 
                return false ; 
            } 

            for (String id : classes.keySet()) { 
                CoderClassDiagram ccd = classes.get(id) ;
                try {
                    JavaClassSource javaClass = ccd.getJavaClassSource() ; 
                    String className = javaClass.getName() ; 
                    String classPath = String.format("%s/%s.java", javaPath, className); 
                    
                    // Check if any syntax error
                    if(javaClass != null && javaClass.hasSyntaxErrors()){
                        System.err.println("SyntaxError: "+javaClass.getSyntaxErrors());
                        continue ; 
                    }
                    
                    // Make a directory and write the java source code
                    FileUtils.forceMkdir(new File(javaPath));
                    writeStringToFile(new File(classPath), javaClass.toString(), Charset.defaultCharset(), false);
                } catch (IOException e) {

                    System.out.println("[IOException] "); 
                }
            }   

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("IOException: cannot get the data from XML"); 
        } 

        return true; 
    }
}
