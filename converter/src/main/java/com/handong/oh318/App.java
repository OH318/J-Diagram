package com.handong.oh318;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException
    {   
        // Generating Java Code
        Generator.JavaCodeGenerate(); 

        JavaCode java = Parser.getJavaCodeData(); 
        
        java.printData();
    }   
}
