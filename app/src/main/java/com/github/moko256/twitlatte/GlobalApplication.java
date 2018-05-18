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

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.widget.Toast;

import com.github.moko256.mastodon.MastodonTwitterImpl;
import com.github.moko256.twitlatte.cacheMap.StatusCacheMap;
import com.github.moko256.twitlatte.cacheMap.UserCacheMap;
import com.github.moko256.twitlatte.config.AppConfiguration;
import com.github.moko256.twitlatte.entity.AccessToken;
import com.github.moko256.twitlatte.entity.Type;
import com.github.moko256.twitlatte.model.AccountsModel;
import com.github.moko256.twitlatte.notification.ExceptionNotification;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import androidx.appcompat.app.AppCompatDelegate;
import okhttp3.OkHttpClient;
import twitter4j.AlternativeHttpClientImpl;
import twitter4j.HttpClient;
import twitter4j.HttpClientConfiguration;
import twitter4j.HttpClientFactory;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by moko256 on 2016/04/30.
 *
 * @author moko256
 */
public class GlobalApplication extends Application {

    public static int statusLimit;
    public static int statusCacheListLimit = 1000;

    public static LruCache<Configuration, Twitter> twitterCache = new LruCache<>(4);
    public static Twitter twitter;
    public static AccessToken accessToken;

    @Type.ClientTypeInt
    public static int clientType = -1;

    static long userId;

    public static AppConfiguration configuration;

    public static UserCacheMap userCache = new UserCacheMap();
    public static StatusCacheMap statusCache = new StatusCacheMap();

