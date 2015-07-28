package com.gdut.topview.lemon.learningservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 远程服务 利用messenger的实现
 * For most applications, the service doesn't need to perform multi-threading, so using a Messenger allows the service to handle one call at a time.
 * If it's important that your service be multi-threaded, then you should use AIDL to define your interface.
 *
 * 1、The service implements a Handler that receives a callback for each call from a client.
 * 2、The Handler is used to create a Messenger object (which is a reference to the Handler).
 * 3、The Messenger creates an IBinder that the service returns to clients from onBind().
 * 4、Clients use the IBinder to instantiate the Messenger (that references the service's Handler), which the client uses to send Message objects to the service.
 * 5、The service receives each Message in its Handler—specifically, in the handleMessage() method.
 *
 * @author lemon
 * @date 2015-07-18
 */
public class RemoteServiceWithMessenger extends Service {

    private static final String TAG="LearningService";
    /** Keeps track of all current registered clients. */
    private ArrayList<Messenger>mClients=new ArrayList<>();
    private int mValue;
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SET_VALUE = 3;
    private final Messenger mMessenger=new Messenger(new IncomingHandler());

    /**
     * Handler of incoming message from clients
     */
    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                    mValue=msg.arg1;
                    for(int i=mClients.size()-1;i>=0;i--){
                        try {
                            mClients.get(i).send(Message.obtain(null, MSG_SET_VALUE, mValue, 0));
                        } catch (RemoteException e) {
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    public RemoteServiceWithMessenger() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind Method is called");
       return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate Method is called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy mehod is called");
    }
}
