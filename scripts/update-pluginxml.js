const fs = require("fs");
const path = require("path").posix;
const pluginXmlHelper = require("./plugin-xml-helper");

const projectDir = path.join(__dirname, "..");
const pluginDir = path.join(projectDir, "plugin")
const androidSrcJavaDir = path.join(
    pluginDir,
    "src",
    "main",
    "java"
);
const androidSrcResDir = path.join(pluginDir, "src", "main", "res");
const androidDestJavaDir = "src";
const androidDestResDir = "res";

console.log(androidSrcJavaDir);

const androidSrcJavaFiles = readDir(androidSrcJavaDir)
    .sort()
    .map(file =>
        createSourceFileElement(file, androidSrcJavaDir, androidDestJavaDir)
    );

const androidSrcResFiles = readDir(androidSrcResDir)
    .filter((file) => !file.endsWith("strings.xml"))
    .sort()
    .map(file =>
        createSourceFileElement(file, androidSrcResDir, androidDestResDir)
    );

const pluginXmlData = pluginXmlHelper.read();

const xmlPlatformAndroid = pluginXmlData.plugin.platform
    .filter(p => p.$.name == "android")
    .pop();
const oldSourceFiles = xmlPlatformAndroid["source-file"];

// keep system libs
const sourceLibs = oldSourceFiles.filter(oldFile =>
    oldFile.$.src.endsWith(".jar")
);

xmlPlatformAndroid["source-file"] = [
    ...androidSrcJavaFiles,
    ...androidSrcResFiles,
    ...sourceLibs
];

pluginXmlHelper.write(pluginXmlData);

function createSourceFileElement(file, srcDir, destDir) {
    const src = path.relative(projectDir, file);
    const destFile = path.join(destDir, path.relative(srcDir, file));
    const targetDir = path.dirname(destFile);

    return {
        $: {
            src,
            "target-dir": targetDir
        }
    };
}

function readDir(dir) {
    let result = [];

    if (!fs.existsSync(dir)) {
        throw new Error(`directory not found ${dir}`);
    }

    fs.readdirSync(dir).forEach(file => {
        const filePath = path.join(dir, file);
        const stat = fs.lstatSync(filePath);

        if (stat.isDirectory()) {
            result = [...result, ...readDir(filePath)];
        } else if (stat.isFile()) {
            result.push(filePath);
        }
    });

    return result;
}
