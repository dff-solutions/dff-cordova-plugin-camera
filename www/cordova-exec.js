const exec = require('cordova/exec');

function CordovaExec(feature, actions) {
    this.feature = feature;

    this.createActions(actions);
}

CordovaExec.prototype.createActionFunction = function (action) {
    var self = this;

    return function (success, error, args) {
        exec(success, error, self.feature, action, [ args || {} ]);
    }
};

CordovaExec.prototype.createActions = function (actions) {
    var self = this;

    if (Array.isArray(actions)) {
        actions
            .forEach(function (action) {
                CordovaExec.prototype[action] = self.createActionFunction(action);
            });
    }
};

module.exports = function (feature, actions) {
    return new CordovaExec(feature, actions);
};
