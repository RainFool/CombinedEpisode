package com.rainfool.combinedepisode.core;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rainfool.combinedepisode.R;

/**
 * Created by rainfool on 16/6/22.
 * 联动的剧集列表
 */
public class CombinedEpisodesView extends RelativeLayout implements View.OnFocusChangeListener {

    public static final String TAG = CombinedEpisodesView.class.getSimpleName();

    Context mContext;
    RelativeLayout mContentPanel;
    RecyclerView mEpisodesView, mGroupsView;
    LinearLayoutManager mEpisodesLayoutManager, mGroupLayoutManager;

    CombinedEpisodesAdapter mAdapter;

    public CombinedEpisodesView(Context context) {
        this(context, null);
    }

    public CombinedEpisodesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CombinedEpisodesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            mContext = context;
            init();
        }
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.combined_episodes_layout, this, true);

        mEpisodesView = (RecyclerView) findViewById(R.id.episodes);
        mGroupsView = (RecyclerView) findViewById(R.id.groups);

        mEpisodesLayoutManager = new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false);
        mGroupLayoutManager = new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false);

        mEpisodesView.setLayoutManager(mEpisodesLayoutManager);
        mGroupsView.setLayoutManager(mGroupLayoutManager);

        mEpisodesView.setItemAnimator(new DefaultItemAnimator());
        mGroupsView.setItemAnimator(new DefaultItemAnimator());

        mEpisodesView.setOnFocusChangeListener(this);
        mGroupsView.setOnFocusChangeListener(this);
        this.setOnFocusChangeListener(this);
    }


    public void setAdapter(final CombinedEpisodesAdapter adapter) {
        mAdapter = adapter;
        mEpisodesView.setAdapter(adapter.getEpisodesAdapter());
        mGroupsView.setAdapter(adapter.getGroupAdapter());

        adapter.getGroupAdapter().setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onGroupItemClick(View view, int position) {
                Log.d(TAG, "group item " + position + " has been clicked;Episodes scroll to "
                        + adapter.getEpisodesPosition(position));
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getEpisodesPosition(position), 0);
            }
        });
        adapter.getGroupAdapter().setOnItemFocusListener(new GroupAdapter.OnItemFocusListener() {
            @Override
            public void onGroupItemFocus(View view, int position, boolean hasFocus) {
                Log.d(TAG, "group item " + position + " has been focused;Episodes scroll to "
                        + adapter.getEpisodesPosition(position));
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getEpisodesPosition(position), 0);
            }
        });

        adapter.getEpisodesAdapter().setOnItemFocusListener(new EpisodesAdapter.OnItemFocusListener() {
            @Override
            public void onEpisodesItemFocus(View view, int position, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(TAG, "episodes item " + position + "has ben focus;group item"
                            + adapter.getGroupPosition(position) + "need be selected!");
                    mGroupLayoutManager.scrollToPosition(position);
                    setSelectedGroup(adapter.getGroupPosition(position));
                }
            }
        });

        adapter.getEpisodesAdapter().setOnItemClickListener(new EpisodesAdapter.OnItemClickListener() {
            @Override
            public void onEpisodesItemClick(View view, int position) {
                Log.d(TAG, "episodes item " + position);
            }
        });
    }


    protected void setSelectedGroup(int position) {
        mGroupsView.getChildAt(position).setSelected(true);
        int count = mGroupsView.getChildCount();
        for (int i = 0; i < count; i++) {
            if (i == position) {
                mGroupsView.getChildAt(i).setSelected(true);
            } else {
                mGroupsView.getChildAt(i).setSelected(false);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus) {
            Log.d(TAG, "content panel has focus");
            mEpisodesView.requestFocus();
        }
        if (v == mEpisodesView && hasFocus) {

            Log.d(TAG, "episodes has focus");
            //TODO
            mEpisodesView.getChildAt(0).requestFocus();
        }
    }
}
