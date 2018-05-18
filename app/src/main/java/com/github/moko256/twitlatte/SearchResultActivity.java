/*
 * Copyright 2015-2018 The twitlatte authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.moko256.twitlatte;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by moko256 on 2017/01/26.
 *
 * @author moko256
 */

public class SearchResultActivity extends AppCompatActivity implements BaseListFragment.GetSnackBar, BaseTweetListFragment.GetRecyclerViewPool {

    RecyclerView.RecycledViewPool tweetListViewPool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("query"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);

        tweetListViewPool = new RecyclerView.RecycledViewPool();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_search_fragment_container,new SearchResultFragment())
                    .commit();
        }
    }

    public static Intent getIntent(Context context, String query){
        return new Intent(context, SearchResultActivity.class).putExtra("query", query);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public Snackbar getSnackBar(String string) {
        return Snackbar.make(findViewById(R.id.activity_search_fragment_container), string, Snackbar.LENGTH_LONG);
    }

    @Override
    public RecyclerView.RecycledViewPool getTweetListViewPool() {
        return tweetListViewPool;
    }
}
