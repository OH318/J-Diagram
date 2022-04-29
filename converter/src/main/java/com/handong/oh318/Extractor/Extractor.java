package com.handong.oh318.Extractor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import org.jboss.forge.roaster.model.source.JavaClassSource;

public class Extractor {
    
    // public HashMap<String, JavaClassSource> getJavaClassSources(String path, String fileExtension) { 
    //     HashMap<String, JavaClassSource> classes = new HashMap<String, JavaClassSource>() ; 

    //     try {
    //         ArrayList<Path> paths = (ArrayList<Path>) Util.getJavaFilepaths(path, fileExtension);

    //         for (Path p : paths) {
    //             classes.put(p.toString(), Util.getJaveClassSource(p.toString())) ; 
    //         }
    //     } catch (IOException e) {
    //         System.err.println("Cannot find the path") ; 
    //     }
    //     return classes; 
    // }   

    public ArrayList<JavaClassSource> getJavaClassSources(String path, String fileExtension) { 
        ArrayList<JavaClassSource> classes = new ArrayList<>() ; 

        try {
            ArrayList<Path> paths = (ArrayList<Path>) Util.getJavaFilepaths(path, fileExtension);

            for (Path p : paths) {
                classes.add(Util.getJaveClassSource(p.toString())) ; 
            }
        } catch (IOException e) {
            System.err.println("Cannot find the path") ; 
        }
        return classes; 
    }   
}
