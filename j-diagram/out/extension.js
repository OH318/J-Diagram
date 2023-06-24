"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.deactivate = exports.activate = void 0;
// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require("vscode");
const vscode_1 = require("vscode");
const utils_1 = require("./utils");
// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
async function activate(context) {
    // Use the console to output diagnostic information (console.log) and errors (console.error)
    // This line of code will only be executed once when your extension is activated
    console.log('Congratulations, your extension "J-Diagram" is now active!!!');
    console.log('Checking the OS...!');
    let osName = "Unknown";
    let downloadPath = "/tmp";
    osName = process.platform;
    downloadPath = await (0, utils_1.getDownloadPathByOs)(osName);
    var JdjarPath = "";
    if (osName === "win32") {
        JdjarPath = downloadPath + "\\J-d.jar";
    }
    else {
        JdjarPath = downloadPath + "/J-d.jar";
    }
    const { exec } = require('child_process');
    context.subscriptions.push(vscode_1.commands.registerCommand('j-diagram.Diagram_to_Source', async () => {
        const selectDrawioFiles = await vscode_1.window.showQuickPick(['Open File Finder', 'Manually Write Path'], {
            placeHolder: 'Choose a drawio file that you like to covert to Java code.',
        });
        var drawioPath = undefined;
        if (selectDrawioFiles === 'Open File Finder') {
            const options = {
                canSelectMany: false,
                openLabel: 'Select',
                canSelectFiles: true,
                canSelectFolders: false
            };
            await vscode.window.showOpenDialog(options).then(fileUri => {
                if (fileUri && fileUri[0]) {
                    console.log('Selected file: ' + fileUri[0].fsPath);
                    drawioPath = fileUri[0].fsPath;
                }
            });
        }
        else if (selectDrawioFiles === 'Manually Write Path') {
            drawioPath = await vscode_1.window.showInputBox({
                placeHolder: 'Write a path to drawio file that you like to covert to Java code.',
            });
        }
        const selectDestiniationPath = await vscode_1.window.showQuickPick(['Open File Finder', 'Manually Write Path', 'Current Directoty'], {
            placeHolder: 'Choose a directory you would like JAVA Sources to be created.',
        });
        var javaPath;
        if (selectDestiniationPath === 'Open File Finder') {
            const options = {
                canSelectMany: false,
                openLabel: 'Select',
                canSelectFiles: false,
                canSelectFolders: true
            };
            await vscode.window.showOpenDialog(options).then(fileUri => {
                if (fileUri && fileUri[0]) {
                    console.log('Selected file: ' + fileUri[0].fsPath);
                    javaPath = fileUri[0].fsPath;
                }
            });
        }
        else if (selectDestiniationPath === 'Manually Write Path') {
            javaPath = await vscode_1.window.showInputBox({
                placeHolder: 'Choose a directory you would like JAVA Sources to be created.',
            });
        }
        else if (selectDestiniationPath === 'Current Directoty') {
            javaPath = './';
        }
        var command = "java -jar \"" + JdjarPath + "\" \"" + javaPath + "\" \"" + drawioPath + "\" " + "1"; // 1 for Coder
        console.log('command : ' + command);
        exec(command, (err, stdout, stderr) => {
            if (err) {
                console.log(`err : ${err.message}`);
            }
            console.log(`stderr: ${stderr}`);
            console.log(`stdout: ${stdout}`);
        });
    }));
    context.subscriptions.push(vscode_1.commands.registerCommand('j-diagram.Source_to_Diagram', async () => {
        const selectJavaFiles = await vscode_1.window.showQuickPick(['Open File Finder', 'Manually Write Path'], {
            placeHolder: 'Choose JAVA Sources or directory containing them',
        });
        var javaPath = undefined;
        if (selectJavaFiles === 'Open File Finder') {
            const options = {
                canSelectMany: false,
                openLabel: 'Select',
                canSelectFiles: true,
                canSelectFolders: true
            };
            await vscode.window.showOpenDialog(options).then(fileUri => {
                if (fileUri && fileUri[0]) {
                    console.log('Selected file: ' + fileUri[0].fsPath);
                    javaPath = fileUri[0].fsPath;
                }
            });
            ;
        }
        else if (selectJavaFiles === 'Manually Write Path') {
            javaPath = await vscode_1.window.showInputBox({
                placeHolder: 'Path to JAVA source file or directory containing JAVA source files',
            });
        }
        const selectDestiniationPath = await vscode_1.window.showQuickPick(['Open File Finder', 'Manually Write Path', 'Current Directoty'], {
            placeHolder: 'Choose where to make Drawio diagram',
        });
        var drawioPath;
        if (selectDestiniationPath === 'Open File Finder') {
            const options = {
                canSelectMany: false,
                openLabel: 'Select',
                canSelectFiles: false,
                canSelectFolders: true
            };
            await vscode.window.showOpenDialog(options).then(fileUri => {
                if (fileUri && fileUri[0]) {
                    console.log('Selected file: ' + fileUri[0].fsPath);
                    drawioPath = fileUri[0].fsPath;
                }
            });
        }
        else if (selectDestiniationPath === 'Manually Write Path') {
            drawioPath = await vscode_1.window.showInputBox({
                placeHolder: 'Path to JAVA source file or directory containing JAVA source files',
            });
        }
        else if (selectDestiniationPath === 'Current Directoty') {
            drawioPath = './';
        }
        var command = "java -jar \"" + JdjarPath + "\" \"" + javaPath + "\" \"" + drawioPath + "/j-diagram.drawio\" " + "0"; // 0 for Extractor
        console.log('command : ' + command);
        await exec(command, (err, stdout, stderr) => {
            if (err) {
                console.log(`err : ${err.message}`);
            }
            console.log(`stderr: ${stderr}`);
            console.log(`stdout: ${stdout}`);
        });
    }));
}
exports.activate = activate;
// this method is called when your extension is deactivated
function deactivate() { }
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map