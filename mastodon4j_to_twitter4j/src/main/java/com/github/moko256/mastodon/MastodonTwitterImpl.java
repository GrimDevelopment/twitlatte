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

package com.github.moko256.mastodon;

import com.google.gson.Gson;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.Pageable;
import com.sys1yagi.mastodon4j.api.Range;
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException;
import com.sys1yagi.mastodon4j.api.method.Accounts;
import com.sys1yagi.mastodon4j.api.method.Favourites;
import com.sys1yagi.mastodon4j.api.method.Public;
import com.sys1yagi.mastodon4j.api.method.Statuses;
import com.sys1yagi.mastodon4j.api.method.Timelines;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import twitter4j.AccountSettings;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.Friendship;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.api.DirectMessagesResources;
import twitter4j.api.FavoritesResources;
import twitter4j.api.FriendsFollowersResources;
import twitter4j.api.HelpResources;
import twitter4j.api.ListsResources;
import twitter4j.api.PlacesGeoResources;
import twitter4j.api.SavedSearchesResources;
import twitter4j.api.SearchResource;
import twitter4j.api.SpamReportingResource;
import twitter4j.api.SuggestedUsersResources;
import twitter4j.api.TimelinesResources;
import twitter4j.api.TrendsResources;
import twitter4j.api.TweetsResources;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.util.function.Consumer;

/**
 * Created by moko256 on 17/10/03.
 *
 * @author moko256
 */

public final class MastodonTwitterImpl implements Twitter {

    private static Gson gson;

    static {
        gson = new Gson();
    }

    public final MastodonClient client;

    private Configuration configuration;
    private long userId;

    /**
     * @param configuration Configuration (getRestBaseURL expects instance url)
     * @param builder OkHttpClient.Builder
     */
    public MastodonTwitterImpl(Configuration configuration, long userId, OkHttpClient.Builder builder){
        this.configuration = configuration;
        this.userId = userId;
        client = new MastodonClient.Builder(configuration.getRestBaseURL(), builder, gson)
                .accessToken(configuration.getOAuthAccessToken())
                .build();
    }

    @Override
    public TimelinesResources timelines() {
        return this;
    }

    @Override
    public TweetsResources tweets() {
        return this;
    }

    @Override
    public SearchResource search() {
        return this;
    }

    @Override
    public DirectMessagesResources directMessages() {
        return this;
    }

    @Override
    public FriendsFollowersResources friendsFollowers() {
        return this;
    }

    @Override
    public UsersResources users() {
        return this;
    }

    @Override
    public SuggestedUsersResources suggestedUsers() {
        return this;
    }

    @Override
    public FavoritesResources favorites() {
        return this;
    }

    @Override
    public ListsResources list() {
        return null;
    }

    @Override
    public SavedSearchesResources savedSearches() {
        return null;
    }

    @Override
    public PlacesGeoResources placesGeo() {
        return null;
    }

    @Override
    public TrendsResources trends() {
        return null;
    }

    @Override
    public SpamReportingResource spamReporting() {
        return null;
    }

    @Override
    public HelpResources help() {
        return null;
    }

    @Override
    public String getScreenName() throws TwitterException, IllegalStateException {
        return verifyCredentials().getScreenName();
    }

    @Override
    public long getId() throws TwitterException, IllegalStateException {
        return verifyCredentials().getId();
    }

    @Override
    public void addRateLimitStatusListener(RateLimitStatusListener rateLimitStatusListener) {

    }

    @Override
    public void onRateLimitStatus(Consumer<RateLimitStatusEvent> consumer) {

    }

    @Override
    public void onRateLimitReached(Consumer<RateLimitStatusEvent> consumer) {

    }

