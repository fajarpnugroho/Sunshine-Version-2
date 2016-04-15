package com.example.android.sunshine.app.services;

import com.example.android.sunshine.app.model.ForecastWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastService {
    @GET("forecast/daily?mode=json&units=metric&cnt=7")
    Call<ForecastWeather> getForecastDaily(@Query("q") String q,
                                           @Query("APPID") String appid);
}
