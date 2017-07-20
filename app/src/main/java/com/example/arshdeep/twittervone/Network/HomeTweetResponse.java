package com.example.arshdeep.twittervone.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Arshdeep on 7/19/2017.
 */

public class HomeTweetResponse {
    @SerializedName("id_str")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
    @SerializedName("entities")
    public ENTITIES entities;

    public static class ENTITIES{
        @SerializedName("urls")
        public URLS urls;

        public URLS getUrls() {
            return urls;
        }

        public void setUrls(URLS urls) {
            this.urls = urls;
        }
    }

    public static class URLS{
        ArrayList<UR> urlList;

        public ArrayList<UR> getUrlList() {
            return urlList;
        }

        public void setUrlList(ArrayList<UR> urlList) {
            this.urlList = urlList;
        }
    }

    public class UR{
        @SerializedName("expanded_url")
        public String expanded_url;

        public String getExpanded_url() {
            return expanded_url;
        }

        public void setExpanded_url(String expanded_url) {
            this.expanded_url = expanded_url;
        }
    }
`   */
    /*
    public String text;
    public UserObj user;

    public static class UserObj{
        private long id;
        private String name;
        private String screen_name;
        private String location;
        private String description;
        private long followers_count;
        private long friends_count;
        private String profile_image_url;

        public long getId() {
            return id;
        }
8
        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getFollowers_count() {
            return followers_count;
        }

        public void setFollowers_count(long followers_count) {
            this.followers_count = followers_count;
        }

        public long getFriends_count() {
            return friends_count;
        }

        public void setFriends_count(long friends_count) {
            this.friends_count = friends_count;
        }

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }
    }
    */
}
