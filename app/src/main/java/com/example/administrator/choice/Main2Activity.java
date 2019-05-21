package com.example.administrator.choice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {
    ServiceConnection conn ;
    MyService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                mService = ((MyService.MyBinder) service).getServer();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService=null;
            }
        };

        Intent intent = new Intent(this,MyService.class);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
        //unbindService(conn);
    }
}
