package vidal.sergi.sallefyv1.restapi.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public abstract class BaseManager {
    protected Retrofit retrofit;

    public BaseManager(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + Session.getInstance(Session.getmContext()).getUserToken().getIdToken());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

    }

}
