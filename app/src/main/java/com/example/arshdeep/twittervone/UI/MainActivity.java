package com.example.arshdeep.twittervone.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.arshdeep.twittervone.Data.Config;
import com.example.arshdeep.twittervone.Data.SPConstants;
import com.example.arshdeep.twittervone.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class MainActivity extends AppCompatActivity {

    private TwitterLoginButton loginButton;
    private String token,secret,userName;
    private long id,userId;
    boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TWITTER INIT
        //first method using strings.xml
//        Twitter.initialize(this);
        //second method
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Config.CONSUMER_KEY , Config.CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        //setting MainActivity View
        setContentView(R.layout.activity_main);

        //checking the sp for token and secret
        SharedPreferences prefs = getSharedPreferences(SPConstants.SP_NAME , MODE_PRIVATE);
        token = prefs.getString(SPConstants.SP_TOKEN , null);
        secret = prefs.getString(SPConstants.SP_SECRET , null);
        userName = prefs.getString(SPConstants.SP_USERNAME , "name");
        id = prefs.getLong(SPConstants.SP_ID , 0);
        userId = prefs.getLong(SPConstants.SP_USERID , 0);
        isFirstTime = prefs.getBoolean(SPConstants.SP_FIRST , true);

        if(isFirstTime){
            //Twitter Login Button
            loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    //TODO : insert Twitter Session here - DONE
                    //Twitter Session
                    TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    token = authToken.token;
                    secret = authToken.secret;
                    userName = session.getUserName();
                    id = session.getId();
                    userId = session.getUserId();

                    //Optional
                    //Request User Email Address
//                TwitterAuthClient authClient = new TwitterAuthClient();
//                authClient.requestEmail(session, new Callback<String>() {
//                    @Override
//                    public void success(Result<String> result) {
//                        //TODO : do something with result that provides email address
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//                        //TODO : do something on failure
//                    }
//                });

                    //TODO : shared prefrences for token and secret - DONE
                    SharedPreferences.Editor editor = getSharedPreferences(SPConstants.SP_NAME, MODE_PRIVATE).edit();
                    editor.putString(SPConstants.SP_SECRET, secret);
                    editor.putString(SPConstants.SP_TOKEN, token);
                    editor.putBoolean(SPConstants.SP_FIRST, false);
                    editor.putString(SPConstants.SP_USERNAME , userName);
                    editor.putLong(SPConstants.SP_ID ,id);
                    editor.putLong(SPConstants.SP_USERID , userId);
                    editor.commit();

                    //TODO : intent - DONE
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    i.putExtra("token", token);
                    i.putExtra("secret", secret);
                    i.putExtra("userName", userName);
                    i.putExtra("id", id);
                    i.putExtra("userId", userId);
                    startActivity(i);
                }

                @Override
                public void failure(TwitterException exception) {
                    //TODO : do something on failure - DONE
                    Toast.makeText(MainActivity.this, "Login Failed! Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            //TODO : intent - DONE
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            i.putExtra("token", token);
            i.putExtra("secret", secret);
            i.putExtra("userName", userName);
            i.putExtra("id", id);
            i.putExtra("userId", userId);
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//           TODO : fragment
//           Fragment fragment = getFragmentManager().findFragmentById(R.id.your_fragment_id);
//           if (fragment != null) {
//              fragment.onActivityResult(requestCode, resultCode, data);
//           }
        //Callback method
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
