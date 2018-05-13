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

package com.github.moko256.mastodon;

import com.sys1yagi.mastodon4j.api.entity.Attachment;
import com.sys1yagi.mastodon4j.api.entity.Status;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

/**
 * Created by moko256 on 17/10/03.
 *
 * @author moko256
 */

public class MTStatus implements twitter4j.Status{

    public Status status;

    public MTStatus(Status status){
        this.status = status;
    }

    @Override
    public Date getCreatedAt() {
        return MastodonTwitterImpl.parseDate(status.getCreatedAt());
    }

    @Override
    public long getId() {
        return status.getId();
    }

    @Override
    public String getText() {
        String spoilerText = status.getSpoilerText();
        String content = status.getContent();
        String br = "<br />";

        if (spoilerText.equals("")) {
            return content;
        } else {
            return spoilerText + br + content;
        }
    }

    @Override
    public int getDisplayTextRangeStart() {
        return 0;
    }

    @Override
    public int getDisplayTextRangeEnd() {
        return getText().length();
    }

    @Override
    public String getSource() {
        if (status.getApplication() != null) {
            //This null check is needed
            // because status.getApplication.getWebsite may be null though its return type is written non null.
            //Kotlin 1.2.10
            if (status.getApplication().getWebsite() != null && !status.getApplication().getWebsite().equals("")) {
                return "<a href='" + status.getApplication().getWebsite() + "'>" + status.getApplication().getName() + "</a>";
            } else {
                return status.getApplication().getName();
            }
        } else {
            return "unknown";
        }
    }

    @Override
    public boolean isTruncated() {
        return false;
    }

    @Override
    public long getInReplyToStatusId() {
        Long id = status.getInReplyToId();
        return id == null?-1:id;
    }

    @Override
    public long getInReplyToUserId() {
        Long id = status.getInReplyToAccountId();
        return id == null?-1:id;
    }

    @Override
    public String getInReplyToScreenName() {
        return (getInReplyToUserId() != -1)?"":null;
    }

    @Override
    public GeoLocation getGeoLocation() {
        return null;
    }

    @Override
    public Place getPlace() {
        return null;
    }

    @Override
    public boolean isFavorited() {
        return status.isFavourited();
    }

    @Override
    public boolean isRetweeted() {
        return status.isReblogged();
    }

    @Override
    public int getFavoriteCount() {
        return status.getFavouritesCount();
    }

    @Override
    public User getUser() {
        return new MTUser(status.getAccount());
    }

    @Override
    public boolean isRetweet() {
        return status.getReblog() != null;
    }

    @Override
    public twitter4j.Status getRetweetedStatus() {
        return isRetweet()?new MTStatus(status.getReblog()):null;
    }

    @Override
    public long[] getContributors() {
        return null;
    }

    @Override
    public int getRetweetCount() {
        return status.getReblogsCount();
    }

    @Override
    public boolean isRetweetedByMe() {
        return false;
    }

    @Override
    public long getCurrentUserRetweetId() {
        return -1;
    }

    @Override
    public boolean isPossiblySensitive() {
        return status.isSensitive();
    }

    @Override
    public String getLang() {
        return null;//status.getLanguage();
    }

    @Override
    public Scopes getScopes() {
        return null;
    }

    @Override
    public String[] getWithheldInCountries() {
        return null;
    }

    @Override
    public long getQuotedStatusId() {
        return -1;
    }

    @Override
    public twitter4j.Status getQuotedStatus() {
        return null;
    }

    @Override
    public int compareTo(@NotNull twitter4j.Status status) {
        return 0;
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        return new UserMentionEntity[0];
    }

    @Override
    public URLEntity[] getURLEntities() {
        return new URLEntity[0];
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return new HashtagEntity[0];
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        List<Attachment> medias = status.getMediaAttachments();
        MediaEntity[] mediaEntities = new MediaEntity[medias.size()];
        for (int i = 0; i < mediaEntities.length; i++) {
            Attachment media = medias.get(i);

            mediaEntities[i] = new MTMediaEntity(media);
        }
        return mediaEntities;
    }

    @Override
    public SymbolEntity[] getSymbolEntities() {
        return new SymbolEntity[0];
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return null;
    }

    @Override
    public int getAccessLevel() {
        return 0;
    }

    @Override
    public int hashCode() {
        return (int) getId();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (this == obj || obj instanceof Status && ((Status) obj).getId() == this.getId());
    }

    private static class MTMediaEntity implements MediaEntity {
        long id;
        String imageUrl;
        String videoUrl;
        String type;
        Variant[] variants;

        public MTMediaEntity(Attachment media) {
            id = media.getId();
            type = (media.getType().equals("gifv") ? "animated_gif" : media.getType());
            if (type.equals("video") || type.equals("animated_gif")) {
                imageUrl = media.getPreviewUrl();
                videoUrl = media.getUrl();
                variants = new Variant[1];
                variants[0] = new Variant() {
                    @Override
                    public int getBitrate() {
                        return 0;
                    }

                    @Override
                    public String getContentType() {
                        return "video/mp4";
                    }

                    @Override
                    public String getUrl() {
                        return videoUrl;
                    }
                };
            } else {
                imageUrl = media.getUrl();
            }
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getMediaURL() {
            return imageUrl;
        }

        @Override
        public String getMediaURLHttps() {
            return imageUrl;
        }

        @Override
        public Map<Integer, Size> getSizes() {
            return null;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public int getVideoAspectRatioWidth() {
            return 0;
        }

        @Override
        public int getVideoAspectRatioHeight() {
            return 0;
        }

        @Override
        public long getVideoDurationMillis() {
            return 0;
        }

        @Override
        public Variant[] getVideoVariants() {
            return variants;
        }

        @Override
        public String getExtAltText() {
            return null;
        }

        @Override
        public String getText() {
            return null;
        }

        @Override
        public String getURL() {
            return imageUrl;
        }

        @Override
        public String getExpandedURL() {
            return imageUrl;
        }

        @Override
        public String getDisplayURL() {
            return imageUrl;
        }

        @Override
        public int getStart() {
            return 0;
        }

        @Override
        public int getEnd() {
            return 0;
        }
    }
}
