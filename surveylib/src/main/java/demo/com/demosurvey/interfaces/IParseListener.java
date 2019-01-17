package demo.com.demosurvey.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by USER on 18-09-2017.
 */

public interface IParseListener {

    void ErrorResponse(VolleyError error, int requestCode);

    void SuccessResponse(String response, int requestCode);
}


