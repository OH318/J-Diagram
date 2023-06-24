"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const utils_1 = require("../utils");
describe('getDownloadPathByOs 테스트', () => {
    it('플랫폼이 win32 일 때 정상적인 경로를 반환한다.', async () => {
        const path = await (0, utils_1.getDownloadPathByOs)('win32');
        console.log(path);
        // validate if path contains 'oh318.j-diagram'
        expect(path).toContain('oh318.j-diagram');
    });
    it('플랫폼이 darwin 일 때 정상적인 경로를 반환한다.', async () => {
        const path = await (0, utils_1.getDownloadPathByOs)('win32');
        console.log(path);
        // validate if path contains 'oh318.j-diagram'
        expect(path).toContain('oh318.j-diagram');
    });
    it('플랫폼이 linux 일 때 정상적인 경로를 반환한다.', async () => {
        const path = await (0, utils_1.getDownloadPathByOs)('linux');
        console.log(path);
        // validate if path contains 'oh318.j-diagram'
        expect(path).toContain('oh318.j-diagram');
    });
});
//# sourceMappingURL=getDownloadPath.test.js.map