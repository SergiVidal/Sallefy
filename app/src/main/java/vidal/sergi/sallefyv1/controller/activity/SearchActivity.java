package vidal.sergi.sallefyv1.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.UserAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Search;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.SearchCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.SearchManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.Session;

public class SearchActivity extends AppCompatActivity implements UserCallback, SearchCallback, TrackListCallback, PlaylistCallback, UserAdapterCallback, PlaylistAdapterCallback, TrackCallback {

    private List<Playlist> playlistList;
    private ArrayList<Track> tracks;
    private Playlist p;
    private EditText etKeyword;
    private int checkboxid = 0;
    private RecyclerView mRecyclerView;

    private RecyclerView mUsersView;
    private UserAdapter mUserAdapter;

    private RecyclerView mPlaylistsView;
    private PlaylistListAdapter mPlaylistAdapter;
    private TrackListAdapter adapter;
    private BottomNavigationView mNav;
    private Search search;
    private Button bSearch;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tracks = new ArrayList<>();
//        getData();
        playlistList = Session.getInstance(getApplicationContext()).getPlaylistList();
        initViews();

    }
    @Override
    public void onResume() {
        super.onResume();
//        getData();
        SearchManager.getInstance(this).getSearch(etKeyword.getText().toString(),this);

    }
    private void initViews() {



        etKeyword = (EditText) findViewById(R.id.keyword);
        bSearch = findViewById(R.id.search_btn);
        bSearch.setOnClickListener(v -> {
            mRecyclerView = findViewById(R.id.rvTracks);
            LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            adapter = new TrackListAdapter(this, getApplicationContext(), null, "");
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(adapter);

            LinearLayoutManager managerUsers = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
            mUserAdapter = new UserAdapter(null, getApplicationContext(), this);
            mUsersView = (RecyclerView) findViewById(R.id.search_users_recyclerview);
            mUsersView.setLayoutManager(managerUsers);
            mUsersView.setAdapter(mUserAdapter);

            LinearLayoutManager managerPlaylists = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
            mPlaylistAdapter = new PlaylistListAdapter(null, getApplicationContext(), this, R.layout.item_playlist_short);
            mPlaylistsView = (RecyclerView) findViewById(R.id.search_playlists_recyclerview);
            mPlaylistsView.setLayoutManager(managerPlaylists);
            mPlaylistsView.setAdapter(mPlaylistAdapter);
            SearchManager.getInstance(this).getSearch(etKeyword.getText().toString(),this);
        });

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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.songs_checkbox:
                if (checked)
                    checkboxid = 0;
                    break;
            case R.id.playlist_checkbox:
                if (checked)
                    checkboxid = 1;
                    break;
            case R.id.users_checkbox:
                if (checked)
                    checkboxid = 2;
                    break;
        }
    }

    private void isLikedTrack(Track track){
        search.getTracks().get(pos).setLiked(track.isLiked());
        adapter.updateTrackLikeStateIcon(pos, track.isLiked());
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
        System.out.println(checkboxid);
        search = s;
        if(checkboxid == 0){
            adapter = new TrackListAdapter(this, getApplicationContext(), (ArrayList<Track>) search.getTracks(), "");
            mRecyclerView.setAdapter(adapter);
        }
        if(checkboxid == 1){
            mPlaylistAdapter = new PlaylistListAdapter((ArrayList<Playlist>) search.getPlaylists(), getApplicationContext(), this, R.layout.item_playlist_short);
            mPlaylistsView.setAdapter(mPlaylistAdapter);
        }
        if(checkboxid == 2){
            mUserAdapter = new UserAdapter((ArrayList<User>) search.getUsers(), getApplicationContext(), this);
            mUsersView.setAdapter(mUserAdapter);;
        }


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
        pos=index;
        TrackManager.getInstance(getApplicationContext())
                .addLikeTrack(search.getTracks().get(index).getId(), this);
    }

    @Override
    public void onDetailsTrackSelected(int index) {
        Intent intent = new Intent(getApplicationContext(), TrackOptionsActivity.class);
        intent.putExtra("track", search.getTracks().get(index));
        startActivity(intent);
    }

    @Override
    public void onDeleteTrackSelected(int index) {

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        Intent intent = new Intent(getApplicationContext(), PlaylistDetailsActivity.class);
        intent.putExtra("Playlist", playlist);
        startActivity(intent);
    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onAddTrackToPlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onAddTrackToPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onGetPlaylistReceivedSuccess(Playlist playlist) {

    }

    @Override
    public void onGetPlaylistReceivedFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistById(Playlist playlist) {

    }

    @Override
    public void onPlaylistsByUser(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onAllList(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onFollowingList(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onFollowingPlaylist(Playlist playlist) {

    }

    @Override
    public void onIsFollowingPlaylist(Playlist playlist) {

    }

    @Override
    public void onNoPlaylist(Throwable throwable) {

    }

    @Override
    public void onPersonalPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onUserPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onNoTracks(Throwable throwable) {

    }

    @Override
    public void onPersonalTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onLikedTrack(Track track) {
        isLikedTrack(track);

    }

    @Override
    public void onIsLikedTrack(Track track) {
        isLikedTrack(track);
    }

    @Override
    public void onCreateTrack() {

    }

    @Override
    public void onLikedTracksReceived(List<Track> tracks) {

    }
}
