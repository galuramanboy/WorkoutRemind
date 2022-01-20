package com.example.workoutremind;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "mychannel")
                .setContentTitle("Time to workout!")
                .setContentText(intent.getStringExtra("Day"))
                .setSmallIcon(R.drawable.ic_baseline_fitness_center_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
        return super.onStartCommand(intent, flags, startId);
    }
}
