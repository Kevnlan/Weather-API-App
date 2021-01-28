package com.kevnlan.application.apiagain;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";


    Context context;
    String cityID;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String cityID);
    }

    public void getCityID(String cityName, final VolleyResponseListener volleyResponseListener) {
        String url = QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cityID = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(cityID);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onError("An Error ");
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface ForecastByIDResponse {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModel);
    }

    public void getCityForecastByID(String cityID, final ForecastByIDResponse forecastByIDResponse) {
        final List<WeatherReportModel> weatherReportModels = new ArrayList<>();

        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
        //get JSONobject
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //get JSONarray from object
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

                    //get each item the array and parse it into WeatherReportModel object
                    for (int i = 0; i < consolidated_weather_list.length(); i++) {

                        WeatherReportModel one_day = new WeatherReportModel();
                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);

                        one_day.setId(first_day_from_api.getInt("id"));
                        one_day.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        one_day.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        one_day.setWind_direction_compass(first_day_from_api.getString("wind_direction"));
                        one_day.setCreated(first_day_from_api.getString("created"));
                        one_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        one_day.setMin_temp(first_day_from_api.getLong("min_temp"));
                        one_day.setMax_temp(first_day_from_api.getLong("max_temp"));
                        one_day.setThe_temp(first_day_from_api.getLong("the_temp"));
                        one_day.setWind_speed(first_day_from_api.getLong("wind_direction"));
                        one_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        one_day.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        one_day.setHumidity(first_day_from_api.getInt("humidity"));
                        one_day.setVisibility(first_day_from_api.getLong("visibility"));
                        one_day.setPredictability(first_day_from_api.getInt("predictability"));

                        weatherReportModels.add(one_day);

                        forecastByIDResponse.onResponse(weatherReportModels);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface GetCityForecastByNameCallBack{
        void onError(String message);

        void onResponce(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByName(String cityName, final GetCityForecastByNameCallBack getCityForecastByNameCallBack){
        // fetch city ID given name
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                //city ID not found
            }

            @Override
            public void onResponse(String cityID ) {
                // city ID found
                getCityForecastByID(cityID, new ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        getCityForecastByNameCallBack.onResponce(weatherReportModel);
                    }
                });
            }
        });

        //fetch city forecast
    }
}
