package com.example;

import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class JavaCode {
    private String packageName ; 
    private String className; 
    private List<MemberSource<JavaClassSource, ?>> memberVariables; 
    private List<MethodSource<JavaClassSource>> methods;
    private String inheritance ; 
    private List<String> interfaces ; 

    JavaCode(String packageName) {
        this.packageName = packageName ; 

        memberVariables = new ArrayList<>(); 
        methods = new ArrayList<>(); 
        interfaces = new ArrayList<>(); 
    }

    public void setClassName(String className) {
        this.className = className ; 
    }

    public void setMemberVariables(List<MemberSource<JavaClassSource, ?>> list) {
        this.memberVariables = list; 
    }

    public void setMethods(List<MethodSource<JavaClassSource>> list) {
        this.methods = list;
    }

    public void setInheritance(String inheritance) {
        this.inheritance = inheritance ; 
    }
    
    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces ; 
    }

    public void printData() {
        /* Package */
        System.out.println("Package: " + packageName) ; 

        /* ClassName */
        System.out.println("ClassName: " + className) ; 

        /* Member Variables */
        System.out.println("<MemberVariables>"); 

        for (int i = 0 ; i < memberVariables.size(); i++) { 
            System.out.print(memberVariables.get(i)) ; 
        }

        /* Methods */
        System.out.println("<Methods>"); 

        for (int i = 0 ; i < methods.size(); i++) { 
            System.out.print(methods.get(i)) ; 
        }

        /* Inheritance */
        System.out.println("Inheritance: " + inheritance) ; 
        
        /* Interface */
        System.out.print("interfaces: "); 

        for (int i = 0 ; i < interfaces.size(); i++) { 
            if ( i == interfaces.size() - 1 ) { 
                System.out.println(interfaces.get(i)) ; 
            } else  {
                System.out.print(interfaces.get(i) + ",") ;
            }
        }
    }
}
