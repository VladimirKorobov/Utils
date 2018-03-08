package com.mega.graphics.Threads;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Vladimir on 23.03.2016.
 */
public class MainThread extends Thread{
    private Handler handler;
    public MainThread(Handler handler) {
        this.handler = handler;
    }
    private boolean bRunning = true;
    public void EndThread()
    {
        bRunning = false;
    }
    public void ThreadBody(Handler handler) {
        if(handler != null)
        {
            Message msg = handler.obtainMessage(1, 0, 0, 0);
            handler.sendMessage(msg);
        }
    }
    public void Process(Handler handler) {
        ThreadBody(handler);
        /*
        try {
            ThreadBody(handler);
            //Thread.sleep(1000);
        }
        catch (InterruptedException e) {

        }
        */
    }
    @Override
    public void run() {
        while (bRunning) {
            Process(handler);
        }
    }
}
