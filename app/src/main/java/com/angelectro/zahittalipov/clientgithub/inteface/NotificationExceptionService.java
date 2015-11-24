package com.angelectro.zahittalipov.clientgithub.inteface;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.ServiceAction;

/**
 * Created by Zahit Talipov on 20.11.2015.
 */
@EService
public class NotificationExceptionService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
