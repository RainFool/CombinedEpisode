package com.rainfool.combinedepisode.core;

import android.os.Handler;
import android.os.Looper;
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

    public static final int LONG_FOCUS_TIME = 2000;

    OnItemClickListener mClickListener;
    OnItemLongFocusListener mLongFocusListener;
    OnItemFocusListener mFocusListener;

    List<String> mData;
    List<Integer> mSelectedPositions;

    int parentWidth, itemWidth;

    int mCurrentPosition;

    Handler mHandler = new Handler(Looper.getMainLooper());

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

        if (position < mData.size()) {
            holder.tv.setText(mData.get(position));
            holder.tv.setWidth(itemWidth);
            holder.tv.setFocusable(true);
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setSelected(false);
            Log.d(CombinedEpisodesView.TAG, "episodes item width :" + itemWidth + " position:" + position);

            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onEpisodesItemClick(v, position);
                }
            });

            final LongFocusRunnable longFocusRunnable = new LongFocusRunnable(holder.tv, position);
            holder.tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    if (hasFocus) {
                        mFocusListener.onEpisodesItemFocus(v, position, hasFocus);
                        mCurrentPosition = position;
                        mHandler.postDelayed(longFocusRunnable, LONG_FOCUS_TIME);
                    } else {
                        mHandler.removeCallbacks(longFocusRunnable);
                    }
                }
            });

            if (mSelectedPositions != null && mSelectedPositions.contains(position)) {
                holder.tv.setSelected(true);
            }

        } else {
            holder.tv.setText("");
            holder.tv.setWidth(itemWidth);
            holder.tv.setVisibility(View.INVISIBLE);
            holder.tv.setFocusable(false);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size() + EPISODES_COLUMN_COUNT;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public List<String> getData() {
        return mData;
    }

    public List<Integer> getSelectedPositions() {
        return mSelectedPositions;
    }

    public void setSelectedPositions(List<Integer> mPositions) {
        this.mSelectedPositions = mPositions;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.item);
            tv.setFocusable(true);
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
        void onEpisodesItemLongFocus(View v, int position);
    }

    public interface OnItemFocusListener {
        void onEpisodesItemFocus(View view, int position, boolean hasFocus);
    }

    class LongFocusRunnable implements Runnable {

        View v;
        int position;

        public LongFocusRunnable(View v, int position) {
            this.v = v;
            this.position = position;
        }

        @Override
        public void run() {
            mLongFocusListener.onEpisodesItemLongFocus(v, position);
        }
    }
}
