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
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.service.PlaylistService;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class PlaylistManager extends BaseManager{
    private static final String TAG = "PlaylistManager";
    private Context mContext;
    private static PlaylistManager sPlaylistManager;
    private PlaylistService mPlaylistService;

    public static PlaylistManager getInstance (Context context) {
        if (sPlaylistManager == null) {
            sPlaylistManager = new PlaylistManager(context);
        }

        return sPlaylistManager;
    }

    public PlaylistManager(Context context) {
        mContext = context;

        mPlaylistService = retrofit.create(PlaylistService.class);
    }

    /**********************
     * Get all playlist of backend
     **********************/
    public synchronized void getListOfPlaylist (final PlaylistCallback playlistCallback) {

        Call<List<Playlist>> call = mPlaylistService.getAllPlaylists();

        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {

                int code = response.code();
                ArrayList<Playlist> data = (ArrayList<Playlist>) response.body();

                if (response.isSuccessful()) {
                    playlistCallback.onAllList(data);
                    Log.d(TAG, "getList");

                } else {
                    Log.d(TAG, "Error: " + code);
                    playlistCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                playlistCallback.onFailure(t);
            }
        });
    }
    /********************   GET PLAYLIST    ********************/
    public synchronized void getPlaylistAttempt (long id, final PlaylistCallback playlistCallback) {

        Call<Playlist> call = mPlaylistService.getPlaylist(id);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlistCallback.onGetPlaylistReceivedSuccess(response.body());
                } else {
                    playlistCallback.onGetPlaylistReceivedFailure(new Throwable("ERROR " + response.code() + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                playlistCallback.onFailure(t);
            }
        });
    }
    /********************   ADD FOLLOW PLAYLIST    ********************/
    public synchronized void addFollowPlaylist (long id, final PlaylistCallback playlistCallback) {

        Call<Playlist> call = mPlaylistService.addFollowPlaylist(id);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful())
                    playlistCallback.onFollowingPlaylist(response.body());
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                playlistCallback.onFailure(t);
            }
        });
    }
    /********************   IS FOLLOW PLAYLIST    ********************/
    public synchronized void isFollowingPlaylist (long id, final PlaylistCallback playlistCallback) {

        Call<Playlist> call = mPlaylistService.isFollowingPlaylist(id);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful())
                    playlistCallback.onIsFollowingPlaylist(response.body());
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                playlistCallback.onFailure(t);
            }
        });
    }
    /********************   ADD TRACK TO PLAYLIST    ********************/
    public synchronized void addTrackToPlaylistAttempt (Playlist playlist, final PlaylistCallback playlistCallback) {

        Call<Playlist> call = mPlaylistService.addTrackToPlaylist(playlist);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlistCallback.onAddTrackToPlaylistSuccess(response.body());
                } else {
                    playlistCallback.onAddTrackToPlaylistFailure(new Throwable("ERROR " + response.code() + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                playlistCallback.onFailure(t);
            }
        });
    }
    public synchronized void getOwnPlayList(final PlaylistCallback playlistCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<List<Playlist>> call = mPlaylistService.getOwnPlayList();
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    playlistCallback.onPersonalPlaylistReceived((ArrayList<Playlist>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    playlistCallback.onNoPlaylist(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                playlistCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }
    public synchronized void getFollowingPlayList(final PlaylistCallback playlistCallback) {
        Call<List<Playlist>> call = mPlaylistService.getFollowingPlayList();
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    playlistCallback.getFollowingPlayList((ArrayList<Playlist>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    playlistCallback.onNoPlaylist(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                playlistCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    /********************   CREATE PLAYLIST    ********************/
    public synchronized void createPlaylistAttempt (Playlist playlist, final PlaylistCallback playlistCallback) {

        Call<Playlist> call = mPlaylistService.createPlaylist(playlist);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlistCallback.onCreatePlaylistSuccess(response.body());
                } else {
                    playlistCallback.onCreatePlaylistFailure(new Throwable("ERROR " + response.code() + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                playlistCallback.onFailure(t);
            }
        });
    }

}
