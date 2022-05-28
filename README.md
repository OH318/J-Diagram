# J-Diagram
 
Class Diagram Extractor & Coder for Java

## Overview  
J-Diagram is a Visual Studio Code extension to help Java developers. This provides a convenient conversion between the Java source code and the class diagram. 

## Dependencies
<!-- dependencies, versions, ... -->
* [Draw.io Integration](https://marketplace.visualstudio.com/items?itemName=hediet.vscode-drawio)
* Java SE >= 8

## Getting Started 

### Install J-Diagram to Visual Studio Code

* Find and install J-Diagram<br><br>
  ![image](https://user-images.githubusercontent.com/47961698/170818137-c8f422a7-12e5-4866-be56-54bc500b14a0.png)<br><br>


### Convert Java source codes to class diagram

1. Open command palette ( `cmd` or `alt` +`shift` + `p`).
2. Type `J-diagram : Convert Source to Diagram` to change Java sources into drawio diagram
3. Choose a way you would like to specify a path to Java sources.
4. Specify where to find java sources
5. The path may be a directory path for Java sources or a file path for just one Java file. 
6. Then, decide where you would like drawio file to be created. The file will have name of `j-diagram.drawio`
 
### Extract Java source codes from class diagram

1. Open command palette ( `cmd` or `alt` +`shift` + `p`).
2. Type `J-diagram : Convert Diagram to Sources` to extract Java sources from drawio diagram
3. Choose a way you would like to specify a path to drawio file.
4. Then, locate the drawio file.
5. Decide where you would like Java files to be created.

## Building J-Diagram from Source

1. Clone this repository
    ```bash
    $ git clone https://github.com/OH318/J-Diagram.git
    ```
2. Make a runnable .jar file
   1. Launch the Eclipse.
   2. Select "Import existing Maven project",
   3. Import ```<path to J-Diagram>/J-Diagram/converter```
   4. Right-click on the project.
   5. Select "Export" > "Java" >> "Runnable JAR file" and click Next button.


## Contact
* [kimseoye15@gmail.com](kimseoye15@gmail.com)
* [audwns392@naver.com](audwns392@naver.com)
* [qpalzmm22@gmail.com](qpalzmm22@gmai.com)
* [sam1783@naver.com](sam1783@naver.com)


