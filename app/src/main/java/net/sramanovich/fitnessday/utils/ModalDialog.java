package net.sramanovich.fitnessday.utils;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;


public class ModalDialog extends DialogFragment {

    static Handler static_handler;

    static Activity static_activity;

    Object sync = new Object();

    public void doModal(){
        static_handler.sendMessage(Message.obtain(static_handler, 100, this));
        try {
            synchronized (sync){
                sync.wait();
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void onDismiss (DialogInterface dialog){
        super.onDismiss(dialog);
        synchronized (sync){
            sync.notify();
        }
    }

    public static void init(Activity a){
        static_activity = a;
        static_handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                ModalDialog dlg = (ModalDialog)msg.obj;
                dlg.show(static_activity.getFragmentManager(), "dialog");
            }
        };
    }
}