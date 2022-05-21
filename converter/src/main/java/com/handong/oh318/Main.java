package com.handong.oh318;

public class Main {
    
    public static void main(String[] args) {
        String javaPath = args[0] ; 
        String drawioPath = args[1] ; 

        System.out.println("Java Path: " + javaPath);
        System.out.println("DrawioPath: " + drawioPath);  

        // Extractor
        if ( args[2].equals("0") ) {


        // Coder
        } else if ( args[2].equals("1")) { 
            Coder coder = new Coder() ; 

            boolean success = coder.createSourceCodes(javaPath, drawioPath) ; 

            if ( success ) { 
                System.out.println("Create Success") ; 
            } else { 
                System.out.println("Cannot create java source codes") ;
            }
        }
    }
}
