package com.gdut.topview.lemon.learningservice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gdut.topview.lemon.learningservice.R;
import com.gdut.topview.lemon.learningservice.activity.LocalServiceActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 前台服务
 * 前台服务&后台服务
 *      1、前台服务：会在通知一栏显示 ONGOING 的 Notification。当服务被终止的时候，通知一栏的 Notification 也会消失，这样对于用户有一定的通知作用。常见的如音乐播放服务。
 *      2、后台服务：默认的服务即为后台服务，即不会在通知一栏显示 ONGOING 的 Notification。	当服务被终止的时候，用户是看不到效果的
 *      3、sdk 2.0 及其以后版本使用的方法是 startForeground 与 stopForeground，之前版本使用的是 setForeground 为了保证向后兼容性，这里必须使用反射技术来调用新方法。
 * @author lemon
 */
public class ForegroundService extends Service {

    private NotificationManager mManager;
    private static final Class[] mStartForegroundSignature=new Class[]{int.class, Notification.class};
    private static final Class[] mStopForegroundSignature=new Class[]{boolean.class};
    private static final Class[] mSetForegroundSignature=new Class[]{boolean.class};
    private Method mStartForeground;
    private Method mStopForeground;
    private Method mSetForeground;
    private Object[] mSetForegroundArgs=new Object[1];
    private Object[] mStartForegroundArgs=new Object[2];
    private Object[] mStopForegroundArgs=new Object[1];
    private static final String TAG="LearningService";

    private void invokeMethod(Method method,Object[] args){
        try {
            method.invoke(this,args);
        } catch (InvocationTargetException e) {
            Log.w(TAG, "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "Unable to invoke method", e);
        }
    }

    public ForegroundService() {
    }

    /*
     *关于java中的反射机制
     *JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；
     *对于任意一个对象，都能够调用它的任意一个方法；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。
     *Java反射机制主要提供了以下功能： 在运行时判断任意一个对象所属的类；
     *                            在运行时构造任意一个类的对象；
     *                            在运行时判断任意一个类所具有的成员变量和方法；
     *                            在运行时调用任意一个对象的方法；生成动态代理。
     */

    /**
     * 以兼容的模式开启前台服务
     * @param id
     * @param notification
     */
    private void startForegroundCompat(int id,Notification notification){
        if(mStartForeground!=null){
            mStartForegroundArgs[0]=id;
            mStartForegroundArgs[1]=notification;
            invokeMethod(mStartForeground,mStartForegroundArgs);
            return;
        }
        mSetForegroundArgs[0]=Boolean.TRUE;
        invokeMethod(mSetForeground,mSetForegroundArgs);
        mManager.notify(id,notification);
    }

    /**
     * 以兼容的模式停止前台服务
     * @param id
     */
    private void stopForegroundCompat(int id){
        if(mStopForeground!=null){
            mStopForegroundArgs[0]=Boolean.TRUE;
            invokeMethod(mStopForeground,mStopForegroundArgs);
            return;
        }
        //在 setForeground 之前调用 cancel，因为我们有可能在取消前台服务之后的那一瞬间被kill掉。这个时候 notification 便永远不会从通知一栏移除
        mManager.cancel(id);
        mSetForegroundArgs[0]=Boolean.FALSE;
        invokeMethod(mSetForeground,mSetForegroundArgs);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind method is called");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate Method is Called");
        mManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification=new Notification(R.mipmap.ic_launcher,"Foreground Service Started",System.currentTimeMillis());
        startForeground(0,notification);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,new Intent(this, LocalServiceActivity.class),0);
        notification.setLatestEventInfo(this,"Foreground Service", "Foreground Service Started.", contentIntent);
        try {
            mStartForeground=getClass().getMethod("startForeground",mStartForegroundSignature);
            mStopForeground=getClass().getMethod("stopForeground",mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground=mSetForeground=null;
            e.printStackTrace();
        }
        try {
            mSetForeground=getClass().getMethod("setForeground",mSetForegroundSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("OS doesn't have Service.startForeground OR Service.setForeground!");
        }
        startForegroundCompat(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroyMethod is Called");
        stopForegroundCompat(1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand method is called");
        return super.onStartCommand(intent, flags, startId);
    }
}
