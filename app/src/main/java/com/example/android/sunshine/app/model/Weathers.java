package com.example.android.sunshine.app.model;

import java.util.List;

public final class Weathers {
    public final int dt;
    public final Temp temp;
    public final List<Weather> weather;

    public Weathers(int dt, Temp temp, List<Weather> weather) {
        this.dt = dt;
        this.temp = temp;
        this.weather = weather;
    }
}
