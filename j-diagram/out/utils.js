"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getDownloadPathByOs = void 0;
const path = require("node:path");
const fsPromises = require("node:fs/promises");
const extName = 'oh318.j-diagram';
const userProfile = (process.platform === 'win32') ? 'USERPROFILE' : 'HOME';
const userProfilePath = process.env[userProfile];
const vscodeExtensionPath = path.join(userProfilePath, '.vscode', 'extensions');
function findExtensionAbsolutePath(files) {
    let result = '';
    files.forEach(file => {
        if (file.startsWith(extName)) {
            result = path.join(vscodeExtensionPath, file);
        }
    });
    return result;
}
/**
 * @param osName OS Platform name
 * @returns return extension absolute path ( automatically reconcile version path )
 */
async function getDownloadPathByOs(osName) {
    let extPath = '';
    switch (osName) {
        case 'win32': {
            extPath = path.join(userProfilePath, '.vscode', 'extensions');
            const files = await fsPromises.readdir(extPath);
            return findExtensionAbsolutePath(files);
        }
        case 'win64': {
            extPath = path.join(userProfilePath, '.vscode', 'extensions');
            const files = await fsPromises.readdir(extPath);
            return findExtensionAbsolutePath(files);
        }
        case 'darwin': {
            extPath = path.join(userProfilePath, '.vscode', 'extensions');
            const files = await fsPromises.readdir(extPath);
            return findExtensionAbsolutePath(files);
        }
        default:
            {
                extPath = path.join(userProfilePath, '.vscode', 'extensions');
                const files = await fsPromises.readdir(extPath);
                return findExtensionAbsolutePath(files);
            }
            ;
    }
}
exports.getDownloadPathByOs = getDownloadPathByOs;
//# sourceMappingURL=utils.js.map