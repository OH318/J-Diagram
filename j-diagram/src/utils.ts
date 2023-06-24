import * as path from 'node:path';
import * as fsPromises from 'node:fs/promises';

const extName = 'oh318.j-diagram';
const userProfile = (process.platform === 'win32' ) ? 'USERPROFILE' : 'HOME';
const userProfilePath = process.env[userProfile];
const vscodeExtensionPath = path.join(userProfilePath, '.vscode', 'extensions');

function findExtensionAbsolutePath(files: string[]): string {
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
export async function getDownloadPathByOs(osName: string): Promise<string> {
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
    default: {
      extPath = path.join(userProfilePath, '.vscode', 'extensions');
      const files = await fsPromises.readdir(extPath);
      return findExtensionAbsolutePath(files);
    };
  }
}
