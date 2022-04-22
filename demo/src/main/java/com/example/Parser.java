package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaUnit;
import org.jboss.forge.roaster.model.source.JavaClassSource;

class Parser { 

    public static String getJavaCode() throws IOException { 
        BufferedReader br = new BufferedReader(
            new FileReader("/Users/jinil/Desktop/Drawio/src/main/java/demo/JavaCodeGenerator/roaster/PersonPojo.java")
        );

        String line ; 
        String javacode = ""; 
        while ( (line = br.readLine()) != null ){ 
            javacode += line ; 
        }   
        br.close(); 

        return javacode;
    }

    public static JavaCode getJavaCodeData() throws IOException{ 
        String javaCode = Parser.getJavaCode() ;

        JavaUnit unit = Roaster.parseUnit(javaCode);
        JavaClassSource myClass = unit.getGoverningType();

        JavaCode data = new JavaCode(myClass.getPackage()) ;
        data.setClassName(myClass.getName());
        data.setMemberVariables(myClass.getMembers());
        data.setMethods(myClass.getMethods());
        data.setInheritance(myClass.getSuperType());
        data.setInterfaces(myClass.getInterfaces());
        
        return data;
    }
}