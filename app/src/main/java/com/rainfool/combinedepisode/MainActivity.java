package com.rainfool.combinedepisode;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rainfool.combinedepisode.core.CombinedEpisodesAdapter;
import com.rainfool.combinedepisode.core.CombinedEpisodesView;
import com.rainfool.combinedepisode.core.GroupAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CombinedEpisodesView mCombinedEpisodesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCombinedEpisodesView = (CombinedEpisodesView) findViewById(R.id.combinedEpisodes);


        final String[] episodes = {
                "第1集", "第2集", "第3集", "第4集", "第5集",
                "第6集", "第7集", "第8集", "第9集", "第10集",
                "第一集", "第二集", "第三集", "第四集", "第五集",
        };

        final String[] groups = {"1-3", "4-6","7-9","10-13","13-15"};

        final Integer[] selectedPositions = {1,2,3,4,6};

        final CombinedEpisodesAdapter<String> adapter = new CombinedEpisodesAdapter<String>() {
            @Override
            public List<String> getEpisodesList() {
                return Arrays.asList(episodes);
            }

            @Override
            public List<String> getGroupList() {
                return Arrays.asList(groups);
            }

            @Override
            public int getEpisodesPosition(int groupPosition) {
                return groupPosition * 3;
            }

            @Override
            public int getGroupPosition(int episodesPosition) {
                return episodesPosition / 3;
            }
        };

        mCombinedEpisodesView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setSelectedPositions(Arrays.asList(selectedPositions));
                mCombinedEpisodesView.setAdapter(adapter);
                mCombinedEpisodesView.requestFocus();
            }
        }, 300);
    }
}