package com.example.androidconcepts.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFetchService extends Service implements WeatherAPIFetch {

    private String location;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        Toast.makeText(this, "Running service", Toast.LENGTH_SHORT).show();
        if (intent != null) {
            String city = intent.getStringExtra("location");
            location = city;
        }
        try {
            getApiData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getApiData() throws IOException {
        final String BASE_URL = "https://api.weatherapi.com/v1/";
        final String API_KEY = "7a077feb1c3e4e4a9fe172123231505";
        final String ANAHEIM_QUERY = location;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPIFetch weatherAPIFetch = retrofit.create(WeatherAPIFetch.class);
        Call<WeatherResponse> call = weatherAPIFetch.getWeather(API_KEY, ANAHEIM_QUERY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    // Access parsed JSON data from the weatherResponse object

                    String temperature = weatherResponse.current.temp_f;

                    Intent broadcastIntent = new Intent("com.example.myapp.SERVICE_DONE");
                    broadcastIntent.putExtra("data", temperature);
                    sendBroadcast(broadcastIntent);

                } else {
                    System.out.println("Error: " + response.code() + " " + response.message());
                    String errorMessage = "Location not found.";
                    Intent broadcastIntent = new Intent("com.example.myapp.SERVICE_DONE");
                    broadcastIntent.putExtra("data", errorMessage);
                    sendBroadcast(broadcastIntent);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static class WeatherResponse {
        CurrentWeatherData current;
    }

    public static class CurrentWeatherData {
        String temp_f;
    }

    @Override
    public Call<WeatherResponse> getWeather(String apiKey, String query) {
        return null;
    }
}