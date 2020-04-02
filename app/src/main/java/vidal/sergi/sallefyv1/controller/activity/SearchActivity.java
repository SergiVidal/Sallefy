package vidal.sergi.sallefyv1.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.GenresAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Search;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.GenreCallback;
import vidal.sergi.sallefyv1.restapi.callback.SearchCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.GenreManager;
import vidal.sergi.sallefyv1.restapi.manager.SearchManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.Session;

public class SearchActivity extends AppCompatActivity implements UserCallback, GenreCallback, SearchCallback, TrackListCallback {

    private List<Playlist> playlistList;
    private ArrayList<Track> tracks;
    private Playlist p;
    private EditText etKeyword;

    private RecyclerView mRecyclerView;
    private RecyclerView mGenresView;
    private GenresAdapter mGenresAdapter;
    private BottomNavigationView mNav;
    private Search search;
    private Button bSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tracks = new ArrayList<>();
        getData();
        playlistList = Session.getInstance(getApplicationContext()).getPlaylistList();
        initViews();

    }

    private void initViews() {

        LinearLayoutManager managerGenres = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mGenresAdapter = new GenresAdapter(null);
        mGenresView = (RecyclerView) findViewById(R.id.search_genres_recyclerview);
        mGenresView.setLayoutManager(managerGenres);
        mGenresView.setAdapter(mGenresAdapter);

        etKeyword = (EditText) findViewById(R.id.keyword);
        bSearch = findViewById(R.id.search_btn);
        bSearch.setOnClickListener(v -> {
            SearchManager.getInstance(this).getSearch(etKeyword.getText().toString(),this);
        });

        mRecyclerView = findViewById(R.id.rvTracks);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, getApplicationContext(), null);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);


        mNav = findViewById(R.id.bottom_navigation);
        mNav.setSelectedItemId(R.id.action_search);
        mNav.setOnNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    break;
//                case R.id.action_search:
//                    intent = new Intent(getApplicationContext(), SearchActivity.class);
//                    startActivity(intent);
//                    break;
                case R.id.action_library:
                    intent = new Intent(getApplicationContext(), LibraryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
//                replaceFragment(fragment);
            return true;
        });
    }


    private void getData() {
        UserManager.getInstance(getApplicationContext())
                .getUsers(this);
        GenreManager.getInstance(getApplicationContext())
                .getAllGenres(this);
    }

    public void onGenresReceive(ArrayList<Genre> genres) {
        ArrayList<String> genresString = (ArrayList<String>) genres.stream().map(Genre::getName).collect(Collectors.toList());
        mGenresAdapter = new GenresAdapter(genresString);
        mGenresView.setAdapter(mGenresAdapter);
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onLoginFailure(Throwable throwable) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onUsersReceived(List<User> users) {

    }

    @Override
    public void onUsersFailure(Throwable throwable) {

    }

    @Override
    public void onGetSearchReceivedSuccess(Search s) {
        search = s;
        TrackListAdapter adapter = new TrackListAdapter(this, getApplicationContext(), (ArrayList<Track>) search.getTracks());
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onGetSearchReceivedFailure(Throwable throwable) {

    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {
        Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
        intent.putExtra("track", search.getTracks().get(index));
        startActivity(intent);
    }

    @Override
    public void onLikeTrackSelected(int index) {

    }
}
