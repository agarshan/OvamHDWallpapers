package com.TolKap.agarshan.tamilwallpaper.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.TolKap.agarshan.tamilwallpaper.R;

import com.onesignal.OneSignal;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();





        showNextActivityWithDelay(3000);

    }



    private void showNextActivityWithDelay(int delay) {

        final Timer timer = new Timer();

        TimerTask t = new TimerTask() {
            @Override
            public void run() {


                Intent intent = new Intent(SplashScreenActivity.this,DashboardActivity.class);
                startActivity(intent);
                timer.cancel();

                finish();

            }

        };
        timer.scheduleAtFixedRate(t, delay, 1000);

    }
}
