package com.example.arshdeep.twittervone.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arshdeep.twittervone.Data.FollowUser;
import com.example.arshdeep.twittervone.Network.FollowResponse;
import com.example.arshdeep.twittervone.Network.RetrofitClient;
import com.example.arshdeep.twittervone.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arshdeep on 7/22/2017.
 */

public class FollowFragment extends Fragment{

    String token,secret,mUserName;
    long id,userId;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView followListView;
    RecyclerAdapter followListAdapter;
    List<FollowUser> followUsers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_follow, container, false);

        followUsers = new ArrayList<>();
        followListView = v.findViewById(R.id.followListView);
        followListAdapter = new RecyclerAdapter(getContext(),followUsers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        followListView.setLayoutManager(mLayoutManager);
        followListView.setItemAnimator(new DefaultItemAnimator());
        followListView.setAdapter(followListAdapter);

        Bundle b = getArguments();
        if (b != null) {
            token = b.getString("token");
            secret = b.getString("secret");
            mUserName = b.getString("userName");
            id = b.getLong("id");
            userId = b.getLong("userId");
        }
        
        fetchFollowUsers();
        
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//            }
//        });

        return v;
    }

    private void fetchFollowUsers() {
        RetrofitClient retroClient = RetrofitClient.getInstance();
        Call<FollowResponse> call = retroClient.getApiInterface().getFollowTweets();
        call.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                FollowResponse result = response.body();
                int pos = 0;
                for(FollowResponse.USER u : result.getUsers()){
                    FollowUser fu = new FollowUser(u.getName(),"@"+u.getScreen_name(),u.getProfile_image_url(),u.getDescription(),u.isVerified());
                    followUsers.add(fu);
                    followListAdapter.notifyItemInserted(pos);
                    pos++;
                }
            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {

            }
        });
    }
}

