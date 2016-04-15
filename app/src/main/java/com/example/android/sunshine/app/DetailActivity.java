package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

public class DetailActivity extends ActionBarActivity implements DetailActivityFragment.Controller {

    public String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            text = bundle.getString(Intent.EXTRA_TEXT);
        }
    }

    @Override
    public String getTextExtra() {
        return text;
    }
}
