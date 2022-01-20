package com.example.workoutremind;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.workoutremind.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        NotificationChannel channel = new NotificationChannel("mychannel", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("For Workout Reminders");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);


        setContentView(binding.getRoot());
        MobileAds.initialize(this);

    }
}