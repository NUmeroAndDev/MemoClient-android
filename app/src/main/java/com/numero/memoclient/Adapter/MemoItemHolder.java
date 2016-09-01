package com.numero.memoclient.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.numero.memoclient.R;

public class MemoItemHolder extends RecyclerView.ViewHolder{

    protected TextView titleTextView;
    protected TextView createTextView;
    protected View view;

    public MemoItemHolder(View itemView) {
        super(itemView);

        this.view = itemView;
        this.titleTextView = (TextView) itemView.findViewById(R.id.memo_title_text);
        this.createTextView = (TextView) itemView.findViewById(R.id.create_text);
    }
}
