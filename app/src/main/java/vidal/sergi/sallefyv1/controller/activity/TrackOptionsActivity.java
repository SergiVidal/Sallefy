package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;

public class TrackOptionsActivity extends AppCompatActivity implements TrackListCallback, TrackCallback {
    private Track track;
    private BottomNavigationView mNav;
    private ImageView ivSongPhoto;
    private TextView tvNameSong;
    private TextView tvArtist;
    private ImageButton bLike;
    private ImageButton bAddSong;
    private ImageButton bDelete;
    private ImageButton bArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_options);

        track = (Track) getIntent().getSerializableExtra("track");
        System.out.println(track);


        bLike = (ImageButton) findViewById(R.id.like_btn);
        bLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeTrackSelected(1);
            }
        });
        bArtist = (ImageButton) findViewById(R.id.artist_btn);
        bArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                intent.putExtra("User", track.getUser());
                startActivity(intent);
            }
        });
        bAddSong = (ImageButton) findViewById(R.id.add_btn);
        bAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTrackToListActivity.class);
                intent.putExtra("track", track);
                startActivity(intent);
            }
        });

        initViews();

    }
    private void initViews() {
        ivSongPhoto = findViewById(R.id.imageSong);
        if (track.getThumbnail() != null && !track.getThumbnail().equals("")) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_account_circle_black)
                    .load(track.getThumbnail())
                    .into(ivSongPhoto);
        }


        tvNameSong = findViewById(R.id.nameSong);
        tvNameSong.setText(track.getName());

        tvArtist = findViewById(R.id.nameAuthor);
        tvArtist.setText(String.valueOf(track.getUser().getLogin()));

        if(track.isLiked()){
            bLike.setImageResource(R.drawable.ic_plus_pause);
        }else{
            bLike.setImageResource(R.drawable.ic_plus);

        }
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
                case R.id.action_library:
                   intent = new Intent(getApplicationContext(), LibraryActivity.class);
                   startActivity(intent);
                   break;
                case R.id.action_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });


        // TODO: Recyrcler View Tracks

    }
    public void onResume() {
        super.onResume();
        getData();

    }
    private void getData() {
        TrackManager.getInstance(getApplicationContext()).getOwnTracks(this);
    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {

    }

    @Override
    public void onLikeTrackSelected(int index) {
        TrackManager.getInstance(getApplicationContext())
                .addLikeTrack(track.getId(), this);
    }


    @Override
    public void onDetailsTrackSelected(int index) {

    }

    @Override
    public void onDeleteTrackSelected(int index) {

    }

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
}
