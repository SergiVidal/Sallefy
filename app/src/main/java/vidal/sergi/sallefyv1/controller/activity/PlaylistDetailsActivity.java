package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.GenresAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;

public class PlaylistDetailsActivity extends AppCompatActivity implements TrackListCallback {

    private Playlist playlist;
    private ImageView ivPhoto;
    private TextView tvPlaylistName;
    private TextView tvAuthor;

    private RecyclerView mTracksView;
    private TrackListAdapter mTracksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_details);
        playlist = (Playlist) getIntent().getSerializableExtra("Playlist");
        System.out.println("detailsOnCreate() " + playlist);

        initViews();
//        getData();


    }

    private void initViews() {
        tvPlaylistName = findViewById(R.id.tvPlaylistName);
        tvPlaylistName.setText(playlist.getName());

        tvAuthor = findViewById(R.id.tvAuthor);
        tvAuthor.setText(playlist.getUser().getLogin());

        ivPhoto = findViewById(R.id.ivPlaylistPhoto);
        if (playlist.getThumbnail() != null) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(playlist.getThumbnail())
                    .into(ivPhoto);
        }

        LinearLayoutManager managerTracks = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), (ArrayList) playlist.getTracks());
        mTracksView = (RecyclerView) findViewById(R.id.search_tracks_recyclerview);
        mTracksView.setLayoutManager(managerTracks);
        mTracksView.setAdapter(mTracksAdapter);
    }
//    private void getData() {
//        PlaylistManager.getInstance(getApplicationContext())
//                .getPlaylistAttempt(p.getId(), DisplayPlaylistTracksActivity.this);
//    }
    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {

    }
}
