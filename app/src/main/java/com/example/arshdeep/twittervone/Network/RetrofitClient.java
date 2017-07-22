package com.example.arshdeep.twittervone.Network;

import com.example.arshdeep.twittervone.Data.Config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Arshdeep on 7/21/2017.
 */

public class RetrofitClient {
    private ApiInterface apiInterface;
    private static RetrofitClient instance = null;
    private static final Object lock = new Object();

    private RetrofitClient() {
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
        apiInterface =  retrofit.create(ApiInterface.class);
    }



    public static RetrofitClient getInstance(){
        if(instance == null){
            synchronized (lock) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    public ApiInterface getApiInterface(){
        return apiInterface;
    }
}
