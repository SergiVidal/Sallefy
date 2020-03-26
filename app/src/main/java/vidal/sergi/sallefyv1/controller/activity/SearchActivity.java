package vidal.sergi.sallefyv1.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.GenresAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.utils.Session;

public class SearchActivity extends AppCompatActivity implements PlaylistCallback {

    private EditText etPlaylistName;
    private Button bDisplayPlaylist;
    private List<Playlist> playlistList;
    private ArrayList<Track> tracks;

    private Playlist p;

    private RecyclerView mRecyclerView;
    private RecyclerView mGenresView;
    private GenresAdapter mGenresAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tracks = new ArrayList<>();

        playlistList = Session.getInstance(getApplicationContext()).getPlaylistList();
        initViews();

    }

    private void initViews() {
        etPlaylistName = findViewById(R.id.playlist_to_display);
        bDisplayPlaylist = findViewById(R.id.display_tracks_btn);
        bDisplayPlaylist.setOnClickListener(v -> {
            displayTracks(etPlaylistName.getText().toString());
        });


        mRecyclerView = findViewById(R.id.rvTracks);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(this, null);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager managerGenres = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mGenresAdapter = new GenresAdapter(null);
        mGenresView = (RecyclerView) findViewById(R.id.search_genres_recyclerview);
        mGenresView.setLayoutManager(managerGenres);
        mGenresView.setAdapter(mGenresAdapter);
    }

    private void displayTracks(String playlistName) {
        p = new Playlist();

        for(Playlist playlist: playlistList){
            if(playlist.getName().equals(playlistName)){
                p = playlist;
            }
        }

        if (p.getName() != null) {
            PlaylistManager.getInstance(getApplicationContext())
                    .getPlaylistAttempt(p.getId(), SearchActivity.this);
        } else {
            Toast.makeText(getApplicationContext(), "Datos incorrectos!", Toast.LENGTH_LONG).show();

        }
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

    public void onGenresReceive(ArrayList<Genre> genres) {
        ArrayList<String> genresString = (ArrayList<String>) genres.stream().map(Genre::getName).collect(Collectors.toList());
        mGenresAdapter = new GenresAdapter(genresString);
        mGenresView.setAdapter(mGenresAdapter);
    }
    @Override
    public void onGetPlaylistReceivedSuccess(Playlist playlist) {

        if(p.getTracks().size() == 0) {
            TrackListAdapter adapter = new TrackListAdapter(this, null);
            mRecyclerView.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), "Esta playlist no dispone de canciones!", Toast.LENGTH_LONG).show();

        }else if (!p.getName().equals("")) {
            Toast.makeText(getApplicationContext(), "GetPlaylistReceived success", Toast.LENGTH_LONG).show();
            this.tracks = (ArrayList) p.getTracks();
            TrackListAdapter adapter = new TrackListAdapter(this, tracks);
            mRecyclerView.setAdapter(adapter);

        }
    }

    @Override
    public void onGetPlaylistReceivedFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
