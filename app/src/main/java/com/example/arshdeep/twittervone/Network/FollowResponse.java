package com.example.arshdeep.twittervone.Network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arshdeep on 7/21/2017.
 */

public class FollowResponse {
    @SerializedName("users")
    private List<USER> users;

    public List<USER> getUsers() {
        return users;
    }

    public void setUsers(List<USER> users) {
        this.users = users;
    }

    public static class USER {

        @SerializedName("name")
        String name;

        @SerializedName("screen_name")
        String screen_name;

        @SerializedName("profile_image_url")
        String profile_image_url;

        @SerializedName("description")
        String description;

        @SerializedName("verified")
        boolean verified;

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

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

    }
}
