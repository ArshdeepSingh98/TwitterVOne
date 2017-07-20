package com.example.arshdeep.twittervone.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Arshdeep on 7/20/2017.
 */

public class SearchTweetResponse {
    @SerializedName("statuses")
    ArrayList <HomeTweetResponse> searchList;

    public ArrayList<HomeTweetResponse> getSearchList() {
        return searchList;
    }

    public void setSearchList(ArrayList<HomeTweetResponse> searchList) {
        this.searchList = searchList;
    }
}
