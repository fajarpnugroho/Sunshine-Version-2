package com.example.android.sunshine.app.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;

import com.example.android.sunshine.app.BuildConfig;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.model.ForecastWeather;
import com.example.android.sunshine.app.model.Weathers;
import com.example.android.sunshine.app.services.ForecastService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private Context context;
    private Retrofit retrofit;
    private Callback callback;

    public RestApi(Context context, Callback callback) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.callback = callback;
        this.context = context;
    }

    public ForecastService getForecastService() {
        return retrofit.create(ForecastService.class);
    }

    public void getForecastForWeek(String location) {
        Call<ForecastWeather> call = getForecastService().getForecastDaily(location,
                BuildConfig.OPEN_WEATHER_MAP_API_KEY);

        call.enqueue(new retrofit2.Callback<ForecastWeather>() {
            @Override
            public void onResponse(Call<ForecastWeather> call, Response<ForecastWeather> response) {
                if (response.isSuccessful()) {
                    callback.setupContent(parseModelResponse(response.body()));
                } else {
                    callback.showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<ForecastWeather> call, Throwable t) {
                callback.showErrorMessage();
            }
        });
    }

    private List<String> parseModelResponse(ForecastWeather forecastWeather) {
        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitType = preferences.getString(context.getString(R.string.pref_unit_key),
                context.getString(R.string.pref_unit_metric));

        List<String> arrayOfString = new ArrayList<>();

        int i =0;
        for (Weathers weathers : forecastWeather.list) {
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            String day = getReadableDateString(dateTime);

            String weather = weathers.weather.get(0).description;
            String maxmin = formatHighLows(weathers.temp.max, weathers.temp.min, unitType);

            arrayOfString.add(day + " - " + weather + " - " + maxmin);

            i++;
        }
        return arrayOfString;
    }

    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    private String formatHighLows(double high, double low, String unitType) {

        if (unitType.equalsIgnoreCase(context.getString(R.string.pref_unit_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        return roundedHigh + "/" + roundedLow;
    }

    public interface Callback {
        void setupContent(List<String> data);

        void showErrorMessage();
    }
}
