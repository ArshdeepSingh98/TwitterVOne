package com.example.arshdeep.twittervone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshdeep.twittervone.Network.ApiInterface;
import com.example.arshdeep.twittervone.Network.HomeTweetResponse;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Header;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String token,secret,userName;
    long id,userId;
    private SectionsPagerAdapter mSectionsPAgerAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    final private int tabCount = 4;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selecting image
                Toast.makeText(ProfileActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1 , "Select Picture") , PICK_IMAGE);
            }
        });

        //navigation drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigstionView = (NavigationView) findViewById(R.id.nav_view);
        navigstionView.setNavigationItemSelectedListener(this);

        //viewPagerAdapter
        mSectionsPAgerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //viewPager with sectionsAdapter
        mViewPager = (ViewPager) findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPAgerAdapter);

        //tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //getting token and secret from login activity
        token = getIntent().getStringExtra("token");
        secret = getIntent().getStringExtra("secret");
        userName = getIntent().getStringExtra("userName");
        id = getIntent().getLongExtra("id",0);
        userId = getIntent().getLongExtra("userId",0);



        //tweet fragment
//        TweetsFragment tweetsFragment = new TweetsFragment();
//        Bundle b = new Bundle();
//        //TODO : pass tweetid
//        //TODO : pass tweet
//        b.putString("token" , token);
//        b.putString("secret" , secret);
//        tweetsFragment.setArguments(b);
//        getSupportFragmentManager().beginTransaction().replace(R.id.container , tweetsFragment).commit();

//        timeline fragment
//        TimelineFragment timelineFragment = new TimelineFragment();
//        Bundle b = new Bundle();
//        //TODO : pass screen_name
//        b.putString("token" , token);
//        b.putString("secret" , secret);
//        timelineFragment.setArguments(b);
//        getSupportFragmentManager().beginTransaction().replace(R.id.container , timelineFragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            Toast.makeText(ProfileActivity.this, "" + selectedImage, Toast.LENGTH_SHORT).show();
            final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            final Intent intent = new ComposerActivity.Builder(ProfileActivity.this)
                    .session(session)
                    .text("tweet")
                    .image(selectedImage)
                    .hashtags("#twitter")
                    .createIntent();
            startActivity(intent);
        }
        if(requestCode == PICK_IMAGE && resultCode == RESULT_CANCELED){
            final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            final Intent intent = new ComposerActivity.Builder(ProfileActivity.this)
                    .session(session)
                    .text("tweet")
                    .hashtags("#twitter")
                    .createIntent();
            startActivity(intent);
        }
    }


    public class MyResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
                // success
                final Long tweetId = intent.getExtras().getLong(TweetUploadService.EXTRA_TWEET_ID);
            } else {
                // failure
                final Intent retryIntent = intent.getExtras().getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
//            getFragmentManager().beginTransaction().replace(Frame, )
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //TODO : delete this and tabbed_activity.xml & string.xml items
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            Bundle b = getArguments();
            int sectionNumber = b.getInt(ARG_SECTION_NUMBER);
            textView.setText(getString(R.string.section_format, sectionNumber));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //TODO : place the fragemnts in here
            if(position == 0) {
                TweetsFragment fragment = new TweetsFragment();
                Bundle args = new Bundle();
                args.putString("token",token);
                args.putString("secret",secret);
                args.putString("userName" ,userName);
                args.putLong("id" , id);
                args.putLong("userId", userId);
                fragment.setArguments(args);
                return fragment;
            }else if(position == 1){
                TimelineFragment fragment = new TimelineFragment();
                Bundle args = new Bundle();
                args.putString("token",token);
                args.putString("secret",secret);
                args.putString("userName" ,userName);
                args.putLong("id" , id);
                args.putLong("userId", userId);
                fragment.setArguments(args);
                return fragment;
            }else if(position == 2){
                FavoriteFragment fragment = new FavoriteFragment();
                Bundle args = new Bundle();
                args.putString("token",token);
                args.putString("secret",secret);
                args.putString("userName" ,userName);
                args.putLong("id" , id);
                args.putLong("userId", userId);
                fragment.setArguments(args);
                return fragment;
            }else if(position == 3){
                SearchFragment fragment = new SearchFragment();
                Bundle args = new Bundle();
                args.putString("token",token);
                args.putString("secret",secret);
                args.putString("userName" ,userName);
                args.putLong("id" , id);
                args.putLong("userId", userId);
                fragment.setArguments(args);
                return fragment;
            }
//            return null;
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "User";
                case 1:
                    return "Home";
                case 2:
                    return "Favorites";
                case 3:
                    return "Search";
            }
            return null;
        }
    }
}
