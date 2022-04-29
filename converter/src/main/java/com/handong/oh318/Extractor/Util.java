package com.handong.oh318.Extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class Util {
    
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

    public static List<Path> getJavaFilepaths(String directoryPath, String fileExtension) throws IOException {

        Path dirPath = Paths.get(directoryPath) ; 
        
        if (!Files.exists(dirPath)) {
            throw new IOException("Path must be a directory"); 
        }

        List<Path> result = new ArrayList<>(); 
        
        try (Stream<Path> walk = Files.walk(dirPath)) { 
            for (Iterator<Path> iter = walk.iterator() ; iter.hasNext() ; ) {
                Path p = iter.next() ; 

                if (Files.isRegularFile(p) && p.toString().endsWith(fileExtension)) {
                    result.add(p) ;
                } 
            }
        }
       
        return result ; 
    }


}
