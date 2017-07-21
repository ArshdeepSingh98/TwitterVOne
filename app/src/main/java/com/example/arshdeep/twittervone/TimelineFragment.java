package com.example.arshdeep.twittervone;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.arshdeep.twittervone.Network.ApiInterface;
import com.example.arshdeep.twittervone.Network.HomeTweetResponse;
import com.example.arshdeep.twittervone.Network.OAuthInterceptor;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.persistence.PersistenceStrategy;
import com.twitter.sdk.android.core.models.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Arshdeep on 7/18/2017.
 */

public class TimelineFragment extends Fragment {

    String token,secret;
    String screen_name,mUserName;
    long id,userId;
    ListView timelineView;
    LinearLayout linearLayoutTweetHolder;
    SwipeRefreshLayout swipeRefreshLayout;
    Boolean firstTime = true;

    //TODO : use custom adapter and Recycler List
    ListAdapter timelineAdapter;
    ArrayList < String > tweet_text;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser){
//            if(firstTime){
//                Log.i("Visible","first");
//            }else {
//                linearLayoutTweetHolder.removeAllViews();
//                fetchTimeline();
////                ((ProfileActivity)getActivity()).toolbar.setTitle("Home");
//
//            }
//        }
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timeline , container , false);

        linearLayoutTweetHolder = (LinearLayout) v.findViewById(R.id.linearLayoutTweetHolder);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

//        ((ProfileActivity)getActivity()).toolbar.setTitle("Home");


        firstTime = false;

        Bundle b = getArguments();
        if(b != null){
            token = b.getString("token");
            secret = b.getString("secret");
            mUserName = b.getString("userName");
            id = b.getLong("id");
            userId = b.getLong("userId");
        }
        fetchTimeline();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                linearLayoutTweetHolder.removeAllViews();
                fetchTimeline();
            }
        });

//        tweet_text = new ArrayList<>();
//        timelineView = (ListView) v.findViewById(R.id.timelineView);
//        timelineAdapter = new ArrayAdapter< String >(v.getContext() ,android.R.layout.simple_list_item_1 , tweet_text);
//        timelineView.setAdapter(timelineAdapter);
//
//        //getting token and secret from frgagment arguments
//        Bundle b = getArguments();
//        if(b != null){
//            token = b.getString("token");
//            secret = b.getString("secret");
//            //TODO : get Screen name
//        }
//
//        //fetch Timeline
//        fetchTimeline();

        return v;
    }

    private void fetchTimeline() {
        //todo : make singleton class of this
        OAuthInterceptor oauth1Woocommerce = new OAuthInterceptor.Builder()
                .consumerKey(Config.CONSUMER_KEY)
                .consumerSecret(Config.CONSUMER_SECRET)
                .tokenFunction(Config.TOKEN_KEY)
                .secretFunction(Config.TOKEN_SECRET)
                .build();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(oauth1Woocommerce)// Interceptor oauth1Woocommerce added
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/1.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiInterface apiInterface =  retrofit.create(ApiInterface.class);
        Call<ArrayList<HomeTweetResponse>> call  =  apiInterface.getHomeTimeline(mUserName,50);
        call.enqueue(new Callback<ArrayList<HomeTweetResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<HomeTweetResponse>> call, Response<ArrayList<HomeTweetResponse>> response) {
                ArrayList<HomeTweetResponse> result = response.body();
                ArrayList<Long> ids = new ArrayList<Long>();
                for(int i=0;i<result.size();i++){
                    ids.add(Long.parseLong(result.get(i).getId()));
                }
                onDownloadComplete(ids);
            }

            private void onDownloadComplete(ArrayList<Long> ids) {
                for(final long idq : ids){
                    TweetUtils.loadTweet(idq, new com.twitter.sdk.android.core.Callback<com.twitter.sdk.android.core.models.Tweet>() {
                        @Override
                        public void success(Result<Tweet> result) {
                            Tweet tweet = result.data;
                            final TweetView tweetView = new TweetView(getContext() , tweet , R.style.tw__TweetLightWithActionsStyle);
                            //tweetView.setOnActionCallback(actionCallback);

                            tweetView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String url = "https://twitter.com/" + mUserName + "/status/" + idq;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle(mUserName);

                                    WebView wv = new WebView(getContext());
                                    wv.loadUrl(url);
                                    wv.setWebViewClient(new WebViewClient(){
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                            view.loadUrl(String.valueOf(request));
                                            return true;
                                        }
                                    });

                                    alert.setView(wv);
                                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    alert.show();
                                }
                            });

                            CardView card = new CardView(getContext());
                            ViewGroup.LayoutParams params = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            card.setContentPadding(0,0,0,2);
                            card.setBackgroundColor(Color.parseColor("#E0E0E0"));
                            card.setMaxCardElevation(2);

                            card.addView(tweetView);
                            linearLayoutTweetHolder.addView(card);

                        }
                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ArrayList<HomeTweetResponse>> call, Throwable t) {

            }
        });
        swipeRefreshLayout.setRefreshing(false);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.twitter.com/1.1/statuses/user_timeline.json")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
//        //TODO : pass screen_name
//        Call<ArrayList <Tweet>> call = apiInterface.getTweets("__Arshdeep__");
//
//        call.enqueue(new Callback<ArrayList<Tweet>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Tweet>> call, Response<ArrayList<Tweet>> response) {
//                ArrayList <Tweet> tweetResponse = response.body();
//                if(tweetResponse != null){
//                    tweet_text.clear();
//                    for(Tweet t : tweetResponse){
//                        tweet_text.add(t.text);
//                    }
//                    timelineAdapter.notify();
//                }else{
//                    Toast.makeText(timelineView.getContext(), "Connection Issue", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Tweet>> call, Throwable t) {
//
//            }
//        });

    }

//    final com.twitter.sdk.android.core.Callback<com.example.arshdeep.twittervone.Tweet> actionCallback = new com.twitter.sdk.android.core.Callback<com.example.arshdeep.twittervone.Tweet>() {
//        @Override
//        public void success(Result<com.example.arshdeep.twittervone.Tweet> result) {
//            // todo : happens when fav is done
//        }
//
//        @Override
//        public void failure(TwitterException exception) {
//            if(exception instanceof TwitterAuthException){
//                Toast.makeText(getContext(), "Fav Fail", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };


}
