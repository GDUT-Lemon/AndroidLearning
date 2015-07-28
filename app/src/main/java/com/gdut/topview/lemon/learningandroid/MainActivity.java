package com.gdut.topview.lemon.learningandroid;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gdut.topview.lemon.learningservice.RemoteServiceInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RemoteServiceInterface mRemoteInterface=null;

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("this","service connected");
            mRemoteInterface=RemoteServiceInterface.Stub.asInterface(service);
            try {
                mRemoteInterface.getData("这是外部应用数据");
            } catch (RemoteException e) {
                throw new IllegalStateException("无法获取远程服务");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    private Intent mServiceIntent=new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceIntent.setComponent(new ComponentName("com.gdut.topview.lemon.learningservice","com.gdut.topview.lemon.learningservice.service.RemoteServiceWithAIDL"));
        setContentView(R.layout.activity_main);
        findViewById(R.id.bind_remote_service).setOnClickListener(this);
        findViewById(R.id.unbind_remote_service).setOnClickListener(this);
        findViewById(R.id.start_service).setOnClickListener(this);
        findViewById(R.id.stop_service).setOnClickListener(this);
//        findViewById(R.id.)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_remote_service:
                bindService(mServiceIntent,mServiceConnection,BIND_AUTO_CREATE);
                break;
            case R.id.unbind_remote_service:
                unbindService(mServiceConnection);
                break;
            case R.id.start_service:
                startService(mServiceIntent);
                break;
            case R.id.stop_service:
                stopService(mServiceIntent);
                break;
        }
    }
}
