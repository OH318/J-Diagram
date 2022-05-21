package com.handong.oh318.test;
public class Main {
    
    public static void main(String[] args) {
        
        Coder coder = new Coder() ;
        boolean createFile = coder.createSourceCodes("") ;

        if ( createFile ) {
            System.out.println("Create!") ;
        } else {
            System.out.println("Fail!") ; 
        }
    }
}
