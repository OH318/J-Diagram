package com.handong.oh318;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.w3c.dom.Document;

import static org.assertj.core.api.Assertions.*;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;

public class ExtracterTest {

    Extractor extractor = null ;

    @Before public void beforeText() { 
        extractor = new Extractor("src/test/resource/create", "src/test/resource"); 
    }

    @Test
    public void testGetJavaFilepaths() { 
        try {
            ArrayList<Path> paths = (ArrayList<Path>) extractor.getJavaFilepaths(extractor.getDirectoryPath()) ;
            
            assertEquals(paths.size(), 6);
            
            for (int i = 0 ; i < paths.size(); i++) {
                assertThat(paths.get(i).toString()).endsWith(".java") ;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("Cannot find the files") ;
        } 
    }

    @Test
    public void testGetJaveClassSource() { 
        try {
            JavaClassSource jcs = extractor.getJaveClassSource("src/test/resource/create/Teslr.java") ;
            
            assertEquals(jcs.getName(), "Teslr");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("Cannot find the files") ;
        }
    }   

    @Test
    public void testInitXMLFile() { 
        extractor.initXMLfile(); 
        Document document = extractor.getDocument() ; 

        assertNotNull(document);
    }
}

