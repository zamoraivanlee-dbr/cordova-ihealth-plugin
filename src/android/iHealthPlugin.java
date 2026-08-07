package cordova.ihealth.plugin;

import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class iHealthPlugin extends CordovaPlugin {

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch(action) {
            case "showToast":
                String message = args.getString(0);
                int duration = args.getInt(1);
                showToast(message, duration);
                callbackContext.success();
                return true;
            case "checkAuthorization":
                boolean isAuthorized = checkAuthorization();
                callbackContext.success(isAuthorized ? 1 : 0);
                return true;
            default:
                return false;
        }
    }

    private void showToast(final String message, final int duration) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(cordova.getActivity(), message, duration).show();
            }
        });
    }

    private boolean checkAuthorization() {
        boolean isAuthorized;

        try (InputStream is = cordova.getActivity().getAssets().open("com_guardian_android_patient_dev_android.pem")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            isAuthorized = iHealthDevicesManager.getInstance().sdkAuthWithLicense(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            isAuthorized = false;
        }

        return isAuthorized;
    }
}
