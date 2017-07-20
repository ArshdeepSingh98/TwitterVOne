package com.example.arshdeep.twittervone.Network;

import com.example.arshdeep.twittervone.Tweet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Arshdeep on 7/18/2017.
 */

public interface ApiInterface {
//    @GET("?screen_name={name}")
//    Call <ArrayList<Tweet>> getTweets(@Path("name") String name);

    @GET("statuses/user_timeline.json")
    Call <ArrayList<HomeTweetResponse>> getUserTimeline(@Query("screen_name")String screenName,@Query("count")long count);


    @GET("statuses/home_timeline.json")
    Call <ArrayList<HomeTweetResponse>> getHomeTimeline(@Query("screen_name")String screenName,@Query("count")long count);

    @GET("users/show.json")
    Call <UserResponse> getUserData(@Query("screen_name")String screenName);

    @GET("favorites/list.json")
    Call <ArrayList<HomeTweetResponse>> getFavTweets(@Query("screen_name")String screenName);

    @GET("search/tweets.json")
    Call <SearchTweetResponse> getSearchTweets(@Query("q")String qry);

    //@Header("Authorization")String auth
//    @FormUrlEncoded
//    Call <ArrayList<HomeTweetResponse>> getTweets(@Field("oauth_consumer_key")String consumerKey,
//                                                  @Field("oauth_nonce")String nonce,
//                                                  @Field("oauth_signature")String sig,
//                                                  @Field("oauth_signature_method")String sig_method,
//                                                  @Field("oauth_timestamp")String timestamp,
//                                                  @Field("oauth_token")String token,
//                                                  @Field("oauth_version")String version);
}
