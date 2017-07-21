package com.example.arshdeep.twittervone.Network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arshdeep on 7/20/2017.
 */

public class UserResponse {
    @SerializedName("profile_image_url")
    private String profile_image_url;

    @SerializedName("statuses_count")
    private long tweetsCount;

    @SerializedName("favourites_count")
    private long likesCount;

    @SerializedName("friends_count")
    private long followingCount;

    @SerializedName("profile_background_color")
    private String bgColor;

    @SerializedName("profile_background_image_url")
    private String bgImage;

    @SerializedName("profile_banner_url")
    private String bannerImage;

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public long getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(long tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }
}
