package com.handong.oh318;

import java.util.HashMap;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.w3c.dom.Element;

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
    public void addFieldAndMethodsInJavaClassSource(String[] attr, JavaClassSource javaClassSource, int type, Element element) {
        // String[] paramTypes = null ;
        HashMap<String, String> paramTypes = null ; 
        accessModifierType accessModifier = accessModifierType.Private ; // private: 0, protected: 1, public: 2

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
         *  TODO: 
         *  Constructor 
         *  + Person(age: int)  
         */
       
        String name = attr[1] ; 

        /**
         * Field (=0), Method(=1)
         * Get the name from attr
         */
        if ( type == 0 ) { 
            
            name = name.trim(); 
        } else { 
            // Parsing the type of parameters on the method 
            int paramLength = name.indexOf(")") - name.indexOf("(");

			if(paramLength > 1) 
			{   
				String[] tempParamTypes = name.substring(name.indexOf("(") + 1, name.indexOf(")")).split(",");
                
                // Parameter should be composed to (ParameterName, DataType). 
                // Thus, we should create the instance of paramTypes String HashMap. 

                paramTypes = new HashMap<String,String>() ; 
                String tempName = "p"; 
                for (int i = 0 ; i < tempParamTypes.length; i++) {
                    // param name, param data type
                    paramTypes.put(tempName.concat(Integer.toString(i)), tempParamTypes[i].trim() );
                }
			}
			
            // Remove "()" on the method name for getting an original method name ; 
			name = name.substring(0, name.indexOf("(") );
        }

        /**
         * TODO: 
         *      abstract method
         *      parameter (name: datatype)  
         *      constructor
         */


        /**
         * Attribute
         * DataType [Field, Method] 
         * */ 
        String dataType = null ; 
        boolean constructor = false; 

        if ( attr.length == 2 ) {
            constructor = true ; 
        } else if ( attr.length == 3 ) {
            dataType = attr[2].trim() ; 
        }

        // Field 
        if ( type == 0 ) {
            FieldSource<JavaClassSource> field = null ; 

            String[] typeAndValue = null ; 
            if ( dataType.contains("=")) typeAndValue = dataType.split("="); 

            if ( accessModifier == accessModifierType.Private ) { 
                field = javaClassSource.addField().setName(name).setType(dataType).setPrivate() ;
            // Protected    
            } else if ( accessModifier == accessModifierType.Protected ) {
                field = javaClassSource.addField().setName(name).setType(dataType).setProtected() ;
            // Public 
            } else if ( accessModifier == accessModifierType.Public ) {
                if ( typeAndValue == null ) {
                    field = javaClassSource.addField().setName(name).setType(dataType).setPublic() ;  
                }else {
                    /**
                     *  When the data type includes final and static, and user initializes the value. 
                     *  + DIRECTION: int = 7 => public static final int DIRECTION = 7 ; 
                     */
                    field = javaClassSource.addField().setName(name).setType(typeAndValue[0]).setPublic() ;  
                    field.setLiteralInitializer(typeAndValue[1]) ; 
                }
            } else {
                javaClassSource.addField().setName(name).setType(dataType);
            }

            /**
             *  Static field (fontStyle=4 in style)
             */
            if ( field != null && element.getAttribute("style").contains("fontStyle=4") ) { 
                field.setStatic(true) ; 
            }

            /**
             *  Final (UpperCase)
             */
            boolean finalChecker = true; 
            for (int i = 0 ; i < name.length() ; i++) {
                if ( Character.isLowerCase(name.charAt(i))  ) {
                    finalChecker = false;  
                    break; 
                }
            }

            // final type is all characters is composed to uppercase. 
            if ( field != null && finalChecker )  {
                // static 
                if ( accessModifier == accessModifierType.Public ) field.setStatic(true) ; 
                // non-static 
                field.setFinal(true) ; 
            }
            

        // Method 
        } else if ( type == 1 ) { 

            MethodSource<JavaClassSource> method = null ; 

            if ( constructor ) {
                if ( accessModifier == accessModifierType.Private ) { 
                    method = javaClassSource.addMethod().setName(name).setPrivate().setConstructor(true) ; 
                } else if ( accessModifier == accessModifierType.Protected ) { 
                    method = javaClassSource.addMethod().setName(name).setProtected().setConstructor(true); 
                } else if ( accessModifier == accessModifierType.Public ) { 
                    method = javaClassSource.addMethod().setName(name).setPublic().setConstructor(true); 
                }
            }
            else {
                if ( accessModifier == accessModifierType.Private ) { 
                    method = javaClassSource.addMethod().setName(name).setReturnType(dataType).setPrivate() ; 
                } else if ( accessModifier == accessModifierType.Protected ) { 
                    method = javaClassSource.addMethod().setName(name).setReturnType(dataType).setProtected(); 
                } else if ( accessModifier == accessModifierType.Public ) { 
                    method = javaClassSource.addMethod().setName(name).setReturnType(dataType).setPublic(); 
                }
            }

            if ( paramTypes != null ) { 
                for (String paramName :  paramTypes.keySet() ) {

                    method.addParameter(paramTypes.get(paramName), paramName) ; 
                }
            } 

            /**
             *  Static method (fontStyle=4 in style)
             */
            if ( method != null && element.getAttribute("style").contains("fontStyle=4") ) {
                method.setStatic(true) ; 
            }

            /**
             *  Final (UpperCase)
             */
            boolean finalChecker = true; 
            for (int i = 0 ; i < name.length() ; i++) {
                if ( Character.isLowerCase(name.charAt(i))  ) {
                    finalChecker = false;  
                    break; 
                }
            }

            // All characters is composed to uppercase. 
            if ( method != null && finalChecker )  {
                method.setFinal(true) ; 
            }
        } 
    }
}
