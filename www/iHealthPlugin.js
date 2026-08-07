var exec = typeof require === 'function' ? require('cordova/exec') : null;

function isCapacitorPluginAvailable() {
    return typeof Capacitor !== 'undefined' && Capacitor.Plugins && Capacitor.Plugins.IHealthPlugin;
}

function isCordovaAvailable() {
    return typeof cordova !== 'undefined' && exec;
}

function runCordovaAction(action, args, successCallback, errorCallback) {
    if (!isCordovaAvailable()) {
        var error = new Error('Cordova is not available');
        if (errorCallback) {
            errorCallback(error);
            return;
        }
        return Promise.reject(error);
    }

    var callbackWrapper = function (result) {
        if (typeof result === 'number') {
            return result !== 0;
        }
        return result;
    };

    if (successCallback || errorCallback) {
        exec(function (result) {
            if (successCallback) {
                successCallback(callbackWrapper(result));
            }
        }, errorCallback, 'iHealthPlugin', action, args);
        return;
    }

    return new Promise(function (resolve, reject) {
        exec(function (result) {
            resolve(callbackWrapper(result));
        }, reject, 'iHealthPlugin', action, args);
    });
}

function normalizeResult(result) {
    if (result && typeof result === 'object' && result.hasOwnProperty('authorized')) {
        return result.authorized;
    }
    return result;
}

function runCapacitorAction(method, options, successCallback, errorCallback) {
    if (!isCapacitorPluginAvailable()) {
        var error = new Error('Capacitor plugin is not available');
        if (errorCallback) {
            errorCallback(error);
            return;
        }
        return Promise.reject(error);
    }

    var promise = Capacitor.Plugins.IHealthPlugin[method](options || {});
    if (successCallback || errorCallback) {
        promise.then(function (result) {
            if (successCallback) {
                successCallback(normalizeResult(result));
            }
        }).catch(function (err) {
            if (errorCallback) {
                errorCallback(err);
            }
        });
        return;
    }

    return promise.then(normalizeResult);
}

let iHealthPlugin = {
    showToast: function (message, duration, successCallback, errorCallback) {
        if (isCapacitorPluginAvailable()) {
            return runCapacitorAction('showToast', { message: message, duration: duration }, successCallback, errorCallback);
        }
        return runCordovaAction('showToast', [message, duration], successCallback, errorCallback);
    },
    checkAuthorization: function (successCallback, errorCallback) {
        if (isCapacitorPluginAvailable()) {
            return runCapacitorAction('checkAuthorization', null, successCallback, errorCallback);
        }
        return runCordovaAction('checkAuthorization', [], successCallback, errorCallback);
    }
};

module.exports = iHealthPlugin;
 