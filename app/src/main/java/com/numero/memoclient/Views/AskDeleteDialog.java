package com.numero.memoclient.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.numero.memoclient.R;

public class AskDeleteDialog {

    private OnClickListener onClickListener;
    private AlertDialog.Builder builder;

    public AskDeleteDialog(Context context) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_ask_delete);
        builder.setPositiveButton(R.string.positive_button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onClickListener == null){
                    return;
                }
                onClickListener.onClickPositiveButton();
            }
        });
        builder.setNegativeButton(R.string.negative_button_delete, null);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void show() {
        builder.show();
    }

    public interface OnClickListener {
        void onClickPositiveButton();
    }
}
