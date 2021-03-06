package com.example.arshdeep.twittervone.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshdeep.twittervone.R;
import com.ms_square.etsyblur.BlurSupport;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String token,secret,userName;
    long id,userId;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int PICK_IMAGE = 100;
    TabLayout tabLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        //fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selecting image from gallary
                Toast.makeText(ProfileActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1 , "Select Picture") , PICK_IMAGE);
            }
        });
        fab.setImageResource(R.drawable.ic_action_twitter_white);

        //navigation drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigstionView = (NavigationView) findViewById(R.id.nav_view);
        navigstionView.setNavigationItemSelectedListener(this);
        BlurSupport.addTo(drawer);

        //viewPagerAdapter
        SectionsPagerAdapter mSectionsPAgerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //viewPager with sectionsAdapter
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPAgerAdapter);

        //tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setTabIcons();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 0){
                    tab.setIcon(R.drawable.ic_action_user_blue);
                }else if(pos == 1){
                    tab.setIcon(R.drawable.ic_action_home_blue);
                }else if(pos == 2){
                    tab.setIcon(R.drawable.ic_action_star_10_blue);
                }else if(pos == 3){
                    tab.setIcon(R.drawable.ic_action_search_blue);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 0){
                    tab.setIcon(R.drawable.ic_action_user);
                }else if(pos == 1){
                    tab.setIcon(R.drawable.ic_action_home);
                }else if(pos == 2){
                    tab.setIcon(R.drawable.ic_action_star_10);
                }else if(pos == 3){
                    tab.setIcon(R.drawable.ic_action_search);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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

    private void setTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_user_blue);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_home);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_star_10);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_action_search);
        tabLayout.getTabAt(4).setIcon(R.drawable.fail);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
//        finish();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            final Intent intent = new ComposerActivity.Builder(ProfileActivity.this)
                    .session(session)
                    .text("Text")
                    .hashtags("#Tweet")
                    .image(selectedImage)
                    .createIntent();
            startActivity(intent);
        }
        if(requestCode == PICK_IMAGE && resultCode == RESULT_CANCELED){
            final Intent intent = new ComposerActivity.Builder(ProfileActivity.this)
                    .session(session)
                    .text("Text")
                    .hashtags("#Tweet")
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
            }else if(position == 4){
                FollowFragment fragment = new FollowFragment();
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
            return 5;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position){
//                case 0:
//                    return "User";
//                case 1:
//                    return "Home";
//                case 2:
//                    return "Favorites";
//                case 3:
//                    return "Search";
//            }
//            return null;
//        }


    }
}
