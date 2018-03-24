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

package com.github.moko256.twicalico.config;

import java.util.regex.Pattern;

/**
 * Created by moko256 on 2017/02/04.
 *
 * @author moko256
 */

public class AppConfiguration {

    private boolean patternTweetMuteEnabled;
    private Pattern tweetMutePattern;

    private boolean patternTweetMuteShowOnlyImageEnabled;
    private Pattern tweetMuteShowOnlyImagePattern;

    private boolean patternUserScreenNameMuteEnabled;
    private Pattern userScreenNameMutePattern;

    private boolean patternUserNameMuteEnabled;
    private Pattern userNameMutePattern;

    private boolean patternTweetSourceMuteEnabled;
    private Pattern tweetSourceMutePattern;

    private boolean isTimelineImageLoad;


    public boolean isPatternTweetMuteEnabled() {
        return patternTweetMuteEnabled;
    }

    public void setPatternTweetMuteEnabled(boolean patternTweetMuteEnabled) {
        this.patternTweetMuteEnabled = patternTweetMuteEnabled;
    }

    public Pattern getTweetMutePattern() {
        return tweetMutePattern;
    }

    public void setTweetMutePattern(Pattern tweetMutePattern) {
        this.tweetMutePattern = tweetMutePattern;
    }

    public boolean isPatternTweetMuteShowOnlyImageEnabled() {
        return patternTweetMuteShowOnlyImageEnabled;
    }

    public void setPatternTweetMuteShowOnlyImageEnabled(boolean patternTweetMuteShowOnlyImageEnabled) {
        this.patternTweetMuteShowOnlyImageEnabled = patternTweetMuteShowOnlyImageEnabled;
    }

    public Pattern getTweetMuteShowOnlyImagePattern() {
        return tweetMuteShowOnlyImagePattern;
    }

    public void setTweetMuteShowOnlyImagePattern(Pattern tweetMuteShowOnlyImagePattern) {
        this.tweetMuteShowOnlyImagePattern = tweetMuteShowOnlyImagePattern;
    }

    public boolean isPatternUserScreenNameMuteEnabled() {
        return patternUserScreenNameMuteEnabled;
    }

    public void setPatternUserScreenNameMuteEnabled(boolean patternUserScreenNameMuteEnabled) {
        this.patternUserScreenNameMuteEnabled = patternUserScreenNameMuteEnabled;
    }

    public Pattern getUserScreenNameMutePattern() {
        return userScreenNameMutePattern;
    }

    public void setUserScreenNameMutePattern(Pattern userScreenNameMutePattern) {
        this.userScreenNameMutePattern = userScreenNameMutePattern;
    }

    public boolean isPatternUserNameMuteEnabled() {
        return patternUserNameMuteEnabled;
    }

    public void setPatternUserNameMuteEnabled(boolean patternUserNameMuteEnabled) {
        this.patternUserNameMuteEnabled = patternUserNameMuteEnabled;
    }

    public Pattern getUserNameMutePattern() {
        return userNameMutePattern;
    }

    public void setUserNameMutePattern(Pattern userNameMutePattern) {
        this.userNameMutePattern = userNameMutePattern;
    }

    public boolean isPatternTweetSourceMuteEnabled() {
        return patternTweetSourceMuteEnabled;
    }

    public void setPatternTweetSourceMuteEnabled(boolean patternTweetSourceMuteEnabled) {
        this.patternTweetSourceMuteEnabled = patternTweetSourceMuteEnabled;
    }

    public Pattern getTweetSourceMutePattern() {
        return tweetSourceMutePattern;
    }

    public void setTweetSourceMutePattern(Pattern tweetSourceMutePattern) {
        this.tweetSourceMutePattern = tweetSourceMutePattern;
    }

    public void setTimelineImageLoad(boolean isTimelineImageLoad) {
        this.isTimelineImageLoad = isTimelineImageLoad;
    }

    public boolean isTimelineImageLoad() {
        return isTimelineImageLoad;
    }
}
