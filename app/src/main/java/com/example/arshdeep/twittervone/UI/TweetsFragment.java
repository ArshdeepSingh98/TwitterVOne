package com.example.arshdeep.twittervone.UI;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshdeep.twittervone.Network.ApiInterface;
import com.example.arshdeep.twittervone.Network.HomeTweetResponse;
import com.example.arshdeep.twittervone.Network.OAuthInterceptor;
import com.example.arshdeep.twittervone.Network.RetrofitClient;
import com.example.arshdeep.twittervone.Network.UserResponse;
import com.example.arshdeep.twittervone.R;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
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
 * Created by Arshdeep on 7/18/2017.
 */

public class TweetsFragment extends Fragment{

    String token,secret;
    String screen_name,mUserName;
    long id,userId;
    ListView timelineView;
    LinearLayout linearLayoutTweetHolder;
    SwipeRefreshLayout swipeRefreshLayout;
    CircleImageView profile_picture;
    TextView user_tweets,user_follwing,user_likes;
    ImageView bgprofile;
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
//                updateProfilePicture();
//                fetchTweet();
////                ((ProfileActivity)getActivity()).toolbar.setTitle("User");
//
//            }
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets , container , false);
        linearLayoutTweetHolder = v.findViewById(R.id.linearLayoutUserTweetHolder);
        swipeRefreshLayout = v.findViewById(R.id.userRefresh);
        profile_picture = v.findViewById(R.id.profile_picture);
        user_tweets = v.findViewById(R.id.user_tweets);
        user_follwing = v.findViewById(R.id.user_following);
        user_likes =  v.findViewById(R.id.user_likes);
        bgprofile =  v.findViewById(R.id.bgprofile);

//        ((ProfileActivity)getActivity()).toolbar.setTitle("User");

//        Toolbar tweet_toolbar = (Toolbar) v.findViewById(R.id.tweet_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(tweet_toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        CollapsingToolbarLayout collapsingToolbarLayout = v.findViewById(R.id.tweet_collapse);
//        collapsingToolbarLayout.setTitle(" ");
//        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getContext() , R.color.colorPrimary));


        firstTime = false;
        setHasOptionsMenu(true);

        Bundle b = getArguments();
        if(b != null){
            token = b.getString("token");
            secret = b.getString("secret");
            mUserName = b.getString("userName");
            id = b.getLong("id");
            userId = b.getLong("userId");
        }
        updateProfilePicture();
        fetchTweet();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                //todo : implement async task
                linearLayoutTweetHolder.removeAllViews();
                fetchTweet();
                updateProfilePicture();
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tweet_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings){
            Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateProfilePicture() {
        final RetrofitClient retrofitClient = RetrofitClient.getInstance();
        Call<UserResponse> call  =  retrofitClient.getApiInterface().getUserData(mUserName);
        call.enqueue(new retrofit2.Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userData = response.body();
                String profileUrl = userData.getProfile_image_url();
                String profileBg = userData.getBannerImage();
                String profileBgColor = userData.getBgColor();
                if(profileBg == null){
                    profileBgColor = "#" + profileBgColor;
                    bgprofile.setBackgroundColor(Color.parseColor(profileBgColor));
                }else{
                    Picasso.with(getContext()).load(profileBg).error(R.drawable.fail).into(bgprofile);
                }
                Picasso.with(getContext()).load(profileUrl).placeholder(R.color.cardview_dark_background).error(R.drawable.fail).into(profile_picture);
                user_tweets.setText(String.valueOf(userData.getTweetsCount()));
                user_follwing.setText(String.valueOf(userData.getFollowingCount()));
                user_likes.setText(String.valueOf(userData.getLikesCount()));
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void fetchTweet(){
        final RetrofitClient retroClient = RetrofitClient.getInstance();
        Call<ArrayList<HomeTweetResponse>> call  =  retroClient.getApiInterface().getUserTimeline(mUserName,20);
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
                        public void success(Result<Tweet> result) {
                            Tweet tweet = result.data;
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
