package vidal.sergi.sallefyv1.controller.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

import java.io.IOException;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Track;


public class PlaySongActivity extends AppCompatActivity {

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
    private Track track;

    private CircleLineVisualizer mVisualizer;

    private MediaPlayer mPlayer;
    private String url;
    //private final static String url = "https://soundcloud.com/lionelrichieofficial/all-night-long-all-night-album";



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        track = (Track) getIntent().getSerializableExtra("track");
        url = track.getUrl();
        Log.d("Static: ", "Enter onCreate " + this.hashCode());
        setContentView(R.layout.activity_play_song);
        initViews();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Static: ", "Enter onStart " + this.hashCode());


        mPlayer.prepareAsync(); // might take long! (for buffering, etc)

    }

    private void initViews() {
        mVisualizer = findViewById(R.id.circleVisualizer);


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

        tvAuthor = findViewById(R.id.music_artist_2);
        tvTitle = findViewById(R.id.music_title_2);
        tvTitle.setText(track.getName());
        tvAuthor.setText(track.getUser().getLogin());
        ivPhoto = findViewById(R.id.ivPlaylistPhoto);
        btnBackward = (ImageButton)findViewById(R.id.music_backward_btn_2);
        btnForward = (ImageButton)findViewById(R.id.music_forward_btn_2);

        btnPlayStop = (ImageButton)findViewById(R.id.music_play_btn_2);
        btnPlayStop.setTag(PLAY_VIEW);
        btnPlayStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (btnPlayStop.getTag().equals(PLAY_VIEW)) {
                    mPlayer.start();
                    updateSeekBar();
                    btnPlayStop.setImageResource(R.drawable.ic_pause);
                    btnPlayStop.setTag(STOP_VIEW);
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();

                } else {
                    mPlayer.pause();
                    btnPlayStop.setImageResource(R.drawable.ic_play);
                    btnPlayStop.setTag(PLAY_VIEW);
                    Toast.makeText(getApplicationContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mSeekBar = (SeekBar) findViewById(R.id.seekBar_2);
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
    protected void onPause() {
        super.onPause();
        Log.d("Static: ", "Enter onPause " + this.hashCode());
        mPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Static:", "Enter onResume " + this.hashCode());

        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Static: ", "Enter onStop " + this.hashCode());
        if (mPlayer != null)
            mPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Static: ", "Enter onDestroy " + this.hashCode() );
        if (mVisualizer != null)
            mVisualizer.release();

    }
}

