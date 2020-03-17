package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.utils.Session;

public class AddTrackActivity extends AppCompatActivity implements TrackCallback, PlaylistCallback {

    private EditText etPlaylistName;
    private EditText etTrackName;
    private Button bAddTrack;

    private List<Playlist> playlistList;
    private List<Track> tracks;

    private Track t;
    private Playlist p;

    private static final String MUSIC_URL = "http://res.cloudinary.com/jcarri/video/upload/v1573734990/sallefy/songs/mobile/hola.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        tracks = new ArrayList<>();

        TrackManager.getInstance(this).getAllTracks(this);

        playlistList = Session.getInstance(getApplicationContext()).getPlaylistList();

        initViews();
    }

    private void initViews() {
        etPlaylistName = findViewById(R.id.playlist_destination);
        etTrackName = findViewById(R.id.track_name);
        bAddTrack = findViewById(R.id.add_track_btn);
        bAddTrack.setOnClickListener(v -> {
            String playlistName = String.valueOf(etPlaylistName.getText());
            String trackName = String.valueOf(etTrackName.getText());
            addTrackToPlaylist(playlistName, trackName);
        });
    }

    private void addTrackToPlaylist(String playlistName, String trackName) {

        t = new Track();
        p = new Playlist();

        for (Track track : tracks) {
            if (track.getName().equals(trackName)) {
                t = track;
            }
        }


        for (Playlist playlist : playlistList) {
            if (playlist.getName().equals(playlistName)) {
                p = playlist;
            }
        }

        if (t.getName() != null && p.getName() != null) {

            p.getTracks().add(new Track(t.getId(), trackName, MUSIC_URL));
            PlaylistManager.getInstance(getApplicationContext())
                    .addTrackToPlaylistAttempt(p, AddTrackActivity.this);
        } else {
            Toast.makeText(getApplicationContext(), "Datos incorrectos!", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        this.tracks = tracks;

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
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onAddTrackToPlaylistSuccess(Playlist playlist) {
        if (!t.getName().equals("") && !p.getName().equals("")) {

            Toast.makeText(getApplicationContext(), "AddTrackToPlaylist success", Toast.LENGTH_LONG).show();
            System.out.println(playlist);

        } else {
            Toast.makeText(getApplicationContext(), "Datos incorrectos!", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onAddTrackToPlaylistFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Datos incorrectos!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onGetPlaylistReceivedSuccess(Playlist playlist) {

    }

    @Override
    public void onGetPlaylistReceivedFailure(Throwable throwable) {

    }
}
