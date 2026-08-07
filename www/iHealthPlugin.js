var exec = require('cordova/exec');

let iHealthPlugin = {
    showToast: (message, duration, successCallback, errorCallback) => {
        exec(successCallback, errorCallback, 'iHealthPlugin', 'showToast', [message, duration]);
    },
    checkAuthorization: (successCallback, errorCallback) => {
        exec(successCallback, errorCallback, 'iHealthPlugin', 'checkAuthorization', []);
    }
};

module.exports = iHealthPlugin;
 