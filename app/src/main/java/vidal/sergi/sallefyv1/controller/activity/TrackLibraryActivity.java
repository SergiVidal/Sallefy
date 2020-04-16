package vidal.sergi.sallefyv1.controller.activity;



import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.music.MusicCallback;
import vidal.sergi.sallefyv1.controller.music.MusicService;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;

public class TrackLibraryActivity extends AppCompatActivity implements TrackListCallback, MusicCallback, TrackCallback {
    private BottomNavigationView mNav;
    private RecyclerView mTracksView;
    private TrackListAdapter mTracksAdapter;
    private Button bPlaylist;
    private Button bUsers;
    private Button baddSong;
    private Button bCanciones;
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private RecyclerView mRecyclerView;
    private boolean mServiceBound = false;

    private ArrayList<Track> mTracks;
    private int currentTrack = 0;
    private int pos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_library);
        getData();
        initViews();

        bCanciones=  (Button)findViewById(R.id.item_canciones_button);
        bUsers = (Button)findViewById(R.id.item_artistas_button);
        bPlaylist =  (Button)findViewById(R.id.item_playlist_button);
        baddSong = (Button)findViewById(R.id.add_song);
        bUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArtistLibraryActivity.class);
                startActivity(intent);
            }
        });
        bPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LibraryActivity.class);
                startActivity(intent);
            }
        });
        baddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
            }
        });
        bCanciones.setEnabled(false);
        bCanciones.setTextColor(Color.parseColor("#9E9E9E"));
    }



    public void onDestroy() {
        super.onDestroy();
    }


    private void getData() {
        TrackManager.getInstance(getApplicationContext()).getOwnTracks(this);
    }

    private void initViews() {

        LinearLayoutManager managerTracks = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks);
        mTracksView = (RecyclerView) findViewById(R.id.search_tracks_recyclerview);
        mTracksView.setLayoutManager(managerTracks);
        mTracksView.setAdapter(mTracksAdapter);

        mNav = findViewById(R.id.bottom_navigation);
        mNav.setSelectedItemId(R.id.action_home);
        mNav.setOnNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
               case R.id.action_home:
                   intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_search:
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });
    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/


    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {
        Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
        intent.putExtra("track", mTracks.get(index));
        startActivity(intent);
    }

    @Override
    public void onLikeTrackSelected(int index) {
        pos=index;
        TrackManager.getInstance(getApplicationContext())
                .addLikeTrack(mTracks.get(index).getId(), this);
    }

    @Override
    public void onDetailsTrackSelected(int index) {
        Intent intent = new Intent(getApplicationContext(), TrackOptionsActivity.class);
        intent.putExtra("track", mTracks.get(index));
        startActivity(intent);
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/
    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onNoTracks(Throwable throwable) {

    }
    @Override
    public void onPersonalTracksReceived(List<Track> tracks) {
        mTracks = (ArrayList<Track>) tracks;
        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks);
        mTracksView.setAdapter(mTracksAdapter);

    }
    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onLikedTrack(Track track) {

    }

    @Override
    public void onIsLikedTrack(Track track) {

    }

    @Override
    public void onCreateTrack() {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onMusicPlayerPrepared() {

    }

    @Override
    public void onTrackChanged(int index) {

    }
}

