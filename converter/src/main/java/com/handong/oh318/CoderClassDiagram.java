package com.handong.oh318;

import org.jboss.forge.roaster.model.source.JavaClassSource;

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
    public void addFieldAndMethodsInJavaClassSource(String[] attr, JavaClassSource javaClassSource, int type, TempNameGenerator tempNameGenerator) {
        String[] paramTypes = null ;
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
         * Field, Method
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
                
                // Parameter should be composed to (DataType, ParameterName). 
                // Thus, we should multiple twice to the length of paramTypes String array. 
                paramTypes = new String[tempParamTypes.length * 2] ; 
                for (int i = 0 ; i < tempParamTypes.length; i++) {
                    paramTypes[i] = tempParamTypes[i].trim(); 

                    // TODO: it should be changed to the code as tempNameGenerator.genearateParamName() ;  
                    String tempName = "param"; 
                    paramTypes[i + 1] = tempName.concat(Integer.toString(i)) ;  // tempNameGenerator.genearateParamName() ; 
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

            // TODO: Multiple parameters

            if ( paramTypes != null ) { 
                if ( accessModifier == accessModifierType.Private ) { 
                    javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setPrivate().addParameter("int", "test1") ; 
                } else if ( accessModifier == accessModifierType.Protected ) { 
                    javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setProtected().addParameter("int", "test1") ; 
                } else if ( accessModifier == accessModifierType.Public ) { 
                    javaClassSource.addMethod().setName(attr[1]).setReturnType(attr[2]).setPublic().addParameter("int", "test1") ; 
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
