package com.rainfool.combinedepisode.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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
    EpisodesAdapter mEpisodesAdapter;
    GroupAdapter mGroupAdapter;

    Handler mHandler = new Handler(Looper.getMainLooper());

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
        mEpisodesAdapter = adapter.getEpisodesAdapter();
        mGroupAdapter = adapter.getGroupAdapter();
        mEpisodesView.setAdapter(mEpisodesAdapter);
        mGroupsView.setAdapter(mGroupAdapter);

        mGroupAdapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onGroupItemClick(View view, int position) {
                Log.d(TAG, "group item " + position + " has been clicked;Episodes scroll to "
                        + adapter.getEpisodesPosition(position));
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getEpisodesPosition(position), 0);
            }
        });
        mGroupAdapter.setOnItemFocusListener(new GroupAdapter.OnItemFocusListener() {
            @Override
            public void onGroupItemFocus(View view, int position, boolean hasFocus) {
                Log.d(TAG, "group item " + position + " has been focused;Episodes scroll to "
                        + adapter.getEpisodesPosition(position));
                int episodePosition = adapter.getEpisodesPosition(position);
                mEpisodesAdapter.mCurrentPosition = episodePosition;
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getEpisodesPosition(position), 0);
            }
        });

        mEpisodesAdapter.setOnItemFocusListener(new EpisodesAdapter.OnItemFocusListener() {
            @Override
            public void onEpisodesItemFocus(View view, int position, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(TAG, "episodes item " + position + "has ben focus;group item"
                            + adapter.getGroupPosition(position) + "need be selected!");
                    int groupPosition = adapter.getGroupPosition(position);
                    mGroupLayoutManager.scrollToPositionWithOffset(groupPosition,0);
                    mGroupAdapter.mCurrentPosition = adapter.getGroupPosition(groupPosition);
                }
            }
        });

        mEpisodesAdapter.setOnItemClickListener(new EpisodesAdapter.OnItemClickListener() {
            @Override
            public void onEpisodesItemClick(View view, int position) {
                Log.d(TAG, "No Episodes Item click listener set!episodes item " + position);
            }
        });

        mEpisodesAdapter.setOnItemLongFocusListener(new EpisodesAdapter.OnItemLongFocusListener() {
            @Override
            public void onEpisodesItemLongFocus(View v, int position) {

                Log.d(TAG, "No Episodes Item long focus listener set!episodes item " + position);
            }

        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (mGroupsView.hasFocus()) {
                        Log.d(TAG, "test group has focus when dpad UP click");
                        mEpisodesView.requestFocus();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (mEpisodesView.hasFocus()) {
                        Log.d(TAG, "test group has focus when dpad DOWN click");
                        mGroupsView.requestFocus();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (mEpisodesView.hasFocus() &&
                            mEpisodesAdapter.getCurrentPosition() >= mEpisodesAdapter.getData().size() - 1) {
                        return true;
                    }
                    if (mGroupsView.hasFocus() &&
                            mGroupAdapter.getCurrentPosition() >= mGroupAdapter.getDatas().size() - 1) {
                        return true;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus) {
            Log.d(TAG, "content panel has focus");
            mEpisodesView.requestFocus();
        } else if (v == mEpisodesView && hasFocus) {

            Log.d(TAG, "episodes has focus");
            View child = mEpisodesView.getLayoutManager().findViewByPosition(mEpisodesAdapter.getCurrentPosition());
            if (child != null) {
                child.requestFocus();
            } else {
                int lastPosition = mEpisodesLayoutManager.findLastVisibleItemPosition();
                child = mEpisodesLayoutManager.findViewByPosition(lastPosition);
                if (child != null)
                    child.requestFocus();
            }
        } else if (v == mGroupsView && hasFocus) {
            Log.d(TAG, "group has focus，position:" + mGroupAdapter.getCurrentPosition());

            View child = mGroupsView.getLayoutManager().findViewByPosition(mGroupAdapter.getCurrentPosition());
            if (child != null) {
                child.requestFocus();
            }
        }
    }
}
