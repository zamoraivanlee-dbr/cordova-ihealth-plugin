var exec = require('cordova/exec');

let iHealthPlugin = {
    showToast: (message, duration, successCallback, errorCallback) => {
        exec(successCallback, errorCallback, 'iHealthPlugin', 'showToast', [message, duration]);
    }
};

module.exports = iHealthPlugin;
