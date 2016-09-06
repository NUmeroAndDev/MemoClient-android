package com.numero.memoclient.Views;

import android.app.ProgressDialog;
import android.content.Context;

import com.numero.memoclient.R;

public class LoadingProgressDialog {

    private ProgressDialog dialog;

    public LoadingProgressDialog(Context context){
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.message_loading));
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
