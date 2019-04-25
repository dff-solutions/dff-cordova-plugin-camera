const fs = require("fs");
const path = require("path");
const xml2js = require("xml2js");

module.exports = {
    read: function () {
        const parser = new xml2js.Parser();
        const data = fs.readFileSync(path.join(__dirname, "..", "plugin.xml"));
        let config;

        parser.parseString(data, (err, result) => {
            if (err) {
                throw err;
            }

            config = result;
        });

        return config;
    }
}