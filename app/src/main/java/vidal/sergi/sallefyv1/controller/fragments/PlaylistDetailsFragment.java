package vidal.sergi.sallefyv1.controller.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
/*import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;*/
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.music.MusicCallback;
import vidal.sergi.sallefyv1.controller.music.MusicService;
import vidal.sergi.sallefyv1.model.CurrentLoc;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;

public class PlaylistDetailsFragment extends Fragment implements TrackListCallback, MusicCallback, PlaylistCallback, TrackCallback {
    public static final String TAG = PlaylistDetailsFragment.class.getName();

    public static PlaylistDetailsFragment getInstance() {
        return new PlaylistDetailsFragment();
    }

    private Playlist playlist;
    private ImageView ivPhoto;
    private TextView tvPlaylistName;
    private TextView tvAuthor;
    private Button bFollow;
    private Button bRandom;
    private ImageButton bShare;


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

    private BottomNavigationView mNav;

    private int pos;

    private CurrentLoc currentLoc;

    private FragmentCallback fragmentCallback;

   // private FusedLocationProviderClient fusedLocationClient;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mBoundService = binder.getService();
            mBoundService.setCallback(PlaylistDetailsFragment.this);
            mServiceBound = true;
            updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist_details, container, false);

        playlist = (Playlist) getArguments().getSerializable("playlist");
        mTracks = (ArrayList) playlist.getTracks();

        bShare = (ImageButton) v.findViewById(R.id.share_btn);
        bShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sallefy");
                    String shareMessage= "\n Playlist: "+playlist.getName()+"\n"+"By: "+playlist.getUser().getLogin()+"\n";
                    shareMessage = shareMessage + "http://sallefy.eu-west-3.elasticbeanstalk.com/playlist/" + playlist.getId() +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        /*fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        currentLoc = new CurrentLoc(location.getLatitude(), location.getLongitude());
                    }
                });
*/
        initViews(v);
        startStreamingService();
        return v;
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
            getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initViews(View view) {
        tvPlaylistName = view.findViewById(R.id.tvPlaylistName);
        tvPlaylistName.setText(playlist.getName());

        tvAuthor = view.findViewById(R.id.tvAuthor);
        tvAuthor.setText(playlist.getUser().getLogin());

        ivPhoto = view.findViewById(R.id.ivPlaylistPhoto);
        if (playlist.getThumbnail() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(playlist.getThumbnail())
                    .into(ivPhoto);
        }

        PlaylistManager.getInstance(getContext())
                .isFollowingPlaylist(playlist.getId(), this);
        bFollow = view.findViewById(R.id.bFollow);
        bFollow.setOnClickListener(v -> {
            PlaylistManager.getInstance(getContext())
                    .addFollowPlaylist(playlist.getId(), this);
        });


        bRandom = view.findViewById(R.id.bRandom);
        bRandom.setOnClickListener(v -> {
            if (mTracks.size() != 0) {
                currentTrack = new Random().nextInt(mTracks.size());
                System.out.println("Random: " + currentTrack);
                updateTrack(currentTrack);
            }
        });


        LinearLayoutManager managerTracks = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mTracksAdapter = new TrackListAdapter(this, getContext(), mTracks, playlist.getUserLogin());
        mTracksView = (RecyclerView) view.findViewById(R.id.search_tracks_recyclerview);
        mTracksView.setLayoutManager(managerTracks);
        mTracksView.setAdapter(mTracksAdapter);


        mHandler = new Handler();

        tvDynamic_title = view.findViewById(R.id.dynamic_title);
        tvDynamic_artist = view.findViewById(R.id.dynamic_artist);

        btnBackward = (ImageButton) view.findViewById(R.id.dynamic_backward_btn);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack - 1) % (mTracks.size()));
                currentTrack = currentTrack < 0 ? (mTracks.size() - 1) : currentTrack;
                updateTrack(currentTrack);
            }
        });
        btnForward = (ImageButton) view.findViewById(R.id.dynamic_forward_btn);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTrack = ((currentTrack + 1) % (mTracks.size()));
                currentTrack = currentTrack >= mTracks.size() ? 0 : currentTrack;
                updateTrack(currentTrack);
            }
        });

        btnPlayStop = (ImageButton) view.findViewById(R.id.dynamic_play_btn);
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

        mSeekBar = (SeekBar) view.findViewById(R.id.dynamic_seekBar);
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

    private void startStreamingService() {
        Intent intent = new Intent(getContext(), MusicService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void playAudio() {
        if (!mBoundService.isPlaying()) {
            mBoundService.togglePlayer();
        }
        updateSeekBar();
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
    }

    private void pauseAudio() {
        if (mBoundService.isPlaying()) {
            mBoundService.togglePlayer();
        }
        btnPlayStop.setImageResource(R.drawable.ic_play);
        btnPlayStop.setTag(PLAY_VIEW);
        Toast.makeText(getContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
    }

    public void updateSeekBar() {
        System.out.println("max duration: " + mBoundService.getMaxDuration());
        System.out.println("progress:" + mBoundService.getCurrrentPosition());
        mSeekBar.setMax(mBoundService.getMaxDuration());
        mSeekBar.setProgress(mBoundService.getCurrrentPosition());

        if (mBoundService.isPlaying()) {
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


    private void isFollowingPlaylist(Playlist playlist) {
        if (playlist.isFollowed()) {
            bFollow.setBackgroundResource(R.drawable.btn_following);
            bFollow.setText(getString(R.string.playlist_unfollow));
        } else {
            bFollow.setBackgroundResource(R.drawable.btn_follow);
            bFollow.setText(getString(R.string.playlist_follow));
        }
    }

    private void isLikedTrack(Track track) {
        playlist.getTracks().get(pos).setLiked(track.isLiked());
        mTracksAdapter.updateTrackLikeStateIcon(pos, track.isLiked());
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/


    @Override
    public void onTrackSelected(Track track) {

    }


    @Override
    public void onTrackSelected(int index) {
        updateTrack(index);
        TrackManager.getInstance(getContext()).addPlayTrack(mTracks.get(index).getId(), currentLoc, this);
    }

    @Override
    public void onLikeTrackSelected(int index) {
        pos = index;
        TrackManager.getInstance(getContext())
                .addLikeTrack(mTracks.get(index).getId(), this);
    }

    @Override
    public void onDetailsTrackSelected(int index) {
        fragmentCallback.onTrackSelection(TrackOptionsFragment.getInstance(), mTracks.get(index));
    }

    @Override
    public void onDeleteTrackSelected(int index) {
        playlist.getTracks().remove(index);
        PlaylistManager.getInstance(getContext())
                .addTrackToPlaylistAttempt(playlist, this);
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   MusicCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/
    @Override
    public void onMusicPlayerPrepared() {
        System.out.println("Entra en el prepared");
        mSeekBar.setMax(mBoundService.getMaxDuration());
        mDuration = mBoundService.getMaxDuration();
        playAudio();

    }

    @Override
    public void onTrackChanged(int index) {
        Track track = mTracks.get(index);
        currentTrack = index;
        tvDynamic_title.setText(track.getName());
        tvDynamic_artist.setText(track.getUserLogin());
        btnPlayStop.setImageResource(R.drawable.ic_pause);
        btnPlayStop.setTag(STOP_VIEW);
        updateSeekBar();
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
        Toast.makeText(getContext(), "Cancion eliminada!", Toast.LENGTH_LONG).show();
        mTracksAdapter = new TrackListAdapter(this, getContext(), (ArrayList<Track>) playlist.getTracks(), playlist.getUserLogin());
        mTracksView.setAdapter(mTracksAdapter);
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
        isFollowingPlaylist(playlist);
    }

    @Override
    public void onIsFollowingPlaylist(Playlist playlist) {
        isFollowingPlaylist(playlist);
    }

    @Override
    public void onNoPlaylist(Throwable throwable) {

    }

    @Override
    public void onPersonalPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

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
    public void onLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onPlayedTrack(Track track) {

    }

    @Override
    public void onSharedTrack(Track track) {

    }
}
