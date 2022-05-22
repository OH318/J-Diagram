package com.handong.oh318;

import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class Main {
    
    public static void main(String[] args) {
        
        Extractor extractor = new Extractor( "C:\\JAVA\\testCar3.drawio", "C:\\JAVA\\Car") ; 
 
        extractor.createDrawio();
    }
}
