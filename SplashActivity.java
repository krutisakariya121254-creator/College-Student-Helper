package com.example.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Load fade-out animation
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // SharedPreferences check
        SharedPreferences prefs =
                getSharedPreferences("StudyBuddyPrefs", MODE_PRIVATE);

        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        // Splash delay (POLISHED: 800 ms)
        findViewById(android.R.id.content).postDelayed(() -> {

            // Start fade animation
            findViewById(android.R.id.content).startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {

                    Intent intent;
                    if (isLoggedIn) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }

                    startActivity(intent);
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

        }, 800); // 👈 700–900 ms sweet spot
    }
}
