package cordova.ihealth.plugin;

import android.widget.Toast;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.JSObject;

import java.io.IOException;
import java.io.InputStream;

@CapacitorPlugin(name = "IHealthPlugin")
public class IHealthPlugin extends Plugin {

    @PluginMethod
    public void showToast(final PluginCall call) {
        final String message = call.getString("message");
        final int duration = call.getInt("duration", 0);

        if (message == null) {
            call.reject("Message is required");
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, duration).show();
            }
        });

        call.resolve();
    }

    @PluginMethod
    public void checkAuthorization(PluginCall call) {
        boolean isAuthorized = checkAuthorization();
        JSObject ret = new JSObject();
        ret.put("authorized", isAuthorized);
        call.resolve(ret);
    }

    private boolean checkAuthorization() {
        boolean isAuthorized;

        try (InputStream is = getActivity().getAssets().open("com_guardian_android_patient_dev_android.pem")) {
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
