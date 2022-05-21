package com.handong.oh318;

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

public class Extractor extends UserInput implements Parse{

    private ArrayList<JavaClassSource> classes; 
    private ArrayList<String> paths; 
    // private Lines lines ; (convert ArrayList<Edges> )
    private PointGenerator pointGenerator ;

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

    
    @Override
    public ArrayList<JavaClassSource> getJavaClassSources(String path) { 
        ArrayList<JavaClassSource> classes = new ArrayList<>() ; 

        try {
            ArrayList<Path> paths = (ArrayList<Path>) getJavaFilepaths(path);

            for (Path p : paths) {
                classes.add(getJaveClassSource(p.toString())) ; 
            }
        } catch (IOException e) {
            System.err.println("Cannot find the path") ; 
        }
        return classes; 
    }   

    public boolean createDrawio(String path) {
        // TODO 
        return false; 
    }
}
