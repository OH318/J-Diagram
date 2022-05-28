"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.deactivate = exports.activate = void 0;
// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require("vscode");
const vscode_1 = require("vscode");
// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
function activate(context) {
    // Use the console to output diagnostic information (console.log) and errors (console.error)
    // This line of code will only be executed once when your extension is activated
    console.log('Congratulations, your extension "J-Diagram" is now active!!!');
    var downloadCommand = "mkdir -p /tmp && wget https://github.com/OH318/J-Diagram/releases/download/v.0.0.1/J-d.jar -O /tmp/J-d.jar";
    const { exec } = require('child_process');
    exec(downloadCommand, (err, stdout, stderr) => {
        if (err) {
            console.log(`1]err : ${err.message}`);
        }
        console.log(`1]stderr: ${stderr}`);
        console.log(`1]stdout: ${stdout}`);
    });
    // The command has been defined in the package.json file
    // Now provide the implementation of the command with registerCommand
    // The commandId parameter must match the command field in package.json
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
            ;
        }
        else if (selectDrawioFiles === 'Manually Write Path') {
            drawioPath = await vscode_1.window.showInputBox({
                placeHolder: 'Write a path to drawio file that you like to covert to Java code.',
            });
        }
        //console.log(' -- DONE Selected file: ' + drawioPath);
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
        //var downloadCommand = "wget https://github.com/qpalzmm22/testRepo/blob/main/J-d.jar";
        //const { exec } = require('child_process');
        // var downloadCommand = "wget https://github.com/qpalzmm22/testRepo/releases/download/0.0.2/J-d.jar -O /tmp/J-d.jar";
        // await exec(downloadCommand, (err: any, stdout: any, stderr: any) => {
        // 	if(err) {
        // 		console.log(`1]err : ${err.message}`);
        // 	}
        // 	console.log(`1]stderr: ${stderr}`);
        // 	console.log(`1]stdout: ${stdout}`);	
        // });
        var command = "java -jar /tmp/J-d.jar " + javaPath + " " + drawioPath + " " + "1"; // 1 for Coder
        console.log('command : ' + command);
        // Delay for download
        //const { exec } = require('child_process');
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
        //console.log(' -- DONE Selected file: ' + javaPath);
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
        // var downloadCommand = "wget https://github.com/qpalzmm22/testRepo/releases/download/0.0.2/J-d.jar -O /tmp/J-d.jar";
        // const { exec } = require('child_process');
        // await exec(downloadCommand, (err: any, stdout: any, stderr: any) => {
        // 	if(err) {
        // 		console.log(`1]err : ${err.message}`);
        // 	}
        // 	console.log(`1]stderr: ${stderr}`);
        // 	console.log(`1]stdout: ${stdout}`);	
        // });
        var command = "java -jar /tmp/J-d.jar " + javaPath + " " + drawioPath + "/j-diagram.drawio " + "0"; // 0 for Extractor
        console.log('command : ' + command);
        //const { exec } = require('child_process');
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