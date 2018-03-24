/*
 * Copyright 2018 The twicalico authors
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

package com.github.moko256.twicalico;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.moko256.mastodon.MTException;
import com.github.moko256.mastodon.MTRangeConverter;
import com.github.moko256.mastodon.MTResponseList;
import com.github.moko256.mastodon.MastodonTwitterImpl;
import com.github.moko256.twicalico.entity.Type;
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException;
import com.sys1yagi.mastodon4j.api.method.Accounts;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by moko256 on 2018/03/10.
 *
 * @author moko256
 */
public class MediaTimelineFragment extends BaseTweetListFragment implements ToolbarTitleInterface {

    long userId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (userId == -1){
            userId = getArguments().getLong("userId", -1);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        adapter.setShouldShowMediaOnly(true);
        return view;
    }

    public static MediaTimelineFragment newInstance(long userId){
        MediaTimelineFragment result = new MediaTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        result.setArguments(args);
        return result;
    }

    @Override
    public ResponseList<Status> getResponseList(Paging paging) throws TwitterException {
        if (GlobalApplication.clientType == Type.MASTODON){
            Accounts accounts = new Accounts(((MastodonTwitterImpl) GlobalApplication.twitter).client);
            try {
                return MTResponseList.convert(accounts.getStatuses(userId, true, MTRangeConverter.convert(paging)).execute());
            } catch (Mastodon4jRequestException e) {
                throw new MTException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.media;
    }

    @Override
    protected String getCachedIdsDatabaseName() {
        return "MediaTimeline_"+String.valueOf(userId);
    }
}