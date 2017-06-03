/*
 * Copyright 2016 The twicalico authors
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

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;

import java.util.Date;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;

/**
 * Created by moko256 on 2017/03/17.
 *
 * @author moko256
 */

public class CachedUsersSQLiteOpenHelperTest extends ApplicationTestCase<Application> {

    private static final long TEST_DUMMY_USER_ID = 1L;
    private static final String TEST_DUMMY_USER_NAME_0 = "0";
    private static final String TEST_DUMMY_USER_NAME_1 = "1";

    public CachedUsersSQLiteOpenHelperTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        CachedUsersSQLiteOpenHelper helper = new CachedUsersSQLiteOpenHelper(new RenamingDelegatingContext(getContext(), "test_"));

        helper.addCachedUser(new TestUser(TEST_DUMMY_USER_ID, TEST_DUMMY_USER_NAME_0));
        User addedStatusResult = helper.getCachedUser(TEST_DUMMY_USER_ID);

        assertEquals(addedStatusResult.getName(), TEST_DUMMY_USER_NAME_0);


        helper.addCachedUser(new TestUser(TEST_DUMMY_USER_ID, TEST_DUMMY_USER_NAME_1));
        User updatedStatusResult = helper.getCachedUser(TEST_DUMMY_USER_ID);

        assertEquals(updatedStatusResult.getName(), TEST_DUMMY_USER_NAME_1);


        helper.deleteCachedUser(TEST_DUMMY_USER_ID);

        assertEquals(helper.getCachedUser(TEST_DUMMY_USER_ID), null);

        helper.close();
    }

    private static class TestUser implements User{

        private final long id;
        private final String name;

        TestUser(final long testId, final String testName){
            id = testId;
            name = testName;
        }


        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getEmail() {
            return null;
        }

        @Override
        public String getScreenName() {
            return null;
        }

        @Override
        public String getLocation() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public boolean isContributorsEnabled() {
            return false;
        }

        @Override
        public String getProfileImageURL() {
            return null;
        }

        @Override
        public String getBiggerProfileImageURL() {
            return null;
        }

        @Override
        public String getMiniProfileImageURL() {
            return null;
        }

        @Override
        public String getOriginalProfileImageURL() {
            return null;
        }

        @Override
        public String getProfileImageURLHttps() {
            return null;
        }

        @Override
        public String getBiggerProfileImageURLHttps() {
            return null;
        }

        @Override
        public String getMiniProfileImageURLHttps() {
            return null;
        }

        @Override
        public String getOriginalProfileImageURLHttps() {
            return null;
        }

        @Override
        public boolean isDefaultProfileImage() {
            return false;
        }

        @Override
        public String getURL() {
            return null;
        }

        @Override
        public boolean isProtected() {
            return false;
        }

        @Override
        public int getFollowersCount() {
            return 0;
        }

        @Override
        public Status getStatus() {
            return null;
        }

        @Override
        public String getProfileBackgroundColor() {
            return null;
        }

        @Override
        public String getProfileTextColor() {
            return null;
        }

        @Override
        public String getProfileLinkColor() {
            return null;
        }

        @Override
        public String getProfileSidebarFillColor() {
            return null;
        }

        @Override
        public String getProfileSidebarBorderColor() {
            return null;
        }

        @Override
        public boolean isProfileUseBackgroundImage() {
            return false;
        }

        @Override
        public boolean isDefaultProfile() {
            return false;
        }

        @Override
        public boolean isShowAllInlineMedia() {
            return false;
        }

        @Override
        public int getFriendsCount() {
            return 0;
        }

        @Override
        public Date getCreatedAt() {
            return new Date(12345);
        }

        @Override
        public int getFavouritesCount() {
            return 0;
        }

        @Override
        public int getUtcOffset() {
            return 0;
        }

        @Override
        public String getTimeZone() {
            return null;
        }

        @Override
        public String getProfileBackgroundImageURL() {
            return null;
        }

        @Override
        public String getProfileBackgroundImageUrlHttps() {
            return null;
        }

        @Override
        public String getProfileBannerURL() {
            return null;
        }

        @Override
        public String getProfileBannerRetinaURL() {
            return null;
        }

        @Override
        public String getProfileBannerIPadURL() {
            return null;
        }

        @Override
        public String getProfileBannerIPadRetinaURL() {
            return null;
        }

        @Override
        public String getProfileBannerMobileURL() {
            return null;
        }

        @Override
        public String getProfileBannerMobileRetinaURL() {
            return null;
        }

        @Override
        public boolean isProfileBackgroundTiled() {
            return false;
        }

        @Override
        public String getLang() {
            return null;
        }

        @Override
        public int getStatusesCount() {
            return 0;
        }

        @Override
        public boolean isGeoEnabled() {
            return false;
        }

        @Override
        public boolean isVerified() {
            return false;
        }

        @Override
        public boolean isTranslator() {
            return false;
        }

        @Override
        public int getListedCount() {
            return 0;
        }

        @Override
        public boolean isFollowRequestSent() {
            return false;
        }

        @Override
        public URLEntity[] getDescriptionURLEntities() {
            return new URLEntity[0];
        }

        @Override
        public URLEntity getURLEntity() {
            return null;
        }

        @Override
        public String[] getWithheldInCountries() {
            return new String[0];
        }

        @Override
        public int compareTo(@NonNull User o) {
            return 0;
        }

        @Override
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }

        @Override
        public int getAccessLevel() {
            return 0;
        }
    }
}