package com.example.sylviatan.jsonvolley;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declare view objects
    private TextView textView;
    private ImageView imageView;
    private Button button;
    private RequestQueue requestQueue;
    private String url, imageUrl = "";
    private static final String TAG = "REQUESTQUEUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize view objects tie to UI
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.buttonRequest);
        button.setOnClickListener(this);

        //create an instance of RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        //url string
        url = "https://api.myjson.com/bins/g9mpu";
        imageUrl = "https://lipeng.000webhostapp.com/images/larva.png";
    }

    @Override
    public void onClick(View v) {

        //create an ImageRequest
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                imageView.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        //create StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Due to response is of JSON object type

                JSONObject jobj= null;
                try {
                    jobj = new JSONObject(response);
                    String c = jobj.getString("user");
                    JSONArray jArray = new JSONArray(c);
                    for (int i = 0; i < jArray.length() ; i++) {
                        String id =jArray.getJSONObject(i).getString("id");
                        String name =jArray.getJSONObject(i).getString("name");
                        String age =jArray.getJSONObject(i).getString("age");
                        String married =jArray.getJSONObject(i).getString("married");
                        textView.append(id + name + age + married + "\n\n");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setTag(TAG);
        imageRequest.setTag(TAG);
        requestQueue.add(stringRequest);
        requestQueue.add(imageRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null)
        requestQueue.cancelAll(TAG);
    }
}
