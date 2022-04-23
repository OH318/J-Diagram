package com.handong.oh318;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class Generator {
    
    public static void JavaCodeGenerate() {
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
         
        //set the package name, and Java class name
        javaClass.setPackage("demo.JavaCodeGenerator.roaster").setName("PersonPojo");
 
        //add interface, can be one or more     
        javaClass.addInterface(Serializable.class);
         
        //extend an abstract class
        javaClass.extendSuperType(Date.class);
         
        //add a Long/private/static/final Long field, with value '-1L' 
        javaClass.addField()
          .setName("serialVersionUID")
          .setType("long")
          .setLiteralInitializer("-1L")
          .setPrivate().setStatic(true).setFinal(true);
 
        //add a String property, setter/getter functions are generated
        javaClass.addProperty(String.class, "firstName");
        //add an Integer property, final and not exposed
        javaClass.addProperty(Integer.class, "id").setMutable(true).setAccessible(false);
        
        //add a public constructor function, 
        javaClass.addMethod()
          .setConstructor(true)
          .setPublic()
          .setBody("this.id = id;")
          .addParameter(Integer.class, "id");
                 
        //add another private function
        javaClass.addMethod()
        .setName("increaseId")
        .setPrivate()
        .setBody("this.id += step;")
        .addParameter("int", "step");
         
        //check if any syntax error
        if(javaClass.hasSyntaxErrors()){
            System.err.println("SyntaxError: "+javaClass.getSyntaxErrors());
        }
         
        //output to file
        String filePath = "src/main/java/demo/JavaCodeGenerator/roaster/PersonPojo.java";
        try {
            FileUtils.forceMkdir(new File("src/main/java/demo/JavaCodeGenerator/roaster"));
            writeStringToFile(new File(filePath), javaClass.toString(), Charset.defaultCharset(), false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
