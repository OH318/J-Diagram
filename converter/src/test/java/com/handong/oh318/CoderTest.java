package com.handong.oh318;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CoderTest {
    DocumentBuilderFactory dbFactory = null ; 
    DocumentBuilder dBuilder = null ;
    Document doc = null ;  
    Coder coder = null ; 

    private static final double DELTA = 1e-15;

    @Before public void beforeTest() { 
        dbFactory = DocumentBuilderFactory.newInstance(); ; 
        coder = new Coder(); 
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("Cannot build the document!") ;
        }
    }

    public Edges setEdge(float src_x, float src_y, float target_x, float target_y) {
        Edges edges = new Edges();
        
        Point src = new Point();
        Point target = new Point();
        
        src.setX(src_x);
        src.setY(src_y);
        
        target.setX(target_x);
        target.setY(target_y);
        
        edges.setSource(src);
        edges.setTarget(target);
        
        return edges;   
     }
     
     public Diagram setDiagram(float x,float y,float width ,float height) {
        
        Diagram dg = new Diagram();
        
        dg.getPoint().setX(x);
        dg.getPoint().setY(y);
        dg.setHeight(height);
        dg.setWidth(width);
        
        return dg;
     }
 
    @Test
    public void testSetPointsOfEdges() throws IOException {
        
        File file = new File("src/test/resource/arrow.xml") ;

        try {
            doc = dBuilder.parse(file); 
        } catch ( SAXException e) { 
            System.err.println("Cannot parse the XML file!") ; 
        }
 
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("mxCell");    
        
        Node node = nList.item(0); 

        assertEquals(node.getNodeType(), Node.ELEMENT_NODE) ;

        Element element = (Element) node ; 

        String style = element.getAttribute("style") ; 
        assertTrue(style.contains("endArrow")) ; 

        Edges edges = new Edges() ; 
        coder.setPointsOfEdges(element, edges);

        assertEquals(edges.getSource().getX(), 130, DELTA); 
        assertEquals(edges.getSource().getY(), 750, DELTA); 

        assertEquals(edges.getTarget().getX(), 129.5, DELTA); 
        assertEquals(edges.getTarget().getY(), 686, DELTA); 
    }
 
    @Test
    public void testSetInheritanceAndInterface() {

        Edges edges = new Edges() ; 
        edges.setArrowType( 1) ; // interface 

        edges.setSourceClassDiagramId("7");
        edges.setTargetClassDiagramId("11");

        CoderClassDiagram source = new CoderClassDiagram() ; 
        JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);	 
        javaClassSource.setName("sourceClass") ; 
        source.setJavaClassSource(javaClassSource); 
        
        CoderClassDiagram target = new CoderClassDiagram() ; 
        JavaClassSource javaClassSource2 = Roaster.create(JavaClassSource.class);	 
        javaClassSource2.setName("targetClass") ; 
        target.setJavaClassSource(javaClassSource2); 

        coder.getClasses().put("7", source) ; 
        coder.getClasses().put("11", target) ; 

        assertEquals(coder.getClasses().size(), 2);

        coder.setInheritanceAndInterface(edges) ;

        // assertEquals(source.getJavaClassSource().isInterface(), true) ; 
    }
 
    @Test
    public void testIsRange() {
        Coder cd = new Coder();
        CoderClassDiagram ccd = new CoderClassDiagram();
        
        Edges edges1 = setEdge((float)10,(float)20,(float)23.6,(float)510); //(10,20) (23.6,510)
        Edges edges2 = setEdge((float)30,(float)120,(float)23.6,(float)510); //src
        Edges edges3 = setEdge((float)10,(float)20,(float)40,(float)280); //target
        
        Diagram dg = setDiagram((float)10.51,(float)120.0,(float)80,(float)160); 
        
        ccd.setDiagram(dg);
        
        assertEquals(0,cd.isRange(edges1,ccd));
        assertEquals(-1,cd.isRange(edges2, ccd));
        assertEquals(1,cd.isRange(edges3, ccd));
    }
 
    @Test
    public void testCreateSourceCodes() {
        Coder nullCoder = new Coder() ; 
        assertEquals(true, coder.createSourceCodes("src/test/resource/output1","src/test/resource/car.drawio"));
        assertEquals(false, nullCoder.createSourceCodes("src/test/resource/output2", "src/test/resource/empty.drawio"));
    }

    @Test
    public void testMethods() { 
        String[] methods = {"+ Person()", "+ Person(String)", "+ Person(String, int)", "+     setName(String, int) :void", "+      getName():String"} ; 
        
        for (int i = 0 ; i < methods.length; i++) {
            String[] attrs = null;
            
            if ( methods[i].contains(":") ) {
                Pattern pattern = Pattern.compile("^([\\+|\\-])\\s*(.*):\\s*(.*)$"); 
                Matcher matcher = pattern.matcher(methods[i]); 
                
                if( matcher.find() ) {
                    attrs = new String[ matcher.groupCount() ];
                    for(int j = 1; j <= matcher.groupCount(); j++) {
                        attrs[ j - 1 ] = matcher.group( j );
                        System.out.println(attrs[j-1]) ;
                    }
                    assertEquals(attrs.length, 3);
                }
            } else { 
                Pattern pattern = Pattern.compile("^([\\+|\\-])*\\s(.*)"); 
                Matcher matcher = pattern.matcher(methods[i]); 
                
                if( matcher.find() ) {
                    attrs = new String[ matcher.groupCount() ];
                    for(int j = 1; j <= matcher.groupCount(); j++) {
                        attrs[ j - 1 ] = matcher.group( j );
                        System.out.println(attrs[j-1]) ;
                    }
                    assertEquals(attrs.length, 2);
                }
            }
        }
    }
}
