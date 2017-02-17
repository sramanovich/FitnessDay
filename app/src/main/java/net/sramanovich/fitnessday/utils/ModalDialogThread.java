package net.sramanovich.fitnessday.utils;

import android.os.AsyncTask;

public class ModalDialogThread extends AsyncTask<Void, Void, Void> {

    private String text;

    public ModalDialogThread(String text) {
        this.text = text;
    }

    @Override
    protected Void doInBackground(Void... h) {
        AskNameDialog msg = AskNameDialog.newInstance(text);
        msg.doModal();
        return null;
    }
}