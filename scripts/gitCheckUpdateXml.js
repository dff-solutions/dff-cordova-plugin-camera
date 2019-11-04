const path = require("path");
const process = require("process");
const child_process = require("child_process");

const pluginXmlName = "plugin.xml";
const pluginXmlPath = path.join(__dirname, "..", pluginXmlName);
const cmd = `git diff --quiet --exit-code ${pluginXmlPath}`;

console.info(`check if ${pluginXmlPath} has unstaged changes: ${cmd}`);

try {
    child_process.execSync(cmd);
}
catch (reason) {
    const rc = reason.status;
    console.error(`${pluginXmlPath} has unstaged changes. rc: ${rc}`);
    process.exit(rc);
}
