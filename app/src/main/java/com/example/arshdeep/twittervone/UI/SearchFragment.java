package com.example.arshdeep.twittervone.UI;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.arshdeep.twittervone.Network.ApiInterface;
import com.example.arshdeep.twittervone.Network.HomeTweetResponse;
import com.example.arshdeep.twittervone.Network.OAuthInterceptor;
import com.example.arshdeep.twittervone.Network.RetrofitClient;
import com.example.arshdeep.twittervone.Network.SearchTweetResponse;
import com.example.arshdeep.twittervone.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.*;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Arshdeep on 7/20/2017.
 */

public class SearchFragment extends Fragment {

    String token,secret,mUserName;
    long id,userId;
    MaterialSearchView searchView;
    LinearLayout linearLayoutSearchHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search , container , false);

        linearLayoutSearchHolder = v.findViewById(R.id.linearLayoutSearchHolder);
        searchView = v.findViewById(R.id.search_view);
        setHasOptionsMenu(true);

//        ((ProfileActivity)getActivity()).toolbar.setTitle("Search");

        Bundle b = getArguments();
        if(b != null){
            token = b.getString("token");
            secret = b.getString("secret");
            mUserName = b.getString("userName");
            id = b.getLong("id");
            userId = b.getLong("userId");
        }

        searchTweets();

        return v;
    }

    private void searchTweets() {

        final RetrofitClient retroClient = RetrofitClient.getInstance();
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    Call<SearchTweetResponse> call  =  retroClient.getApiInterface().getSearchTweets(newText);
                    call.enqueue(new Callback<SearchTweetResponse>() {
                        @Override
                        public void onResponse(Call<SearchTweetResponse> call, Response<SearchTweetResponse> response) {
                            SearchTweetResponse result = response.body();
                            ArrayList<HomeTweetResponse> searchs = result.getSearchList();
                            ArrayList<Long> ids = new ArrayList<Long>();
                            for(HomeTweetResponse s : searchs){
                                ids.add(Long.parseLong(s.getId()));
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
                                        linearLayoutSearchHolder.addView(card);

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
                        public void onFailure(Call<SearchTweetResponse> call, Throwable t) {

                        }
                    });
                }else{
                    //search text is null
                }
                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search){
            searchView.setMenuItem(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
