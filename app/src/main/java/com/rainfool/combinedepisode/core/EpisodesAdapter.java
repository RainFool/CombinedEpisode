package com.rainfool.combinedepisode.core;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainfool.combinedepisode.R;

import java.util.List;

/**
 * Created by rainfool on 16/6/22.
 */
public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.MyViewHolder> {

    private static final int EPISODES_COLUMN_COUNT = 10;

    OnItemClickListener mClickListener;
    OnItemLongFocusListener mLongFocusListener;
    OnItemFocusListener mFocusListener;

    List<String> mData;

    int parentWidth, itemWidth;

    public EpisodesAdapter(List<String> data) {
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.episodes_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        parentWidth = parent.getMeasuredWidth();
        itemWidth = (parentWidth -
                (holder.tv.getPaddingLeft() + holder.tv.getPaddingRight()) * (EPISODES_COLUMN_COUNT))
                / EPISODES_COLUMN_COUNT + 1;
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv.setText(mData.get(position));
        holder.tv.setWidth(itemWidth);
        Log.d(CombinedEpisodesView.TAG, "episodes item width :" + itemWidth + " position:" + position);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onEpisodesItemClick(v, position);
            }
        });
        holder.tv.setFocusable(true);
        holder.tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mFocusListener.onEpisodesItemFocus(v, position, hasFocus);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongFocusListener(OnItemLongFocusListener listener) {
        mLongFocusListener = listener;
    }

    public void setOnItemFocusListener(OnItemFocusListener listener) {
        mFocusListener = listener;
    }

    public interface OnItemClickListener {
        void onEpisodesItemClick(View view, int position);
    }

    public interface OnItemLongFocusListener {
        void onEpisodesItemLongFocus();
    }

    public interface OnItemFocusListener {
        void onEpisodesItemFocus(View view, int position, boolean hasFocus);
    }


}
