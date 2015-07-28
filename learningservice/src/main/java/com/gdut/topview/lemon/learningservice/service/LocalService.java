package com.gdut.topview.lemon.learningservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 本地服务
 * 本地服务（LocalService）&远程服务（RemoteService）：
 *      1、本地服务依附在主进程上而不是独立的进程，这样在一定程度上节约了资源，另外Local服务因为是在同一进程因此不需要IPC，也不需要AIDL。相应bindService会方便很多。
 *         主进程被Kill后，服务便会终止。
 *      2、远程服务服务为独立的进程，对应进程名格式为所在包名加上你指定的android:process字符串。由于是独立的进程，因此在Activity所在进程被Kill的时候，该服务依然在运行，不受其他进程影响，有利于为多个进程提供服务具有较高的灵活性。
 *         该服务是独立的进程，会占用一定资源，并且使用AIDL进行IPC稍微麻烦一点。
 * @author lemon
 * @date 2015-07-18
 *
 */
public class LocalService extends Service {

    private final static  String TAG="LearningService";
    private final LocalBinder mBinder=new LocalBinder();
    public static String GET="LOCAL SERVICE";


    public class LocalBinder extends Binder{
        public LocalService getService(){
            return LocalService.this;
        }
    }

    public LocalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG,"onBind Method Called");
        return  mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate Method Called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy Method Called");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"onUnbind Method Called");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG,"onRebind Method Called");
        super.onRebind(intent);
    }

    /**
     *使用startService()这个方法启动service的时候会回调这个方法（使用bindService不会回调这个方法）
     * @param intent
     * @param flags
     * @param startId
     * @return  START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
     *               随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
     *               如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
     *
     *          START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统将会把它置为started状态，系统不会自动重启该服务，直到startService(Intent intent)方法再次被调用;。
     *
     *          START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     *
     *          START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand Method Called");
        Log.i(TAG, "the startId is" + startId + ":" + intent);
//        return super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY;
//        return START_STICKY_COMPATIBILITY;
        return START_REDELIVER_INTENT;
    }

    public String printText(){
        return "Game Is Over";
    }


}
