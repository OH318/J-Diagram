package com.handong.oh318;

import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class Main {
    
    public static void main(String[] args) {
        
        Extractor extractor = new Extractor( "C:\\JAVA\\testCar2.drawio", "C:\\JAVA\\Car") ; 
 //       Extractor extractor = new Extractor() ; 
    	 
        System.out.println("zzzz");
        // HashMap<String, JavaClassSource> classes = 
/*        ArrayList<ClassBox> classes = extractor.getJavaClassSources(".java");
        		
        for (ClassBox jcs : classes)  {
            System.out.println("[Class]: " + jcs.src.getName()) ; 
            
            List<FieldSource<JavaClassSource>> fieldList = jcs.src.getFields() ; 

            System.out.println("[Fields names]") ; 
            for (FieldSource<JavaClassSource> f : fieldList) { 
                System.out.println(f.getVisibility().name() + " " + f.getType() + " " +  f.getName()) ;
            }

            List<MethodSource<JavaClassSource>> methodLists = jcs.src.getMethods() ;
            System.out.println("[Method names]") ; 
            for (MethodSource<JavaClassSource> m : methodLists ){ 
                System.out.println(m.getVisibility().name() + " " + m.getName()) ;
            }
        }   
*/        
        extractor.createDrawio();
        // for (String path : classes.keySet()) {
        //     System.out.println("[FilePath]: " + path) ; 
            
        //     JavaClassSource java = classes.get(path) ; 
            
        //     System.out.println("[Classes]") ;
        //     System.out.println(java.getClass()); 
        // }
    }
}
