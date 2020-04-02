package vidal.sergi.sallefyv1.controller.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
    private PlaylistListAdapter mPlaylistAdapter;
    private ArrayList<Playlist> mPlaylist;
    private Button bPlaylist;
    private Button bArtistas;
    private Button bCanciones;


    private int pos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        getData();
        initViews();

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    public void onDestroy() {
        super.onDestroy();
    }


    private void getData() {
        PlaylistManager.getInstance(getApplicationContext()).getOwnPlayList(this);
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
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        Intent intent = new Intent(getApplicationContext(), PlaylistDetailsActivity.class);
        intent.putExtra("Playlist", playlist);
        startActivity(intent);
    }
}
