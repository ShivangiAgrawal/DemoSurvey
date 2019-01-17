package demo.com.demosurvey.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by USER on 18-09-2017.
 */

public class StaticUtils {

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager _connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean _isConnected = false;
        NetworkInfo _activeNetwork = _connManager.getActiveNetworkInfo();
        if (_activeNetwork != null) {
            _isConnected = _activeNetwork.isConnectedOrConnecting();
        }
        return _isConnected;
    }
}