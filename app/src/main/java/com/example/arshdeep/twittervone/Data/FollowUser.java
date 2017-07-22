package com.example.arshdeep.twittervone.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arshdeep on 7/22/2017.
 */

public class FollowUser {
    private String name;
    private String screen_name;
    private String profile_image_url;
    private String description;
    private boolean verified;

    public FollowUser(String name, String screen_name, String profile_image_url, String description, boolean verified) {
        this.name = name;
        this.screen_name = screen_name;
        this.profile_image_url = profile_image_url;
        this.description = description;
        this.verified = verified;
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
