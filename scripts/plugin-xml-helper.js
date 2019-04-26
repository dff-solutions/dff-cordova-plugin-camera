const fs = require("fs");
const path = require("path").posix;
const xml2js = require("xml2js");

function PluginXmlHelper() {
    this.pluginXmlPath = path.join(__dirname, "..", "plugin.xml");
}

PluginXmlHelper.prototype.read = function () {
    const parser = new xml2js.Parser();
    const data = fs.readFileSync(this.pluginXmlPath);
    let config;

    parser.parseString(data, (err, result) => {
        if (err) {
            throw err;
        }

        config = result;
    });

    return config;
};

PluginXmlHelper.prototype.write = function (obj) {
    const builder = new xml2js.Builder();
    const xml = builder.buildObject(obj);

    fs.writeFileSync(this.pluginXmlPath, xml);
};

module.exports = new PluginXmlHelper();
