package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.mediarouter.app.MediaRouteButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.UserAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.Database;
import vidal.sergi.sallefyv1.model.ObjectBox;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Search;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.DownloadCallback;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.SearchCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.DownloadManager;
import vidal.sergi.sallefyv1.restapi.manager.SearchManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.utils.Session;

import static com.google.android.gms.cast.framework.CastContext.*;

public class PlayerFragment extends Fragment implements SessionManagerListener<CastSession>, DownloadCallback {

    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private TextView tvTitle;
    private TextView tvAuthor;
    private ImageView ivPhoto;

    private ImageButton btnBackward;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.browse, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getActivity(), menu , R.id.share_btn);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_play_song, container, false);
        super.onCreate(savedInstanceState);
        track = (Track) getArguments().getSerializable("track");
        url = track.getUrl();
        mCastContext = CastContext.getSharedInstance(getContext());

        ObjectBox.init(getContext());
        userBox = ObjectBox.get().boxFor(Database.class);

        if (!isDownloaded()) {
            DownloadManager.getInstance(getContext()).downloadTrack(track, this);
            url = track.getUrl();
            System.out.println("--------------------->False!");
        }

        Log.d("Static: ", "Enter onCreate " + this.hashCode());
        initViews(v);
        return v;
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


        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(url);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mSeekBar.setMax(mPlayer.getDuration());

                    int audioSessionId = mPlayer.getAudioSessionId();
                    if (audioSessionId != -1) {
//                        mVisualizer.setAudioSessionId(audioSessionId);
                    }
                }
            });
            mHandler = new Handler();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Thread connection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mPlayer.setDataSource(url);
                    mPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {

                }
            }
        });

        tvAuthor = v.findViewById(R.id.music_artist_2);
        tvTitle = v.findViewById(R.id.music_title_2);
        tvTitle.setText(track.getName());
        tvAuthor.setText(track.getUser().getLogin());
        ivPhoto = v.findViewById(R.id.ivPlaylistPhoto);
        btnBackward = (ImageButton) v.findViewById(R.id.music_backward_btn_2);
        btnForward = (ImageButton) v.findViewById(R.id.music_forward_btn_2);

        btnPlayStop = (ImageButton) v.findViewById(R.id.music_play_btn_2);
        btnPlayStop.setTag(PLAY_VIEW);
        btnPlayStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (btnPlayStop.getTag().equals(PLAY_VIEW)) {
                    mPlayer.start();
                    updateSeekBar();
                    btnPlayStop.setImageResource(R.drawable.ic_pause);
                    btnPlayStop.setTag(STOP_VIEW);
                    Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_SHORT).show();

                } else {
                    mPlayer.pause();
                    btnPlayStop.setImageResource(R.drawable.ic_play);
                    btnPlayStop.setTag(PLAY_VIEW);
                    Toast.makeText(getContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mSeekBar = (SeekBar) v.findViewById(R.id.seekBar_2);
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
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
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