    @Override
    public Authorization getAuthorization() {
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<DirectMessage> getSentDirectMessages() throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws TwitterException {
        return null;
    }

    @Override
    public DirectMessage showDirectMessage(long l) throws TwitterException {
        return null;
    }

    @Override
    public DirectMessage destroyDirectMessage(long l) throws TwitterException {
        return null;
    }

    @Override
    public DirectMessage sendDirectMessage(long l, String s) throws TwitterException {
        return null;
    }

    @Override
    public DirectMessage sendDirectMessage(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public InputStream getDMImageAsStream(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> getFavorites() throws TwitterException {
        return getFavorites(new Paging(-1L));
    }

    @Override
    public ResponseList<Status> getFavorites(long l) throws TwitterException {
        return getFavorites(l, new Paging(-1L));
    }

    @Override
    public ResponseList<Status> getFavorites(String s) throws TwitterException {
        return new MTResponseList<>();
    }

    @Override
    public ResponseList<Status> getFavorites(Paging paging) throws TwitterException {
        try {
            return MTResponseList.convert(new Favourites(client).getFavourites(MTRangeConverter.convert(paging)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public ResponseList<Status> getFavorites(long l, Paging paging) throws TwitterException {
        if (l == userId) {
            return getFavorites(paging);
        } else {
            return new MTResponseList<>();
        }
    }

    @Override
    public ResponseList<Status> getFavorites(String s, Paging paging) throws TwitterException {
        return new MTResponseList<>();
    }

    @Override
    public Status createFavorite(long l) throws TwitterException {
        try {
            return new MTStatus(new Statuses(client).postFavourite(l).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public Status destroyFavorite(long l) throws TwitterException {
        try {
            return new MTStatus(new Statuses(client).postUnfavourite(l).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public IDs getNoRetweetsFriendships() throws TwitterException {
        return null;
    }

    @Override
    public IDs getFriendsIDs(long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFriendsIDs(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFriendsIDs(long l, long l1, int i) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFriendsIDs(String s, long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFriendsIDs(String s, long l, int i) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFollowersIDs(long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFollowersIDs(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFollowersIDs(long l, long l1, int i) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFollowersIDs(String s, long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getFollowersIDs(String s, long l, int i) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Friendship> lookupFriendships(long... longs) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Friendship> lookupFriendships(String... strings) throws TwitterException {
        return null;
    }

    @Override
    public IDs getIncomingFriendships(long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getOutgoingFriendships(long l) throws TwitterException {
        return null;
    }

    @Override
    public User createFriendship(long l) throws TwitterException {
        try {
            new Accounts(client).postFollow(l).execute();
            return null;
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public User createFriendship(String s) throws TwitterException {
        return null;
    }

    @Override
    public User createFriendship(long l, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public User createFriendship(String s, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public User destroyFriendship(long l) throws TwitterException {
        try {
            new Accounts(client).postUnFollow(l).execute();
            return null;
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public User destroyFriendship(String s) throws TwitterException {
        return null;
    }

    @Override
    public Relationship updateFriendship(long l, boolean b, boolean b1) throws TwitterException {
        return null;
    }

    @Override
    public Relationship updateFriendship(String s, boolean b, boolean b1) throws TwitterException {
        return null;
    }

    @Override
    public Relationship showFriendship(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public Relationship showFriendship(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFriendsList(long l, long l1) throws TwitterException {
        try {
            return MTPageableResponseList.convert(new Accounts(client).getFollowing(l, new Range(l1 != -1? l1: null)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public PagableResponseList<User> getFriendsList(long l, long l1, int i) throws TwitterException {
        try {
            return MTPageableResponseList.convert(new Accounts(client).getFollowing(l, new Range(l1 != -1? l1: null, null, i)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public PagableResponseList<User> getFriendsList(String s, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFriendsList(String s, long l, int i) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFriendsList(long l, long l1, int i, boolean b, boolean b1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFriendsList(String s, long l, int i, boolean b, boolean b1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFollowersList(long l, long l1) throws TwitterException {
        try {
            return MTPageableResponseList.convert(new Accounts(client).getFollowers(l, new Range(l1 != -1? l1: null)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public PagableResponseList<User> getFollowersList(String s, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFollowersList(long l, long l1, int i) throws TwitterException {
        try {
            return MTPageableResponseList.convert(new Accounts(client).getFollowers(l, new Range(l1 != -1? l1: null, null, i)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public PagableResponseList<User> getFollowersList(String s, long l, int i) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFollowersList(long l, long l1, int i, boolean b, boolean b1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getFollowersList(String s, long l, int i, boolean b, boolean b1) throws TwitterException {
        return null;
    }

    @Override
    public TwitterAPIConfiguration getAPIConfiguration() throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Language> getLanguages() throws TwitterException {
        return null;
    }

    @Override
    public String getPrivacyPolicy() throws TwitterException {
        return null;
    }

    @Override
    public String getTermsOfService() throws TwitterException {
        return null;
    }

    @Override
    public Map<String, RateLimitStatus> getRateLimitStatus() throws TwitterException {
        return null;
    }

    @Override
    public Map<String, RateLimitStatus> getRateLimitStatus(String... strings) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<UserList> getUserLists(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<UserList> getUserLists(String s, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<UserList> getUserLists(long l) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<UserList> getUserLists(long l, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> getUserListStatuses(long l, Paging paging) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> getUserListStatuses(long l, String s, Paging paging) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> getUserListStatuses(String s, String s1, Paging paging) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListMember(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListMember(long l, String s) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListMembers(long l, String[] strings) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListMembers(long l, long[] longs) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListMembers(String s, String s1, String[] strings) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListMember(long l, String s, long l1) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListMember(String s, String s1, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(int i, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long l, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String s, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String s, int i, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String s, long l, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(String s, int i, long l, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long l, long l1, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListMemberships(long l, int i, long l1, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long l, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long l, int i, long l1, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long l, String s, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long l, String s, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(long l, String s, int i, long l1, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(String s, String s1, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(String s, String s1, int i, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListSubscribers(String s, String s1, int i, long l, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListSubscription(long l) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListSubscription(long l, String s) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListSubscription(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public User showUserListSubscription(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public User showUserListSubscription(long l, String s, long l1) throws TwitterException {
        return null;
    }

    @Override
    public User showUserListSubscription(String s, String s1, long l) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListSubscription(long l) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListSubscription(long l, String s) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserListSubscription(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMembers(long l, long... longs) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMembers(long l, String s, long... longs) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMembers(String s, String s1, long... longs) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMembers(long l, String... strings) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMembers(long l, String s, String... strings) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMembers(String s, String s1, String... strings) throws TwitterException {
        return null;
    }

    @Override
    public User showUserListMembership(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public User showUserListMembership(long l, String s, long l1) throws TwitterException {
        return null;
    }

    @Override
    public User showUserListMembership(String s, String s1, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long l, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long l, int i, long l1, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long l, String s, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long l, String s, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(long l, String s, int i, long l1, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String s, String s1, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String s, String s1, int i, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getUserListMembers(String s, String s1, int i, long l, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMember(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMember(long l, String s, long l1) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserListMember(String s, String s1, long l) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserList(long l) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserList(long l, String s) throws TwitterException {
        return null;
    }

    @Override
    public UserList destroyUserList(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public UserList updateUserList(long l, String s, boolean b, String s1) throws TwitterException {
        return null;
    }

    @Override
    public UserList updateUserList(long l, String s, String s1, boolean b, String s2) throws TwitterException {
        return null;
    }

    @Override
    public UserList updateUserList(String s, String s1, String s2, boolean b, String s3) throws TwitterException {
        return null;
    }

    @Override
    public UserList createUserList(String s, boolean b, String s1) throws TwitterException {
        return null;
    }

    @Override
    public UserList showUserList(long l) throws TwitterException {
        return null;
    }

    @Override
    public UserList showUserList(long l, String s) throws TwitterException {
        return null;
    }

    @Override
    public UserList showUserList(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(String s, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(String s, int i, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListSubscriptions(long l, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(String s, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(String s, int i, long l) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<UserList> getUserListsOwnerships(long l, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public Place getGeoDetails(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Place> reverseGeoCode(GeoQuery geoQuery) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Place> searchPlaces(GeoQuery geoQuery) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Place> getSimilarPlaces(GeoLocation geoLocation, String s, String s1, String s2) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
        return null;
    }

    @Override
    public SavedSearch showSavedSearch(long l) throws TwitterException {
        return null;
    }

    @Override
    public SavedSearch createSavedSearch(String s) throws TwitterException {
        return null;
    }

    @Override
    public SavedSearch destroySavedSearch(long l) throws TwitterException {
        return null;
    }

    @Override
    public QueryResult search(Query query) throws TwitterException {
        Pageable<com.sys1yagi.mastodon4j.api.entity.Status> pageable = null;
        try {
            pageable = new Public(client).
                    getFederatedTag(query.getQuery(), MTRangeConverter.convert(query))
                    .execute();
        } catch (Mastodon4jRequestException e) {
            e.printStackTrace();
        }

        long previous;
        long next;

        if (pageable == null) return null;

        if (pageable.getLink() != null) {
            next = pageable.getLink().getMaxId();
            previous = pageable.getLink().getSinceId();
        } else {
            next = -1;
            previous = -1;
        }

        List<com.sys1yagi.mastodon4j.api.entity.Status> part = pageable.getPart();
        Pageable<com.sys1yagi.mastodon4j.api.entity.Status> finalPageable = pageable;
        return new QueryResult() {
            @Override
            public long getSinceId() {
                return previous;
            }

            @Override
            public long getMaxId() {
                return next;
            }

            @Override
            public String getRefreshURL() {
                return null;
            }

            @Override
            public int getCount() {
                return part.size();
            }

            @Override
            public double getCompletedIn() {
                return 0;
            }

            @Override
            public String getQuery() {
                return query.getQuery();
            }

            @Override
            public List<Status> getTweets() {
                return MTResponseList.convert(finalPageable);
            }

            @Override
            public Query nextQuery() {
                return new Query(query.getQuery()).sinceId(next);
            }

            @Override
            public boolean hasNext() {
                return next != -1;
            }

            @Override
            public RateLimitStatus getRateLimitStatus() {
                return null;
            }

            @Override
            public int getAccessLevel() {
                return 0;
            }
        };
    }

    @Override
    public User reportSpam(long l) throws TwitterException {
        return null;
    }

    @Override
    public User reportSpam(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<User> getUserSuggestions(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<User> getMemberSuggestions(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> getMentionsTimeline() throws TwitterException {
        return new MTResponseList<>();
    }

    @Override
    public ResponseList<Status> getMentionsTimeline(Paging paging) throws TwitterException {
        return new MTResponseList<>();
    }

    @Override
    public ResponseList<Status> getUserTimeline(String s, Paging paging) throws TwitterException {
        Accounts accounts = new Accounts(client);
        try {
            return MTResponseList.convert(accounts.getStatuses(accounts.getAccountSearch(s, 1).execute().get(0).getId(), false, MTRangeConverter.convert(paging)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public ResponseList<Status> getUserTimeline(long l, Paging paging) throws TwitterException {
        Accounts accounts = new Accounts(client);
        try {
            return MTResponseList.convert(accounts.getStatuses(l, false, MTRangeConverter.convert(paging)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public ResponseList<Status> getUserTimeline(String s) throws TwitterException {
        return getUserTimeline(s, new Paging(-1L));
    }

    @Override
    public ResponseList<Status> getUserTimeline(long l) throws TwitterException {
        return getUserTimeline(l, new Paging(-1L));
    }

    @Override
    public ResponseList<Status> getUserTimeline() throws TwitterException {
        return getUserTimeline(new Paging(-1L));
    }

    @Override
    public ResponseList<Status> getUserTimeline(Paging paging) throws TwitterException {
        return getUserTimeline(userId, paging);
    }

    @Override
    public ResponseList<Status> getHomeTimeline() throws TwitterException {
        return getHomeTimeline(new Paging(-1L));
    }

    @Override
    public ResponseList<Status> getHomeTimeline(Paging paging) throws TwitterException {
        try {
            return MTResponseList.convert(new Timelines(client).getHome(MTRangeConverter.convert(paging)).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return null;
    }

    @Override
    public Trends getPlaceTrends(int i) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Location> getClosestTrends(GeoLocation geoLocation) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> getRetweets(long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getRetweeterIds(long l, long l1) throws TwitterException {
        return null;
    }

    @Override
    public IDs getRetweeterIds(long l, int i, long l1) throws TwitterException {
        return null;
    }

    @Override
    public Status showStatus(long l) throws TwitterException {
        try {
            return new MTStatus(new Statuses(client).getStatus(l).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public Status destroyStatus(long l) throws TwitterException {
        try {
            new Statuses(client).deleteStatus(l);
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
        return null;
    }

    @Override
    public Status updateStatus(String s) throws TwitterException {
        return null;
    }

    @Override
    public Status updateStatus(StatusUpdate statusUpdate) throws TwitterException {
        return null;
    }

    @Override
    public Status retweetStatus(long l) throws TwitterException {
        try {
            return new MTStatus(new Statuses(client).postReblog(l).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public Status unRetweetStatus(long statusId) throws TwitterException {
        try {
            return new MTStatus(new Statuses(client).postUnreblog(statusId).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public OEmbed getOEmbed(OEmbedRequest oEmbedRequest) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<Status> lookup(long... longs) throws TwitterException {
        ResponseList<Status> statuses = new MTResponseList<>(longs.length);
        try {
            for (long l : longs) {
                statuses.add(new MTStatus(new Statuses(client).getStatus(l).execute()));
            }
        } catch (Mastodon4jRequestException e){
            throw new MTException(e);
        }
        return statuses;
    }

    @Override
    public UploadedMedia uploadMedia(File file) throws TwitterException {
        return null;
    }

    @Override
    public UploadedMedia uploadMedia(String s, InputStream inputStream) throws TwitterException {
        return null;
    }

    @Override
    public AccountSettings getAccountSettings() throws TwitterException {
        return null;
    }

    @Override
    public User verifyCredentials() throws TwitterException {
        try {
            return new MTUser(new Accounts(client).getVerifyCredentials().execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public AccountSettings updateAccountSettings(Integer integer, Boolean aBoolean, String s, String s1, String s2, String s3) throws TwitterException {
        return null;
    }

    @Override
    public User updateProfile(String s, String s1, String s2, String s3) throws TwitterException {
        return null;
    }

    @Override
    public User updateProfileBackgroundImage(File file, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public User updateProfileBackgroundImage(InputStream inputStream, boolean b) throws TwitterException {
        return null;
    }

    @Override
    public User updateProfileColors(String s, String s1, String s2, String s3, String s4) throws TwitterException {
        return null;
    }

    @Override
    public User updateProfileImage(File file) throws TwitterException {
        return null;
    }

    @Override
    public User updateProfileImage(InputStream inputStream) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getBlocksList() throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getBlocksList(long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getBlocksIDs() throws TwitterException {
        return null;
    }

    @Override
    public IDs getBlocksIDs(long l) throws TwitterException {
        return null;
    }

    @Override
    public User createBlock(long l) throws TwitterException {
        try {
            new Accounts(client).postBlock(l).execute();
        } catch (Mastodon4jRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User createBlock(String s) throws TwitterException {
        return null;
    }

    @Override
    public User destroyBlock(long l) throws TwitterException {
        try {
            new Accounts(client).postUnblock(l).execute();
        } catch (Mastodon4jRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User destroyBlock(String s) throws TwitterException {
        return null;
    }

    @Override
    public PagableResponseList<User> getMutesList(long l) throws TwitterException {
        return null;
    }

    @Override
    public IDs getMutesIDs(long l) throws TwitterException {
        return null;
    }

    @Override
    public User createMute(long l) throws TwitterException {
        try {
            new Accounts(client).postMute(l).execute();
        } catch (Mastodon4jRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User createMute(String s) throws TwitterException {
        return null;
    }

    @Override
    public User destroyMute(long l) throws TwitterException {
        try {
            new Accounts(client).postUnmute(l).execute();
        } catch (Mastodon4jRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User destroyMute(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<User> lookupUsers(long... longs) throws TwitterException {
        ResponseList<User> l = new MTResponseList<>(longs.length);
        for (long id: longs){
            l.add(showUser(id));
        }
        return l;
    }

    @Override
    public ResponseList<User> lookupUsers(String... strings) throws TwitterException {
        ResponseList<User> l = new MTResponseList<>(strings.length);
        for (String id: strings){
            l.add(showUser(id));
        }
        return l;
    }

    @Override
    public User showUser(long l) throws TwitterException {
        try {
            return new MTUser(new Accounts(client).getAccount(l).execute());
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public User showUser(String s) throws TwitterException {
        try {
            return new MTUser(new Accounts(client).getAccountSearch(s, 1).execute().get(0));
        } catch (Mastodon4jRequestException e) {
            throw new MTException(e);
        }
    }

    @Override
    public ResponseList<User> searchUsers(String s, int i) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<User> getContributees(long l) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<User> getContributees(String s) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<User> getContributors(long l) throws TwitterException {
        return null;
    }

    @Override
    public ResponseList<User> getContributors(String s) throws TwitterException {
        return null;
    }

    @Override
    public void removeProfileBanner() throws TwitterException {

    }

    @Override
    public void updateProfileBanner(File file) throws TwitterException {

    }

    @Override
    public void updateProfileBanner(InputStream inputStream) throws TwitterException {

    }

    @Override
    public OAuth2Token getOAuth2Token() throws TwitterException {
        return null;
    }

    @Override
    public void setOAuth2Token(OAuth2Token oAuth2Token) {

    }

    @Override
    public void invalidateOAuth2Token() throws TwitterException {

    }

    @Override
    public void setOAuthConsumer(String s, String s1) {

    }

    @Override
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return null;
    }

    @Override
    public RequestToken getOAuthRequestToken(String s) throws TwitterException {
        return null;
    }

    @Override
    public RequestToken getOAuthRequestToken(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public RequestToken getOAuthRequestToken(String s, String s1, String s2) throws TwitterException {
        return null;
    }

    @Override
    public AccessToken getOAuthAccessToken() throws TwitterException {
        return null;
    }

    @Override
    public AccessToken getOAuthAccessToken(String s) throws TwitterException {
        return null;
    }

    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        return null;
    }

    @Override
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String s) throws TwitterException {
        return null;
    }

    @Override
    public AccessToken getOAuthAccessToken(String s, String s1) throws TwitterException {
        return null;
    }

    @Override
    public void setOAuthAccessToken(AccessToken accessToken) {

    }
}
