package vidal.sergi.sallefyv1.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.service.TrackService;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class TrackManager {

    private static final String TAG = "TrackManager";
    private Context mContext;
    private static TrackManager sTrackManager;
    private Retrofit mRetrofit;
    private TrackService mTrackService;


    public static TrackManager getInstance(Context context) {
        if (sTrackManager == null) {
            sTrackManager = new TrackManager(context);
        }

        return sTrackManager;
    }

    public TrackManager(Context context) {
        mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mTrackService = mRetrofit.create(TrackService.class);
    }

    public synchronized void getAllTracks(final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mTrackService.getAllTracks("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    trackCallback.onTracksReceived(response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                trackCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void getUserTracks(String login, final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mTrackService.getUserTracks(login, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    trackCallback.onUserTracksReceived(response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                trackCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void getOwnTracks(final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<List<Track>> call = mTrackService.getOwnTracks("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    trackCallback.onPersonalTracksReceived((ArrayList<Track>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                trackCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    /********************   ADD LIKE TRACK    ********************/
    public synchronized void addLikeTrack(long id, final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Track> call = mTrackService.addLikeTrack("Bearer " + userToken.getIdToken(), id);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful()) {
                    System.out.println("addLikeTrack()");
                    trackCallback.onLikedTrack(response.body());
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                trackCallback.onFailure(t);
            }
        });
    }

    /********************   IS LIKED TRACK    ********************/
    public synchronized void isLikedTrack(long id, final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Track> call = mTrackService.isLikedTrack("Bearer " + userToken.getIdToken(), id);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful())
                    trackCallback.onIsLikedTrack(response.body());
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                trackCallback.onFailure(t);
            }
        });
    }
}

