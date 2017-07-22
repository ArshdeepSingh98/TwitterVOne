package com.example.arshdeep.twittervone.UI;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arshdeep.twittervone.Data.FollowUser;
import com.example.arshdeep.twittervone.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Arshdeep on 7/22/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private List<FollowUser> followUsers;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile_picture;
        TextView name,screen_name,description;
        ImageView verified;
        MyViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
            screen_name = view.findViewById(R.id.screen_name);
            description = view.findViewById(R.id.description);
            verified = view.findViewById(R.id.verified);
            profile_picture = view.findViewById(R.id.profile_picture);
        }
    }

    public RecyclerAdapter(Context context , List <FollowUser> followUsers){
        this.followUsers = followUsers;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_user_list_item , parent ,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FollowUser followUser = followUsers.get(position);
        holder.name.setText(followUser.getName());
        holder.screen_name.setText(followUser.getScreen_name());
        holder.description.setText(followUser.getDescription());
        Picasso.with(mContext).load(followUser.getProfile_image_url()).into(holder.profile_picture);
        if(followUser.isVerified()) {
            Picasso.with(mContext).load(R.drawable.verified).into(holder.verified);
        }
    }

    @Override
    public int getItemCount() {
        return followUsers.size();
    }
}