    public static AccountsModel accountsModel;

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "crash_log",
                    getString(R.string.crash_log),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(getString(R.string.crash_log_channel_description));
            channel.setLightColor(Color.RED);
            channel.enableLights(true);
            channel.setShowBadge(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        final Thread.UncaughtExceptionHandler defaultUnCaughtExceptionHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            try {
                new ExceptionNotification().create(e, getApplicationContext());
            }catch (Throwable fe){
                fe.printStackTrace();
            } finally {
                defaultUnCaughtExceptionHandler.uncaughtException(t,e);
            }
        });

        SharedPreferences defaultSharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);

        configuration=new AppConfiguration();

        configuration.isPatternTweetMuteEnabled = defaultSharedPreferences.getBoolean("patternTweetMuteEnabled",false);
        if(configuration.isPatternTweetMuteEnabled){
            try {
                configuration.tweetMutePattern = Pattern.compile(defaultSharedPreferences.getString("tweetMutePattern",""));
            } catch (PatternSyntaxException e){
                e.printStackTrace();
                configuration.isPatternTweetMuteEnabled = false;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        configuration.isPatternTweetMuteShowOnlyImageEnabled = defaultSharedPreferences.getBoolean("patternTweetMuteShowOnlyImageEnabled",false);
        if(configuration.isPatternTweetMuteShowOnlyImageEnabled){
            try {
                configuration.tweetMuteShowOnlyImagePattern = Pattern.compile(defaultSharedPreferences.getString("tweetMuteShowOnlyImagePattern",""));
            } catch (PatternSyntaxException e){
                e.printStackTrace();
                configuration.isPatternTweetMuteShowOnlyImageEnabled = false;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        configuration.isPatternUserScreenNameMuteEnabled = defaultSharedPreferences.getBoolean("patternUserScreenNameMuteEnabled",false);
        if(configuration.isPatternUserScreenNameMuteEnabled){
            try {
                configuration.userScreenNameMutePattern = Pattern.compile(defaultSharedPreferences.getString("userScreenNameMutePattern",""));
            } catch (PatternSyntaxException e){
                e.printStackTrace();
                configuration.isPatternUserScreenNameMuteEnabled = false;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        configuration.isPatternUserNameMuteEnabled = defaultSharedPreferences.getBoolean("patternUserNameMuteEnabled",false);
        if(configuration.isPatternUserNameMuteEnabled){
            try {
                configuration.userNameMutePattern = Pattern.compile(defaultSharedPreferences.getString("userNameMutePattern",""));
            } catch (PatternSyntaxException e){
                e.printStackTrace();
                configuration.isPatternUserNameMuteEnabled = false;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        configuration.isPatternTweetSourceMuteEnabled = defaultSharedPreferences.getBoolean("patternTweetSourceMuteEnabled",false);
        if(configuration.isPatternTweetSourceMuteEnabled){
            try {
                configuration.tweetSourceMutePattern = Pattern.compile(defaultSharedPreferences.getString("tweetSourceMutePattern",""));
            } catch (PatternSyntaxException e){
                e.printStackTrace();
                configuration.isPatternTweetSourceMuteEnabled = false;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        configuration.isTimelineImageLoad = Boolean.valueOf(defaultSharedPreferences.getString("isTimelineImageLoad","true"));

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        @AppCompatDelegate.NightMode
        int mode= AppCompatDelegate.MODE_NIGHT_NO;

        switch(defaultSharedPreferences.getString("nightModeType","mode_night_no_value")){

            case "mode_night_no":
                mode=AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "mode_night_auto":
                mode=AppCompatDelegate.MODE_NIGHT_AUTO;
                break;
            case "mode_night_follow_system":
                mode=AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
            case "mode_night_yes":
                mode=AppCompatDelegate.MODE_NIGHT_YES;
                break;
        }

        AppCompatDelegate.setDefaultNightMode(mode);

        accountsModel = new AccountsModel(getApplicationContext());

        String accountKey = defaultSharedPreferences.getString("AccountKey","-1");

        if (accountKey.equals("-1")) return;

        AccessToken accessToken = accountsModel.get(accountKey);

        if (accessToken==null)return;
        initTwitter(accessToken);

        super.onCreate();
    }

    public void initTwitter(@NonNull AccessToken accessToken){
        userId = accessToken.getUserId();
        clientType = accessToken.getType();
        twitter = createTwitterInstance(accessToken);
        GlobalApplication.accessToken = accessToken;
        userCache.prepare(this, accessToken);
        statusCache.prepare(this, accessToken);
        statusLimit = clientType == Type.TWITTER? 200: 40;
    }

    @NonNull
    public Twitter createTwitterInstance(@NonNull AccessToken accessToken){
        Twitter t;

        Configuration conf;

        if (accessToken.getType() == Type.TWITTER){
            conf = new ConfigurationBuilder()
                    .setTweetModeExtended(true)
                    .setOAuthConsumerKey(BuildConfig.CONSUMER_KEY)
                    .setOAuthConsumerSecret(BuildConfig.CONSUMER_SECRET)
                    .setOAuthAccessToken(accessToken.getToken())
                    .setOAuthAccessTokenSecret(accessToken.getTokenSecret())
                    .build();
            t = twitterCache.get(conf);

            if (t == null) {
                t = new TwitterFactory(conf).getInstance();
                twitterCache.put(conf, t);
            }
        } else {
            conf = new ConfigurationBuilder()
                    .setOAuthAccessToken(accessToken.getToken())
                    .setRestBaseURL(accessToken.getUrl())
                    .build();
            t = twitterCache.get(conf);

            if (t == null) {
                t = new MastodonTwitterImpl(
                        conf,
                        accessToken.getUserId(),
                        getOkHttpClient(conf.getHttpClientConfiguration()).newBuilder()
                );
                twitterCache.put(conf, t);
            }
        }

        return t;
    }

    @NonNull
    public static OkHttpClient getOkHttpClient(){
        return getOkHttpClient(twitter.getConfiguration().getHttpClientConfiguration());
    }

    @NonNull
    public static OkHttpClient getOkHttpClient(HttpClientConfiguration configuration){
        HttpClient httpClient = HttpClientFactory.getInstance(configuration);
        return ((AlternativeHttpClientImpl) httpClient).getOkHttpClient();
    }
}