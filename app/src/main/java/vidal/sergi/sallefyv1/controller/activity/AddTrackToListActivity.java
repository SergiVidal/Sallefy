package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;

public class AddTrackToListActivity extends AppCompatActivity implements PlaylistCallback, PlaylistAdapterCallback {
    private RecyclerView mPlaylistsView;
    private PlaylistListAdapter mPlaylistAdapter;
    private ArrayList<Playlist> mPlaylist;
    private int pos;
    private Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track_to_list);
        track = (Track) getIntent().getSerializableExtra("track");
        System.out.println(track);
        getData();
        initViews();
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

    @Override
    public void onPlaylistClick(Playlist playlist) {
//
        int flag = 0;
        System.out.println(playlist.getTracks().size());

            for(int i= 0; i <= playlist.getTracks().size()-1; i++){
                if(track.getName().equals(playlist.getTracks().get(i).getName())){
                    flag = 1;
                }
            }


       if(flag == 0){
            List<Track> t = playlist.getTracks();
            t.add(track);
            playlist.setTracks(t);
            addTrackToPlaylist(playlist);
        }else{
            Toast.makeText(getApplicationContext(), "Ya existe esta cancion!", Toast.LENGTH_LONG).show();
        }

    }
    private void addTrackToPlaylist(Playlist p){
        System.out.println(p.getName()+" <-- Playlist");
        PlaylistManager.getInstance(getApplicationContext())
                .addTrackToPlaylistAttempt(p, AddTrackToListActivity.this);


    }

    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onAddTrackToPlaylistSuccess(Playlist playlist) {
        Intent intent = new Intent(getApplicationContext(), TrackOptionsActivity.class);
        intent.putExtra("track", track);
        Toast.makeText(getApplicationContext(), "Cancion añadida!", Toast.LENGTH_LONG).show();

        startActivity(intent);
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
    public void onAllList(ArrayList<Playlist> playlists) {

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
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
