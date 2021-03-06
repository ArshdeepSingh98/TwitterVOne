package com.example.arshdeep.twittervone.UI;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arshdeep.twittervone.Network.ApiInterface;
import com.example.arshdeep.twittervone.Network.HomeTweetResponse;
import com.example.arshdeep.twittervone.Network.OAuthInterceptor;
import com.example.arshdeep.twittervone.Network.RetrofitClient;
import com.example.arshdeep.twittervone.R;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.*;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Arshdeep on 7/20/2017.
 */

public class FavoriteFragment extends Fragment{

    String token,secret;
    String screen_name,mUserName;
    long id,userId;
    ListView timelineView;
    LinearLayout linearLayoutTweetHolder;
    SwipeRefreshLayout swipeRefreshLayout;
    Boolean firstTime = true;

    //TODO : use custom adapter and Recycler List
    ListAdapter timelineAdapter;
    ArrayList< String > tweet_text;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser){
//            if(firstTime){
//                Log.i("Visible","first");
//            }else {
//                linearLayoutTweetHolder.removeAllViews();
//                fetchTweet();
////                ((ProfileActivity)getActivity()).toolbar.setTitle("Favorites");
//
//            }
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites , container , false);

        linearLayoutTweetHolder = (LinearLayout) v.findViewById(R.id.linearLayoutFavHolder);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.favRefresh);

//        ((ProfileActivity)getActivity()).toolbar.setTitle("Favorites");

        firstTime = false;

        linearLayoutTweetHolder.removeAllViews();
        Bundle b = getArguments();
        if(b != null){
            token = b.getString("token");
            secret = b.getString("secret");
            mUserName = b.getString("userName");
            id = b.getLong("id");
            userId = b.getLong("userId");
        }
        fetchTweet();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                linearLayoutTweetHolder.removeAllViews();
                fetchTweet();
            }
        });

        return v;
    }

    private void fetchTweet(){

        final RetrofitClient retroClient = RetrofitClient.getInstance();
        Call<ArrayList<HomeTweetResponse>> call  =  retroClient.getApiInterface().getFavTweets(mUserName);
        call.enqueue(new retrofit2.Callback<ArrayList<HomeTweetResponse>>() {
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
                for(final long id : ids){
                    TweetUtils.loadTweet(id, new com.twitter.sdk.android.core.Callback<com.twitter.sdk.android.core.models.Tweet>() {
                        @Override
                        public void success(Result<com.twitter.sdk.android.core.models.Tweet> result) {
                            com.twitter.sdk.android.core.models.Tweet tweet = result.data;
                            final TweetView tweetView = new TweetView(getContext() , tweet , R.style.tw__TweetLightWithActionsStyle);
                            //tweetView.setOnActionCallback(actionCallback);

                            CardView card = new CardView(getContext());
                            ViewGroup.LayoutParams params = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            card.setContentPadding(0,0,0,2);
                            card.setBackgroundColor(Color.parseColor("#E0E0E0"));
                            card.setMaxCardElevation(2);

                            card.addView(tweetView);
                            linearLayoutTweetHolder.addView(card);

                            tweetView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String url = "https://twitter.com/" + mUserName + "/status/" + id;
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
    }

}
