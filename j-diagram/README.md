# J_Diagram

J-Diagram is a Visual Studio Code extension to help Java developers. This provides a convenient conversion between the Java source code and the class diagram. 

## Features

`J-Diagram` supports 2 major features
1. Extract `drawio diagram` from `Java sources` 
2. Create `Java sources` from `drawio diagram` 

### How to Use J-Diagram

- **Extract drawio diagram from Java source file.**
    1. Open command palette ( `cmd` or `alt` +`shift` + `p`).
    2. Type `J-diagram : Convert Source to Diagram` to change Java sources into drawio diagram
    3. Choose a way you would like to specify a path to Java sources.
    4. Specify where to find java sources
    5. The path may be a directory path for Java sources or a file path for just one Java file.
    6. Then, decide where you would like drawio file to be created. The file will have name of `j-diagram.drawio`

---

![Demo Extractor](https://github.com/OH318/J-Diagram/blob/main/j-diagram/images/Extractor_Demo.gif?raw=true)
 
--- 
- **Create Java sources from drawio diagram.**
    1. Open command palette ( `cmd` or `alt` +`shift` + `p`).
    2. Type `J-diagram : Convert Diagram to Sources` to extract Java sources from drawio diagram.
    3. Choose a way you would like to specify a path to drawio file.
    4. Then, locate the drawiofile.
    5. Decide where you would like Java files to be created.

---

![Demo Coder](https://github.com/OH318/J-Diagram/blob/main/j-diagram/images/Coder_demo.gif?raw=true)

---

## Requirements

- **[Drawio Extension](https://marketplace.visualstudio.com/items?itemName=hediet.vscode-drawio)** must be installed.

## Known Issues

- Some operating systems that doensn't have `/tmp` directory can't download j-d.jar 

## Release Notes

This is the first release of the extension. This is to test if we could successfully upload 

### 0.0.6


Initial release of J-Diagram
s