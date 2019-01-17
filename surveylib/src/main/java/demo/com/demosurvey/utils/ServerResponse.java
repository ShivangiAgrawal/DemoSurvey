package demo.com.demosurvey.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

import demo.com.demosurvey.interfaces.IParseListener;

import static android.content.ContentValues.TAG;

public class ServerResponse {

    private RequestQueue requestQueue;

    public void serviceRequestJsonObject(Context mContext, final String url, final JSONObject params,
                                         final IParseListener iParseListener, final int requestCode) {

        Log.d(TAG, "serviceRequestJSonObject: Params...." + params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                iParseListener.SuccessResponse("" + response, requestCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iParseListener.ErrorResponse(error, requestCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return super.getParams();
            }

        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void serviceRequestGet(Context mContext, final String url, StringBuilder params,
                                  final IParseListener iParseListener, final int requestCode) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response is", response);
                        iParseListener.SuccessResponse(response, requestCode);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iParseListener.ErrorResponse(error, requestCode);
                    }
                }) {

        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
