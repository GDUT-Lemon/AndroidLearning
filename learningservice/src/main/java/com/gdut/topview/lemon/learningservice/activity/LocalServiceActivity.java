package com.gdut.topview.lemon.learningservice.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gdut.topview.lemon.learningservice.R;

import com.gdut.topview.lemon.learningservice.service.ForegroundService;
import com.gdut.topview.lemon.learningservice.service.LocalService;



public class LocalServiceActivity extends AppCompatActivity implements View.OnClickListener{

    private LocalService mLocalService;
    private ServiceConnection mConnection=new ServiceConnection() {
       @Override
       public void onServiceConnected(ComponentName name, IBinder service) {
           mLocalService=((LocalService.LocalBinder)service).getService();
       }

       @Override
       public void onServiceDisconnected(ComponentName name) {

       }
   };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_service);
        findViewById(R.id.start_service).setOnClickListener(this);
        findViewById(R.id.stop_service).setOnClickListener(this);
        findViewById(R.id.bind_service).setOnClickListener(this);
        findViewById(R.id.unbind_service).setOnClickListener(this);
        findViewById(R.id.start_foreground_service).setOnClickListener(this);
        findViewById(R.id.stop_foreground_dervice).setOnClickListener(this);
        findViewById(R.id.remote_test).setOnClickListener(this);
//        mTextView= (TextView) findViewById(R.id.textView);
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this, LocalService.class);
        Intent intent1=new Intent(this, ForegroundService.class);
        switch (v.getId()){
            case R.id.start_service:
                startService(intent);
                break;
            case R.id.stop_service:
                stopService(intent);
                break;
            case R.id.bind_service:
                bindService(intent,mConnection,BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(mConnection);
                break;
            case R.id.start_foreground_service:
                startService(intent1);
                break;
            case R.id.stop_foreground_dervice:
                stopService(intent1);
                break;
            case R.id.remote_test:
                Intent intent2=new Intent(this,RemoteServiceActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
