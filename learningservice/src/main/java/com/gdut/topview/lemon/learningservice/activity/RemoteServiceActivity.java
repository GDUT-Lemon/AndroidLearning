package com.gdut.topview.lemon.learningservice.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import com.gdut.topview.lemon.learningservice.R;
import com.gdut.topview.lemon.learningservice.service.RemoteServiceWithMessenger;

/**
 * Created by lemon on 15/7/19.
 */
public class RemoteServiceActivity extends Activity implements View.OnClickListener{

    private static final String TAG="RemoteServiceActivity";

    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RemoteServiceWithMessenger.MSG_SET_VALUE:
                    Log.i(TAG,"Received from service:"+msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    private boolean isBound=false;
    private Messenger mRemoteMessenger;
    private Messenger mClientMessenger=new Messenger(new IncomingHandler());
    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteMessenger=new Messenger(service);
            Message msg=Message.obtain(null,RemoteServiceWithMessenger.MSG_REGISTER_CLIENT);
            msg.replyTo=mClientMessenger;
            try {
                mRemoteMessenger.send(msg);
                sendMessage();
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteMessenger=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_service);
        findViewById(R.id.bind_service).setOnClickListener(this);
        findViewById(R.id.unbind_service).setOnClickListener(this);
        findViewById(R.id.start_service).setOnClickListener(this);
        findViewById(R.id.stop_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,RemoteServiceWithMessenger.class);
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
        }
    }

    /**
     * 使用服务端的信使向它发送一个消息。
     */
    private void sendMessage() {
        // TODO Auto-generated method stub
        Message message = Message.obtain(null, RemoteServiceWithMessenger.MSG_SET_VALUE);
        message.replyTo = mClientMessenger;
        try {
            mRemoteMessenger.send(message);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
