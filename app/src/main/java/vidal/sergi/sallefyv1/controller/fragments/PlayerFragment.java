package vidal.sergi.sallefyv1.controller.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.SessionManagerListener;

import java.io.IOException;
import java.util.List;

import io.objectbox.Box;
import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.model.Database;
import vidal.sergi.sallefyv1.model.ObjectBox;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.DownloadCallback;
import vidal.sergi.sallefyv1.restapi.manager.DownloadManager;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class PlayerFragment extends Fragment implements SessionManagerListener<CastSession>, DownloadCallback {

    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private TextView tvTitle;
    private TextView tvAuthor;
    private ImageView ivPhoto;

    private ImageButton btnBackward;
    private ImageButton btnDownload;
    private ImageButton btnPlayStop;
    private ImageButton btnForward;
    private MenuItem btnCast;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private Runnable mRunnable;
    private Track track;

    private FragmentCallback fragmentCallback;

    private CircleLineVisualizer mVisualizer;

    private MediaPlayer mPlayer;
    private String url;
    private CastContext mCastContext;
    private MenuItem mediaRouteMenuItem;

    private Box<Database> userBox;
    private CastStateListener mCastStateListener;
    private Toolbar mToolbar;

    private IntroductoryOverlay mIntroductoryOverlay;
    public static PlayerFragment getInstance() {
        return new PlayerFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Static: ", "Enter onStart " + this.hashCode());

        mPlayer.prepareAsync(); // might take long! (for buffering, etc)
    }

    public static final String TAG = PlayerFragment.class.getName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCastContext = CastContext.getSharedInstance(getContext()); // inicio del proceso de casteo
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        System.out.println("Entra el OncreateOption");
        inflater.inflate(R.menu.browse, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getActivity(), menu,
                R.id.media_route_menu_item);
        super.onCreateOptionsMenu(menu,inflater);
    }
    private void showIntroductoryOverlay() {
        if (mIntroductoryOverlay != null) {
            mIntroductoryOverlay.remove();
        }
        if ((mediaRouteMenuItem != null) && mediaRouteMenuItem.isVisible()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mIntroductoryOverlay = new IntroductoryOverlay.Builder(
                            getActivity(), mediaRouteMenuItem)
                            .setTitleText("Introducing Cast")
                            .setSingleTime()
                            .setOnOverlayDismissedListener(
                                    new IntroductoryOverlay.OnOverlayDismissedListener() {
                                        @Override
                                        public void onOverlayDismissed() {
                                            mIntroductoryOverlay = null;
                                        }
                                    })
                            .build();
                    mIntroductoryOverlay.show();
                }
            });
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_play_song, container, false);
        super.onCreate(savedInstanceState);
        track = (Track) getArguments().getSerializable("track");
        mCastContext = CastContext.getSharedInstance(getContext());
        ObjectBox.init(getContext());
        userBox = ObjectBox.get().boxFor(Database.class);

        if (!isDownloaded()) {
            url = track.getUrl();
        }

        Log.d("Static: ", "Enter onCreate " + this.hashCode());
        initViews(v);
        mCastContext = CastContext.getSharedInstance(this.getContext()); // inicio del proceso de casteo
        mediaRouteMenuItem = v.findViewById(R.id.media_route_menu_item);

        setupActionBar(v);
        showIntroductoryOverlay();
        setHasOptionsMenu(true);
        return v;
    }
    private void setupActionBar(View v) {
        System.out.println("Seting UP");
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        // mToolbar.set
        //((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    public boolean isDownloaded() {
        List<Database> databases = userBox.getAll();

        for (Database database : databases) {
            System.out.println(database);
            if (database.getId() == track.getId()) {
                this.url = database.getUrl();
                System.out.println("--------------------------->True!");
                return true;
            }
        }
        return false;
    }


    public void createBox(Database database) {
        userBox.put(database);
        System.out.println("----->  " + userBox.getAll());
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
    }

    private void initViews(View v) {
        mVisualizer = v.findViewById(R.id.circleVisualizer);

        btnDownload = v.findViewById(R.id.download_btn);

        btnDownload.setOnClickListener(v1 -> {

            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                if (!isDownloaded()) {
                    DownloadManager.getInstance(getContext()).downloadTrack(track, this);
                    url = track.getUrl();
                    System.out.println("--------------------->False!");
                    Toast.makeText(getContext(), "Downloading Audio", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Already downloaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(url);
            mPlayer.setOnPreparedListener(mp -> {
                mSeekBar.setMax(mPlayer.getDuration());

                int audioSessionId = mPlayer.getAudioSessionId();
                if (audioSessionId != -1) {
//                        mVisualizer.setAudioSessionId(audioSessionId);
                }
            });
            mHandler = new Handler();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Thread connection = new Thread(() -> {
            try {
                mPlayer.setDataSource(url);
                mPlayer.prepare(); // might take long! (for buffering, etc)
            } catch (IOException e) {

            }
        });

        tvAuthor = v.findViewById(R.id.music_artist_2);
        tvTitle = v.findViewById(R.id.music_title_2);
        tvTitle.setText(track.getName());
        tvAuthor.setText(track.getUser().getLogin());
        ivPhoto = v.findViewById(R.id.ivPlaylistPhoto);


        btnPlayStop = v.findViewById(R.id.music_play_btn_2);
        btnPlayStop.setTag(PLAY_VIEW);
        btnPlayStop.setOnClickListener(v1 -> {
            if (btnPlayStop.getTag().equals(PLAY_VIEW)) {
                mPlayer.start();
                updateSeekBar();
                btnPlayStop.setImageResource(R.drawable.ic_pause);
                btnPlayStop.setTag(STOP_VIEW);
                if(isDownloaded()) {
                    Toast.makeText(getContext(), "Playing Offline Audio", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Playing Stream Audio", Toast.LENGTH_SHORT).show();
                }

            } else {
                mPlayer.pause();
                btnPlayStop.setImageResource(R.drawable.ic_play);
                btnPlayStop.setTag(PLAY_VIEW);
                Toast.makeText(getContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
            }
        });

        mSeekBar = v.findViewById(R.id.seekBar_2);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
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

    public void updateSeekBar() {
        mSeekBar.setProgress(mPlayer.getCurrentPosition());

        if (mPlayer.isPlaying()) {
            mRunnable = () -> updateSeekBar();
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Static: ", "Enter onPause " + this.hashCode());
        mPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Static:", "Enter onResume " + this.hashCode());

        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Static: ", "Enter onStop " + this.hashCode());
        if (mPlayer != null)
            mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Static: ", "Enter onDestroy " + this.hashCode());
        if (mVisualizer != null)
            mVisualizer.release();

    }

    @Override
    public void onSongDownload(Database database) {
        createBox(database);
    }

    @Override
    public void onSongDownloadFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onSessionStarting(CastSession castSession) {

    }

    @Override
    public void onSessionStarted(CastSession castSession, String s) {

    }

    @Override
    public void onSessionStartFailed(CastSession castSession, int i) {

    }

    @Override
    public void onSessionEnding(CastSession castSession) {

    }

    @Override
    public void onSessionEnded(CastSession castSession, int i) {

    }

    @Override
    public void onSessionResuming(CastSession castSession, String s) {

    }

    @Override
    public void onSessionResumed(CastSession castSession, boolean b) {

    }

    @Override
    public void onSessionResumeFailed(CastSession castSession, int i) {

    }

    @Override
    public void onSessionSuspended(CastSession castSession, int i) {

    }
}
