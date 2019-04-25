const readPluginXml = require("./readPluginXml");

const pluginXml = readPluginXml.read();

console.log("##teamcity[enableServiceMessages]");
console.log(`##teamcity[setParameter name='PluginId' value='${pluginXml.plugin.$.id}']`);
console.log(`##teamcity[setParameter name='PluginVersion' value='${pluginXml.plugin.$.version}']`);
console.log(`##teamcity[setParameter name='PluginName' value='${pluginXml.plugin.$.name}']`);
console.log("##teamcity[disableServiceMessages]");