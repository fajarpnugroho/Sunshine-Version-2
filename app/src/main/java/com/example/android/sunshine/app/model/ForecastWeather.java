package com.example.android.sunshine.app.model;

import java.util.List;

public class ForecastWeather {
    public final List<Weathers> list;

    public ForecastWeather(List<Weathers> list) {
        this.list = list;
    }
}
