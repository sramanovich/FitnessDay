package net.sramanovich.fitnessday.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

public class AskNameDialog extends ModalDialog {

    public static AskNameDialog newInstance(String msg){
        AskNameDialog dlg = new AskNameDialog();
        Bundle args = new Bundle();
        args.putString("msg", msg);
        dlg.setArguments(args);
        return dlg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DialogInterface.OnClickListener dlgButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        };

        String msg = getArguments().getString("msg");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Program name")
                .setMessage(msg)
                .setView(input)
                .setPositiveButton("OK", dlgButtonListener)
                .create();
    }
}