package com.kevnlan.application.apiagain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnCityID, btnWeatherByName,btnWeatherByID;
    EditText etDataInput;
    ListView lvnWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCityID = findViewById(R.id.btnGetCityID);
        btnWeatherByID = findViewById(R.id.btnGetWeatherByCityID);
        btnWeatherByName = findViewById(R.id.btnGetWeatherByCityName);

        etDataInput = findViewById(R.id.etDataInput);
        lvnWeather= findViewById(R.id.lv_Weather);

        final WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

        btnCityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nothing retuned
                weatherDataService.getCityID(etDataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"An Error Here",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityID) {
                        Toast.makeText(MainActivity.this,"Return an ID of " + cityID,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnWeatherByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDataService.getCityForecastByID(etDataInput.getText().toString(), new WeatherDataService.ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Error Here",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        //Toast.makeText(MainActivity.this, weatherReportModel.toString(),Toast.LENGTH_SHORT).show();

                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModel);
                        lvnWeather.setAdapter(arrayAdapter);
                    }
                });
            }
        });

        btnWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDataService.getCityForecastByName(etDataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallBack() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Error Here",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponce(List<WeatherReportModel> weatherReportModels) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                        lvnWeather.setAdapter(arrayAdapter);
                    }

                });
            }
        });


    }

}


// Request a string response from the provided URL.
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
//                    }
//                });

// Add the request to the RequestQueue.



// Toast.makeText(MainActivity.this,"City ID Button clicked",Toast.LENGTH_SHORT).show();