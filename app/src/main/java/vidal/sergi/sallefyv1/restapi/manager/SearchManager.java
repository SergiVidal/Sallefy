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
import vidal.sergi.sallefyv1.model.Search;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.SearchCallback;
import vidal.sergi.sallefyv1.restapi.service.PlaylistService;
import vidal.sergi.sallefyv1.restapi.service.SearchService;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class SearchManager extends BaseManager{
    private static final String TAG = "SearchManager";
    private Context mContext;
    private static SearchManager sSearchManager;
    private SearchService mSearchService;


    public static SearchManager getInstance (Context context) {
        if (sSearchManager == null) {
            sSearchManager = new SearchManager(context);
        }

        return sSearchManager;
    }

    public SearchManager(Context context) {
        mContext = context;

        mSearchService = retrofit.create(SearchService.class);
    }

    /**********************
     * Get all playlist of backend
     **********************/
    public synchronized void getSearch (String keyword, final SearchCallback searchCallback) {

        Call<Search> call = mSearchService.getSearch(keyword);

        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {

                int code = response.code();
                Search data = response.body();

                if (response.isSuccessful()) {
                    searchCallback.onGetSearchReceivedSuccess(data);
                    Log.d(TAG, "getSearch");

                } else {
                    Log.d(TAG, "Error: " + code);
                    searchCallback.onGetSearchReceivedFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                searchCallback.onFailure(t);
            }
        });
    }


}
