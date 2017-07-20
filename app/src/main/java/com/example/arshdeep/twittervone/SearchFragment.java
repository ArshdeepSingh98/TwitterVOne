package com.example.arshdeep.twittervone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.example.arshdeep.twittervone.Network.SearchTweetResponse;
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

        linearLayoutSearchHolder = (LinearLayout) v.findViewById(R.id.linearLayoutSearchHolder);
        searchView = (MaterialSearchView) v.findViewById(R.id.search_view);
        setHasOptionsMenu(true);

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
        final ApiInterface apiInterface =  retrofit.create(ApiInterface.class);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    Call<SearchTweetResponse> call  =  apiInterface.getSearchTweets(newText);
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
                                        final TweetView tweetView = new TweetView(getContext() , tweet , R.style.tw__TweetDarkWithActionsStyle);
                                        //tweetView.setOnActionCallback(actionCallback);

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

                                        linearLayoutSearchHolder.addView(tweetView);
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
