package vidal.sergi.sallefyv1.controller.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private MusicService mBoundService;
    private boolean mServiceBound = false;

    private ArrayList<Track> mTracks;
    private int currentTrack = 0;


    private int pos;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            mBoundService = binder.getService();
            mBoundService.setCallback(TrackLibraryActivity.this);
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
        setContentView(R.layout.activity_track_library);
        getData();
        initViews();
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

    private void isLikedTrack(Track track){
//        playlist.getTracks().get(pos).setLiked(track.isLiked());
//        mTracksAdapter.updateTrackLikeStateIcon(pos, track.isLiked());
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

    @Override
    public void onLikeTrackSelected(int index) {
        pos=index;
        TrackManager.getInstance(getApplicationContext())
                .addLikeTrack(mTracks.get(index).getId(), this);
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

    @Override
    public void onTrackChanged(int index) {

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
        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks);
        mTracksView.setAdapter(mTracksAdapter);

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
    public void onFailure(Throwable throwable) {

    }
}

