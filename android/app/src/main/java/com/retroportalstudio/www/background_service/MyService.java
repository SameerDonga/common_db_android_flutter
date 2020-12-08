package com.retroportalstudio.www.background_service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.retroportalstudio.www.background_service.Database.AppDatabase;
import com.retroportalstudio.www.background_service.Database.Objects.Dog;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private AppDatabase mDb;
    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"messages")
                    .setContentText("This is running in Background")
                    .setContentTitle("Flutter Background")
                    .setSmallIcon(R.drawable.ic_android_black_24dp);

            startForeground(101,builder.build());
        }
        //declare database

        //register broadcast
        IntentFilter filter = new IntentFilter();
        filter.addAction("SOME_ACTION");
        filter.addAction("SOME_OTHER_ACTION");
        MyReceiver receiver = new MyReceiver();

        registerReceiver(receiver, filter);
        startJob();

//        sendDataToFlutterDb();

    }

    private void startJob() {
        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                if(mDb==null)
                    mDb = AppDatabase.getInstance(getApplicationContext());

                final List<Dog> persons = mDb.dogDao().loadAllDogs();
                Log.e("Dog count ==>", String.valueOf(persons.size()));

                mDb.dogDao().insertDog(new Dog("Android dog",23));

                Intent intent = new Intent("SOME_ACTION");
                intent.putExtra("dogs",persons.size());
                sendBroadcast(intent);


            }
        };
        timerObj.schedule(timerTaskObj, 0, 5000);
    }

    private void sendDataToFlutterDb() {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
