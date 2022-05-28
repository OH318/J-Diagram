# J-Diagram
 
Class Diagram Extractor & Coder for Java

## Overview  
J-Diagram is a Visual Studio Code extension to help Java developers. This provides a convenient conversion between the Java source code and the class diagram. 

## Dependencies
<!-- dependencies, versions, ... -->
* [Draw.io Integration](https://marketplace.visualstudio.com/items?itemName=hediet.vscode-drawio)
* Java SE >= 8

## Supported Environments
* MacOS

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

1. Make a runnable .jar file
   1. Import https://github.com/OH318/J-Diagram.git using the Eclipse.
   2. Right-click on the project.
   3. Select "Export" > "Java" >> "Runnable JAR file" and click Next button.
   4. Finish the process
2. Execute .jar file
   ```bash
   java -jar [runnable .jar path] [java src dir path] [.drawio file path] [0|1]
   # If the last argument is 0, extractor will be executed.
   # If the last argument is 1, coder will be executed. 
   ```
* *We had a classpath-related problem with one of the dependencies when we used Maven build in VSCode. So we chose to create the .jar file in Eclipse as an alternative. As soon as we find a solution, we will change it to using Maven build.*

## Contact
* [kimseoye15@gmail.com](kimseoye15@gmail.com)
* [audwns392@naver.com](audwns392@naver.com)
* [qpalzmm22@gmail.com](qpalzmm22@gmai.com)
* [sam1783@naver.com](sam1783@naver.com)
* [jiniljeil1@naver.com](jiniljeil1@naver.com)

