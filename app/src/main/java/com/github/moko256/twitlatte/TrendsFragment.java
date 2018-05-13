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

import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.moko256.twitlatte.database.CachedTrendsSQLiteOpenHelper;
import com.github.moko256.twitlatte.text.TwitterStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import twitter4j.GeoLocation;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;

/**
 * Created by moko256 on 2017/07/05.
 *
 * @author moko256
 */

public class TrendsFragment extends BaseListFragment {
    TrendsAdapter adapter;
    List<Trend> list;

    CompositeSubscription subscription;

    CachedTrendsSQLiteOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        list=new ArrayList<>();
        subscription = new CompositeSubscription();
        helper = new CachedTrendsSQLiteOpenHelper(getContext(), GlobalApplication.accessToken);
        if (savedInstanceState == null) {
            List<Trend> trends = helper.getTrends();
            if (trends.size() > 0){
                list = trends;
                setRefreshing(false);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=super.onCreateView(inflater, container, savedInstanceState);

        getRecyclerView().addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view)==0){
                    outRect.top=Math.round(getResources().getDisplayMetrics().density*8f);
                }
            }
        });

        adapter=new TrendsAdapter(getContext(), list);
        setAdapter(adapter);
        if (!isInitializedList()){
            adapter.notifyDataSetChanged();
        }

        return view;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            Trend[] l = (Trend[]) savedInstanceState.getSerializable("list");
            if(l!=null){
                list.addAll(Arrays.asList(l));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", list.toArray(new Trend[list.size()]));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        subscription = null;
        helper.close();
        helper = null;
        list=null;
    }

    @Override
    protected void onInitializeList() {
        setRefreshing(true);
        subscription.add(
                getGeoLocationSingle()
                        .flatMap(this::getResponseSingle)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result-> {
                                    list.clear();
                                    list.addAll(Arrays.asList(result.getTrends()));
                                    adapter.notifyDataSetChanged();
                                    setRefreshing(false);
                                },
                                e -> {
                                    e.printStackTrace();
                                    getSnackBar(TwitterStringUtils.convertErrorToText(e)).show();
                                    setRefreshing(false);
                                }
                        )
        );
    }

    @Override
    protected void onUpdateList() {
        onInitializeList();
    }

    @Override
    protected void onLoadMoreList() {}

    @Override
    protected boolean isInitializedList() {
        return !list.isEmpty();
    }

    @Override
    protected RecyclerView.LayoutManager initializeRecyclerViewLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    public Single<Trends> getResponseSingle(GeoLocation geolocation) {
        return Single.create(
                subscriber->{
                    try {
                        Trends trends = GlobalApplication.twitter
                                .getPlaceTrends(GlobalApplication.twitter.getClosestTrends(geolocation).get(0).getWoeid());
                        helper.setTrends(Arrays.asList(trends.getTrends()));

                        subscriber.onSuccess(trends);
                    } catch (TwitterException e) {
                        subscriber.onError(e);
                    }
                }
        );
    }

    public Single<GeoLocation> getGeoLocationSingle(){
        return Single.create(
                subscriber -> {
                    try {
                        Geocoder geocoder = new Geocoder(getContext());
                        Locale locale = Locale.getDefault();
                        Address address = geocoder.getFromLocationName(locale.getDisplayCountry(), 1).get(0);
                        if (address != null){
                            subscriber.onSuccess(new GeoLocation(address.getLatitude(), address.getLongitude()));
                        } else {
                            subscriber.onError(new Exception("Cannot use trends"));
                        }
                    } catch(IOException e){
                        subscriber.onError(e);
                    }
                }
        );
    }

}
