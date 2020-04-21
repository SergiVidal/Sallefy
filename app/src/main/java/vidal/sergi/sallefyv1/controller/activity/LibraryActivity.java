package vidal.sergi.sallefyv1.controller.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;


import vidal.sergi.sallefyv1.model.Playlist;


import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;

public class LibraryActivity extends AppCompatActivity implements PlaylistCallback, PlaylistAdapterCallback {
    private BottomNavigationView mNav;
    private RecyclerView mPlaylistsView;
    private RecyclerView mFavPlaylistView;
    private PlaylistListAdapter mPlaylistAdapter;
    private PlaylistListAdapter mFavPlaylistAdapter;
    private ArrayList<Playlist> mPlaylist;
    private ArrayList<Playlist> mFavPlaylist;
    private Button bPlaylist;
    private Button bArtistas;
    private Button bCanciones;
    private Button createPlayList;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        getData();
        initViews();
        createPlayList = bArtistas= (Button)findViewById(R.id.create_playlist);
        bPlaylist = (Button) findViewById(R.id.item_playlist_button);
        bArtistas= (Button)findViewById(R.id.item_artistas_button);
        bCanciones=  (Button)findViewById(R.id.item_canciones_button);
        bArtistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArtistLibraryActivity.class);
                startActivity(intent);
            }
        });
        bCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrackLibraryActivity.class);
                startActivity(intent);
            }
        });
        createPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePlaylistActivity.class);
                startActivity(intent);
            }
        });
        bPlaylist.setEnabled(false);
        bPlaylist.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.opacity));
    }
    @Override
    public void onResume() {
        super.onResume();
        getData();

    }

    public void onDestroy() {
        super.onDestroy();
    }


    private void getData() {
        PlaylistManager.getInstance(getApplicationContext()).getOwnPlayList(this);
        PlaylistManager.getInstance(getApplicationContext()).getFollowingPlayList(this);
    }


    private void initViews() {
//        LinearLayoutManager managerTracks = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
//        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks);
//        mTracksView = (RecyclerView) findViewById(R.id.search_tracks_recyclerview);
//        mTracksView.setLayoutManager(managerTracks);
//        mTracksView.setAdapter(mTracksAdapter);

        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mPlaylistAdapter = new PlaylistListAdapter(null, getApplicationContext(), this, R.layout.item_playlist_short);

        mPlaylistsView = (RecyclerView) findViewById(R.id.search_playlists_recyclerview);
        mPlaylistsView.setLayoutManager(managerPlaylists);
        mPlaylistsView.setAdapter(mPlaylistAdapter);

        LinearLayoutManager managerPlaylistsFav = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mFavPlaylistAdapter = new PlaylistListAdapter(null, getApplicationContext(), this, R.layout.item_playlist_short);
        mFavPlaylistView = (RecyclerView) findViewById(R.id.fav_playlists_recyclerview);
        mFavPlaylistView.setLayoutManager(managerPlaylistsFav);
        mFavPlaylistView.setAdapter(mFavPlaylistAdapter);

        mNav = findViewById(R.id.bottom_navigation);
        mNav.setSelectedItemId(R.id.action_home);
        mNav.setOnNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_search:
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    break;
//                case R.id.action_library:
//                   intent = new Intent(getApplicationContext(), LibraryActivity.class);
//                   startActivity(intent);
//                   break;
                case R.id.action_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });

    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   PlaylistCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/
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
    public void onPersonalPlaylistReceived(ArrayList<Playlist> p) {
        mPlaylist = p;
        mPlaylistAdapter = new PlaylistListAdapter(mPlaylist, getApplicationContext(), this, R.layout.item_playlist_short);
        mPlaylistsView.setAdapter(mPlaylistAdapter);
    }
    @Override
    public void onUserPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> p) {
        mFavPlaylist = p;
        mFavPlaylistAdapter = new PlaylistListAdapter(mFavPlaylist, getApplicationContext(), this, R.layout.item_playlist_short);
        mFavPlaylistView.setAdapter(mFavPlaylistAdapter);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        Intent intent = new Intent(getApplicationContext(), PlaylistDetailsActivity.class);
        intent.putExtra("Playlist", playlist);
        startActivity(intent);
    }


}
