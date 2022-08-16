package com.example.jsonextract;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button btn_get_weather;
    EditText et_city_name;
    TextView tv_result;
    ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_get_weather = (Button) findViewById(R.id.getWeather);
        et_city_name = (EditText) findViewById(R.id.etCityName);
        tv_result = (TextView) findViewById(R.id.tv_result);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);


        btn_get_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(et_city_name.getText())) {

                    et_city_name.setError("City name is required!");

                } else {

                    String city = et_city_name.getText().toString();

                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=7cf490200d964017dcc395263040a938&units=metric";


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String final_result = "";
                                String weatherInfo = response.getString("weather");
                                JSONArray arr = new JSONArray(weatherInfo);
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject JsonPart = arr.getJSONObject(i);
                                    final_result += JsonPart.getString("description") + "\n";


                                    Glide.with(MainActivity.this).load("http://openweathermap.org/img/w/" + JsonPart.getString("icon") + ".png").into(iv_icon);


                                }
                                JSONObject main = response.getJSONObject("main");
                                String temp = "Temperature: " + main.getString("temp") + "°C";

                                final_result += temp;
                                tv_result.setText(final_result);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(MainActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();

                        }
                    });
                    queue.add(jsonObjectRequest);


                }
            }
        });


    }


}