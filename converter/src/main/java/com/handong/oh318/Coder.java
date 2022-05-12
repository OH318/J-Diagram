package com.handong.oh318 ;

import java.util.ArrayList;

import org.jboss.forge.roaster.model.source.JavaClassSource;

class Coder implements Parse{
    private TempNameGenerator nameGenerator ; 
    private ArrayList<JavaClassSource> classes ; 

    @Override
    public ArrayList<JavaClassSource> getJavaClassSources(String path)  {
        // TODO 
        return null ;
    }

    public boolean createSourceCodes(String path) { 
        // TODO 

        return false ; 
    }
}
