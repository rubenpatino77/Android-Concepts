package com.example.androidconcepts.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPIFetch {
    @GET("current.json")
    Call<ApiFetchService.WeatherResponse> getWeather(
            @Query("key") String apiKey,
            @Query("q") String query
    );
}