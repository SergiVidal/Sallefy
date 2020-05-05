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
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.GenreCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.service.GenreService;
import vidal.sergi.sallefyv1.restapi.service.TrackService;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class GenreManager extends BaseManager {

    private static final String TAG = "genreManager";
    private static GenreManager sGenreManager;
    private Context mContext;

    private GenreService mService;

    public static GenreManager getInstance(Context context) {
        if (sGenreManager == null) {
            sGenreManager = new GenreManager(context);
        }
        return sGenreManager;
    }

    private GenreManager(Context cntxt) {
        mContext = cntxt;
        mService = retrofit.create(GenreService.class);
    }

    public synchronized void getAllGenres(final GenreCallback genreCallback) {

        Call<List<Genre>> call = mService.getAllGenres();
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                int code = response.code();
                ArrayList<Genre> data = (ArrayList<Genre>) response.body();

                if (response.isSuccessful()) {
                    genreCallback.onGenresReceive(data);

                } else {
                    Log.d(TAG, "Error: " + code);
                    genreCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.d(TAG, "Error: " + t);
                genreCallback.onFailure(new Throwable("ERROR " + t.getMessage()));
            }
        });
    }

    public synchronized void getTracksByGenre(int genreId, final GenreCallback genreCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mService.getTracksByGenre(genreId);
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();
                ArrayList<Track> data = (ArrayList<Track>) response.body();

                if (response.isSuccessful()) {
                    genreCallback.onTracksByGenre(data);

                } else {
                    Log.d(TAG, "Error: " + code);
                    genreCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error: " + t);
                genreCallback.onFailure(new Throwable("ERROR " + t.getMessage()));
            }
        });

    }

}
