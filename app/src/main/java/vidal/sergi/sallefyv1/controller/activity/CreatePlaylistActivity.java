package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.utils.Session;

public class CreatePlaylistActivity extends AppCompatActivity implements PlaylistCallback{ //implements PlaylistCallback {

    private EditText etPlaylistName;
    private Button bCreatePlaylist;
    private List<Playlist> playlistList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);
        playlistList = Session.getInstance(getApplicationContext()).getPlaylistList();

        initViews();

    }

    private void initViews() {
        etPlaylistName = findViewById(R.id.playlist_name);
       bCreatePlaylist = findViewById(R.id.create_playlist_btn);
       bCreatePlaylist.setOnClickListener(v -> createPlaylist(new Playlist(etPlaylistName.getText().toString())));

    }

    private void createPlaylist(Playlist playlist) {
        PlaylistManager.getInstance(getApplicationContext())
                .createPlaylistAttempt(playlist, CreatePlaylistActivity.this);
    }

    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {
        playlistList.add(playlist);
        Toast.makeText(getApplicationContext(), "onCreatePlaylist Success", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), LibraryActivity.class);
        startActivity(intent);

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Datos incorrectos!", Toast.LENGTH_LONG).show();

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
    public void onFailure(Throwable throwable) {

    }
}
