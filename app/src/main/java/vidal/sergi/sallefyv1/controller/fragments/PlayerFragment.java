package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.UserAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Search;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.SearchCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.SearchManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.utils.Session;

public class PlayerFragment extends Fragment  {

    private static final String TAG = "DynamicPlaybackActivity";
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private TextView tvTitle;
    private TextView tvAuthor;
    private ImageView ivPhoto;

    private ImageButton btnBackward;
    private ImageButton btnPlayStop;
    private ImageButton btnForward;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private Runnable mRunnable;
    Track track;

    private FragmentCallback fragmentCallback;

    private CircleLineVisualizer mVisualizer;

    private MediaPlayer mPlayer;
    private String url;


    public static PlayerFragment getInstance() {
        return new PlayerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_play_song, container, false);
        super.onCreate(savedInstanceState);
        track = (Track) getArguments().getSerializable("track");
        url = track.getUrl();
        Log.d("Static: ", "Enter onCreate " + this.hashCode());
        initViews(v);
        return v;
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
                    if (audioSessionId != -1){
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
        btnBackward = (ImageButton)v.findViewById(R.id.music_backward_btn_2);
        btnForward = (ImageButton)v.findViewById(R.id.music_forward_btn_2);

        btnPlayStop = (ImageButton)v.findViewById(R.id.music_play_btn_2);
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

        if(mPlayer.isPlaying()) {
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
        Log.d("Static: ", "Enter onDestroy " + this.hashCode() );
        if (mVisualizer != null)
            mVisualizer.release();

    }
}
