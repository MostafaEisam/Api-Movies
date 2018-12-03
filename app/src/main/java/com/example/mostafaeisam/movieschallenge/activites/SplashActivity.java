package com.example.mostafaeisam.movieschallenge.activites;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.mostafaeisam.movieschallenge.R;
import com.example.mostafaeisam.movieschallenge.utilities.UiManager;

public class SplashActivity extends AppCompatActivity {
    private int mSplah_Time = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                /*
                Intent MainActivity = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(MainActivity);
                finish();
                */
                UiManager.startMainActivityWithoutExtrasOrFlags(SplashActivity.this);
            }
        }, mSplah_Time);
    }

}

