package com.gdut.topview.lemon.learningservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.gdut.topview.lemon.learningservice.RemoteServiceInterface;

/**
 * 远程服务实现
 * 实现远程服务的两种途径：AIDL&Messenger
 * Using AIDL is necessary only if you allow clients from different applications to access your service for IPC and want to handle multithreading in your service.
 * If you do not need to perform concurrent IPC across different applications, you should create your interface by implementing a Binder
 * or, if you want to perform IPC, but do not need to handle multithreading, implement your interface using a Messenger.
 * Regardless, be sure that you understand Bound Services before implementing an AIDL.
 *
 * Calls made from the local process are executed in the same thread that is making the call.
 * If this is your main UI thread, that thread continues to execute in the AIDL interface.
 * If it is another thread, that is the one that executes your code in the service.
 */
public class RemoteServiceWithAIDL extends Service {
    private static final String TAG="LearningService";
    private  final RemoteInterface mRemoteInterface=new RemoteInterface();
    private String data="服务内部的数据";
    private boolean isRuning=false;
    public RemoteServiceWithAIDL() {
    }

    public class RemoteInterface extends RemoteServiceInterface.Stub{

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void getData(String data) throws RemoteException {
            RemoteServiceWithAIDL.this.data=data;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG,"onBind method is called");
       return mRemoteInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate method is called");
        new Thread(){
            @Override
            public void run() {
                super.run();
                isRuning=true;
                while (isRuning){
                    Log.i(TAG,data);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy method is called");
        isRuning=false;
    }


}
