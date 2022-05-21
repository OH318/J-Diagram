package com.handong.oh318;

import java.util.HashMap;

import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class CoderClassDiagram {
    
    private Diagram diagram ; 
    private JavaClassSource javaClassSource ; 

    CoderClassDiagram() {
        diagram = new Diagram() ; 
    }

    public void setDiagram(Diagram diagram) { 
        this.diagram = diagram ; 
    }

    public Diagram getDiagram() { 
        return this.diagram ; 
    }

    public void setJavaClassSource(JavaClassSource javaClassSource) { 
        this.javaClassSource = javaClassSource ; 
    }

    public JavaClassSource getJavaClassSource() { 
        return this.javaClassSource  ;
    }

    /**
     * @param attr
     *      field) - direction: int&#10;
     *      method) + sideMirrorTurnOff(): void
     * @param type
     *      ex) 0: field, 1: method
     */
    public void addFieldAndMethodsInJavaClassSource(String[] attr, JavaClassSource javaClassSource, int type) {
        // String[] paramTypes = null ;
        HashMap<String, String> paramTypes = null ; 
        accessModifierType accessModifier = accessModifierType.Private ; // private: 0, protected: 1, public: 2

        if ( attr.length != 3 ) return ; 

        /**
         * Access Modifier 
         * -: private 
         * #: protected
         * +: public 
         */
        if ( attr[0] != null ) {
            if ( attr[0].equals("-")) { 
                accessModifier = accessModifierType.Private ; 
            } else if ( attr[0].equals("#")) { 
                accessModifier = accessModifierType.Protected ; 
            } else if ( attr[0].equals("+")) { 
                accessModifier = accessModifierType.Public ; 
            } else {
                System.err.println(attr[0] + "doesn't exist!") ; 
                return ;
            }
        }

        /**
         * Field (=0), Method(=1)
         * Get the name from attr
         */
        if ( type == 0 ) { 
            // Remove ":" on the field name for getting an original field name. 
            attr[1] = attr[1].substring(0, attr[1].length() - 1 );
        } else { 
            // Parsing the type of parameters on the method 
            int paramLength = attr[1].indexOf(")") - attr[1].indexOf("(");
		
			if(paramLength > 1) 
			{   
                
				String[] tempParamTypes = attr[1].substring(attr[1].indexOf("(") + 1, attr[1].indexOf(")")).split(",");
                
                // Parameter should be composed to (ParameterName, DataType). 
                // Thus, we should create the instance of paramTypes String HashMap. 

                paramTypes = new HashMap<String,String>() ; 
                String tempName = "param"; 
                for (int i = 0 ; i < tempParamTypes.length; i++) {
                    // param name, param data type
                    paramTypes.put(tempName.concat(Integer.toString(i)), tempParamTypes[i].trim() );
                }
			}
			
            // Remove "()" on the method name for getting an original method name ; 
			attr[1] = attr[1].substring(0, attr[1].indexOf("(") );
        }

        /**
         * TODO: 
         *      Check final variable & static
         */


        /**
         * Attribute
         * DataType [Field, Method] 
         * */ 
        attr[2] = attr[2].trim() ; 

        // Field 
        if ( type == 0 ) {

            // Private 
            if ( accessModifier == accessModifierType.Private ) { 
                javaClassSource.addField().setName(attr[1]).setType(attr[2]).setPrivate() ;
            // Protected    
            } else if ( accessModifier == accessModifierType.Protected ) {
                javaClassSource.addField().setName(attr[1]).setType(attr[2]).setProtected() ;
            // Public 
            } else if ( accessModifier == accessModifierType.Public ) {
                javaClassSource.addField().setName(attr[1]).setType(attr[2]).setPublic() ;  
            }
        // Method 
        } else if ( type == 1 ) { 
            // TODO: static 

            if ( paramTypes != null ) { 
                if ( accessModifier == accessModifierType.Private ) { 
                    MethodSource<JavaClassSource> privateMethod = javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setPrivate() ; 
                    
                    if ( paramTypes != null ) {
                        for (String paramName :  paramTypes.keySet() ) {
                            privateMethod.addParameter(paramTypes.get(paramName), paramName) ; 
                        }
                    }
                } else if ( accessModifier == accessModifierType.Protected ) { 
                    MethodSource<JavaClassSource> protectedMethod = javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setProtected(); 
                    
                    if ( paramTypes != null ) { 
                        for (String paramName :  paramTypes.keySet() ) {
                            protectedMethod.addParameter(paramTypes.get(paramName), paramName) ; 
                        } 
                    }
                } else if ( accessModifier == accessModifierType.Public ) { 
                    MethodSource<JavaClassSource> publicMethod = javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setPublic(); 
                    
                    if ( paramTypes != null ) {
                        for (String paramName :  paramTypes.keySet() ) {
                            publicMethod.addParameter(paramTypes.get(paramName), paramName) ; 
                        } 
                    }
                }
            } else { 
                if ( accessModifier == accessModifierType.Private ) { 
                    javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setPrivate() ; 
                } else if ( accessModifier == accessModifierType.Protected ) { 
                    javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setProtected() ; 
                } else if ( accessModifier == accessModifierType.Public ) { 
                    javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setPublic() ; 
                }
            }
        } 
    }
}
