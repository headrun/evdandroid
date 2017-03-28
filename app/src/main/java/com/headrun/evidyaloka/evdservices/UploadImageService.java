package com.headrun.evidyaloka.evdservices;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.config.ApiEndpoints;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.core.VolleyMultipartRequest;
import com.headrun.evidyaloka.dto.ChangeSessionStatus;
import com.headrun.evidyaloka.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadImageService extends Service {

    public String TAG = UploadImageService.class.getSimpleName();
    Utils utils;
    private String uri = "uri", file_uri;
    Bitmap bitmap;
    String imageString;
    byte[] imageBytes;

    public UploadImageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new Utils(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Log.i(TAG, "resonse is change service");

            if (intent != null) {
                if (intent != null) {

                    file_uri = intent.getStringExtra(uri);
                    if (!file_uri.isEmpty()) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(file_uri));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            imageBytes = baos.toByteArray();
                            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            ServiceCall();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return START_STICKY;
    }


    private void ServiceCall() {

        try {
            if (imageString != null && !imageString.isEmpty())
                saveProfileAccount();

        } catch (Exception e) {
            e.printStackTrace();
            onDestroy();
        }
    }


    private void saveProfileAccount() {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiEndpoints.UPLOAD_FILE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                if (resultResponse != null && !resultResponse.isEmpty() && resultResponse.toLowerCase().equals("ok"))
                    Toast.makeText(getApplicationContext(), "profile upload Sucessfull", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("platform", "android");
                params.put("csrfmiddlewaretoken", new Utils(getApplicationContext()).userSession.getCsrf());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if (imageBytes != null)
                    params.put("file", new DataPart("file_avatar.jpg", imageBytes, "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                return EVDNetowrkServices.getSessionHeaders(getApplicationContext());
            }
        };

        EvdApplication.getInstance().addToRequestQueue(multipartRequest);
    }
}
