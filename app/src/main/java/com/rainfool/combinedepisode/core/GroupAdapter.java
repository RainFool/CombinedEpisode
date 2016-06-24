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
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private static final int GROUPS_COLUMN_COUNT = 5;

    OnItemClickListener mItemClickListener;
    OnItemFocusListener mItemFocusListener;

    List<String> mDatas;

    int parentWidth,itemWidth;

    int mCurrentPosition;

    public GroupAdapter(List<String> datas) {
        mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.episodes_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        parentWidth = parent.getMeasuredWidth();
        itemWidth = (parentWidth -
                (holder.tv.getPaddingLeft() + holder.tv.getPaddingRight()) * (GROUPS_COLUMN_COUNT))
                / GROUPS_COLUMN_COUNT + 1;
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setText(mDatas.get(position));
        holder.tv.setWidth(itemWidth);
        holder.tv.setFocusable(true);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onGroupItemClick(v, position);
            }
        });

        holder.tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mItemFocusListener.onGroupItemFocus(v, position, hasFocus);
                    mCurrentPosition = position;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setOnItemFocusListener(OnItemFocusListener listener) {
        mItemFocusListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.item);
        }
    }

    public interface OnItemClickListener {
        void onGroupItemClick(View view, int position);
    }

    public interface OnItemFocusListener {
        void onGroupItemFocus(View view, int position, boolean hasFocus);
    }
}
