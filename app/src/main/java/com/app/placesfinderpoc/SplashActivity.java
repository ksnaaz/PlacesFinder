package com.app.placesfinderpoc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    Handler navigateHandler=new Handler();
    Runnable navigateRunnable=new Runnable() {
        @Override
        public void run() {
            Intent i=new Intent(SplashActivity.this,MapsActivity.class);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        navigateHandler.postDelayed(navigateRunnable,3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigateHandler.removeCallbacks(navigateRunnable);
    }
}
