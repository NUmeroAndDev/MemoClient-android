package com.numero.memoclient.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.numero.memoclient.MemoApiClient.Memo;
import com.numero.memoclient.R;

import java.util.ArrayList;

public class MemoItemAdapter extends RecyclerView.Adapter<MemoItemHolder>{

    private ArrayList<Memo> memoList;
    private OnClickListener onClickListener;

    public MemoItemAdapter(ArrayList<Memo> memoList){
        this.memoList = memoList;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public MemoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
        return new MemoItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MemoItemHolder holder, final int position) {
        holder.titleTextView.setText(memoList.get(position).title);
        holder.createTextView.setText(memoList.get(position).created);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener == null){
                    return;
                }
                onClickListener.onClick(position);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onClickListener == null){
                    return false;
                }
                onClickListener.onLongClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public interface OnClickListener{
        void onClick(int position);

        void onLongClick(int position);
    }
}
