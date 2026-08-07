package cordova.ihealth.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class iHealthPlugin extends CordovaPlugin {

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("showToast".equals(action)) {
            String message = args.getString(0);
            int duration = args.getInt(1);
            showToast(message, duration);
            callbackContext.success();
            return true;
        }
        return false;
    }

    private void showToast(final String message, final int duration) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(cordova.getActivity(), message, duration).show();
            }
        });
    }
}
