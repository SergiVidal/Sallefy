package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.music.MusicCallback;
import vidal.sergi.sallefyv1.controller.music.MusicService;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;

public class PlaylistDetailsActivity extends AppCompatActivity implements TrackListCallback, MusicCallback {

    private Playlist playlist;
    private ImageView ivPhoto;
    private TextView tvPlaylistName;
    private TextView tvAuthor;

    private RecyclerView mTracksView;
    private TrackListAdapter mTracksAdapter;


    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private TextView tvDynamic_title;
    private TextView tvDynamic_artist;

    private ImageButton btnBackward;
    private ImageButton btnPlayStop;
    private ImageButton btnForward;
    private SeekBar mSeekBar;

    private Handler mHandler;
    private Runnable mRunnable;

    private int mDuration;
    private RecyclerView mRecyclerView;

    // Service
    private MusicService mBoundService;
    private boolean mServiceBound = false;

    private ArrayList<Track> mTracks;
    private int currentTrack = 0;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            mBoundService = binder.getService();
            mBoundService.setCallback(PlaylistDetailsActivity.this);
            mServiceBound = true;
            updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_details);
        playlist = (Playlist) getIntent().getSerializableExtra("Playlist");
        System.out.println("detailsOnCreate() " + playlist);
        mTracks = (ArrayList) playlist.getTracks();
        initViews();
        startStreamingService();


    }
    @Override
    public void onResume() {
        super.onResume();
        if (mBoundService != null) {
            resumeSongText();
            if (mBoundService.isPlaying()) {
                playAudio();
            } else {
                pauseAudio();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mServiceBound) {
            //pauseAudio();
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mServiceBound) {
            this.unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks);
        mTracksView = (RecyclerView) findViewById(R.id.search_tracks_recyclerview);
        mTracksView.setLayoutManager(managerTracks);
        mTracksView.setAdapter(mTracksAdapter);


        mHandler = new Handler();

        tvDynamic_title = findViewById(R.id.dynamic_title);
        tvDynamic_artist = findViewById(R.id.dynamic_artist);

        btnBackward = (ImageButton) findViewById(R.id.dynamic_backward_btn);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack-1)%(mTracks.size()));
                currentTrack = currentTrack < 0 ? (mTracks.size()-1):currentTrack;
                updateTrack(currentTrack);
            }
        });
        btnForward = (ImageButton) findViewById(R.id.dynamic_forward_btn);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack+1)%(mTracks.size()));
                currentTrack = currentTrack >= mTracks.size() ? 0:currentTrack;
                updateTrack(currentTrack);
            }
        });

        btnPlayStop = (ImageButton) findViewById(R.id.dynamic_play_btn);
        btnPlayStop.setTag(PLAY_VIEW);
        btnPlayStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (btnPlayStop.getTag().equals(PLAY_VIEW)) {
                    playAudio();
                } else {
                    pauseAudio();
                }
            }
        });

        mSeekBar = (SeekBar) findViewById(R.id.dynamic_seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBoundService.setCurrentDuration(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void startStreamingService () {
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        this.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void playAudio() {
        if (!mBoundService.isPlaying()) { mBoundService.togglePlayer(); }
        updateSeekBar();
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
    }

    private void pauseAudio() {
        if (mBoundService.isPlaying()) { mBoundService.togglePlayer(); }
        btnPlayStop.setImageResource(R.drawable.ic_play);
        btnPlayStop.setTag(PLAY_VIEW);
        Toast.makeText(getApplicationContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
    }

    public void updateSeekBar() {
        System.out.println("max duration: " + mBoundService.getMaxDuration());
        System.out.println("progress:" + mBoundService.getCurrrentPosition());
        mSeekBar.setMax(mBoundService.getMaxDuration());
        mSeekBar.setProgress(mBoundService.getCurrrentPosition());

        if(mBoundService.isPlaying()) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    private void updateTrack(int index) {
        Track track = mTracks.get(index);
        currentTrack = index;
        tvDynamic_title.setText(track.getName());
        tvDynamic_artist.setText(track.getUserLogin());
        mBoundService.playStream(mTracks, index);
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        //updateSeekBar();
    }


    private void resumeSongView(boolean isPlaying) {
        if (isPlaying) {
            btnPlayStop.setImageResource(R.drawable.ic_pause);
            btnPlayStop.setTag(STOP_VIEW);
        } else {
            btnPlayStop.setImageResource(R.drawable.ic_play);
            btnPlayStop.setTag(PLAY_VIEW);
        }
    }

    private void resumeSongText() {
        Track track = mBoundService.getCurrentTrack();
        if (track != null) {
            tvDynamic_artist.setText(track.getUserLogin());
            tvDynamic_title.setText(track.getName());
        }
    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/


    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {
        System.out.println("Index song: " + index);
        updateTrack(index);
    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   MusicCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/
    @Override
    public void onMusicPlayerPrepared() {
        System.out.println("Entra en el prepared");
        mSeekBar.setMax(mBoundService.getMaxDuration());
        mDuration =  mBoundService.getMaxDuration();
        playAudio();

    }
}
