package com.rainfool.combinedepisode.core;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

/**
 * Created by rainfool on 16/6/22.
 */
public abstract class CombinedEpisodesAdapter <T>{

    EpisodesAdapter mEpisodesAdapter;
    GroupAdapter mGroupAdapter;

    public CombinedEpisodesAdapter() {
        mEpisodesAdapter = new EpisodesAdapter(getEpisodesList());
        mGroupAdapter = new GroupAdapter(getGroupList());

    }

    public EpisodesAdapter getEpisodesAdapter() {
        return mEpisodesAdapter;
    }

    public GroupAdapter getGroupAdapter() {
        return mGroupAdapter;
    }

    /**
     * @return 剧集部分的显示内容
     */
    public abstract List<String> getEpisodesList();

    public abstract List<String> getGroupList();

    public abstract int getEpisodesPosition(int groupPosition);

    public abstract int getGroupPosition(int episodesPosition);

}
