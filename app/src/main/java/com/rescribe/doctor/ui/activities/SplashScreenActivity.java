package com.rescribe.doctor.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rescribe.doctor.R;
import com.rescribe.doctor.network.RequestPool;
import com.rescribe.doctor.notification.MQTTServiceAlarmTask;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class SplashScreenActivity extends AppCompatActivity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = SplashScreenActivity.this;

        MQTTServiceAlarmTask.cancelAlarm(mContext);
        new MQTTServiceAlarmTask(mContext).run();

        doNext();

    }

    /*private void callApi() {
        StringRequest stringRequest = new StringRequest(GET, "https://drrescribe.com/medsonit-be/Masterdata/getAllDoctorSpecialities",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Responce", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Responce", "error");
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerParams = new HashMap<>();
                headerParams.put("User-ID", "2600");
                headerParams.put("Auth-Key", "simplerestapi");
                headerParams.put("Location-ID", "65");
                headerParams.put("Client-Service", "frontend-client");
                headerParams.put("Parent-ID", "2600");
                headerParams.put("Authorization-Token", "$1$DowDISK4$T/hjDf0/myRL.SnvTuLdf0");
                headerParams.put("Hospital-ID", "77");
                return headerParams;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("BackUpRequest");
        RequestPool.getInstance(this).addToRequestQueue(stringRequest);
    }*/

    private void doNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, mContext).equals(RescribeConstants.YES)) {
                    Intent intentObj = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                    startActivity(intentObj);
                } else {
                    Intent intentObj = new Intent(SplashScreenActivity.this, LoginSignUpActivity.class);
                    startActivity(intentObj);
                }
                finish();
            }
        }, RescribeConstants.TIME_STAMPS.THREE_SECONDS);
    }
}
