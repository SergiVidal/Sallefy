package vidal.sergi.sallefyv1.controller.activity;



import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.music.MusicCallback;

import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.utils.Session;

public class TrackLibraryActivity extends AppCompatActivity implements TrackListCallback, MusicCallback, TrackCallback {
    private BottomNavigationView mNav;

    private RecyclerView mTracksView;
    private RecyclerView mFavTracksView;


    private TrackListAdapter mTracksAdapter;
    private TrackListAdapter mFavTracksAdapter;
    private Button bPlaylist;
    private Button bUsers;
    private Button baddSong;
    private Button bCanciones;
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";

    private ArrayList<Track> mTracks;
    private ArrayList<Track> mFavTracks;
    private int currentTrack = 0;
    private int pos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_library);
        /*getData();
        initViews();*/

        bCanciones=  (Button)findViewById(R.id.item_canciones_button);
        bUsers = (Button)findViewById(R.id.item_artistas_button);
        bPlaylist =  (Button)findViewById(R.id.item_playlist_button);
        baddSong = (Button)findViewById(R.id.add_song);
       /* bUsers.setOnClickListener(new View.OnClickListener() {
           /* @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ArtistLibraryActivity.class);
                startActivity(intent);
            }
        });*/
      /*  bPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LibraryActivity.class);
                startActivity(intent);
            }
        });*/
        baddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
//                startActivity(intent);
            }
        });
        bCanciones.setEnabled(false);
        bCanciones.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.opacity));
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    public void onDestroy() {
        super.onDestroy();
    }


    private void getData() {
        TrackManager.getInstance(getApplicationContext()).getOwnTracks(this);
        TrackManager.getInstance(getApplicationContext()).getLikedTracks(this);
    }

    private void initViews() {

        LinearLayoutManager managerTracks = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks, Session.getInstance(getApplicationContext()).getUser().getLogin());
        mTracksView = (RecyclerView) findViewById(R.id.search_tracks_recyclerview);
        mTracksView.setLayoutManager(managerTracks);
        mTracksView.setAdapter(mTracksAdapter);

        LinearLayoutManager managerTracksFav = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mFavTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mFavTracks, Session.getInstance(getApplicationContext()).getUser().getLogin());
        mFavTracksView = (RecyclerView) findViewById(R.id.fav_tracks_recyclerview);
        mFavTracksView.setLayoutManager(managerTracksFav);
        mFavTracksView.setAdapter(mFavTracksAdapter);

        mNav = findViewById(R.id.bottom_navigation);
        mNav.setSelectedItemId(R.id.action_home);
        mNav.setOnNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
               case R.id.action_home:
//                   intent = new Intent(getApplicationContext(), HomeActivity.class);
//                    startActivity(intent);
                    break;
                /*case R.id.action_search:
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    break;*/
                case R.id.action_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });
    }
    private void isLikedTrack(Track track){
        mTracks.get(pos).setLiked(track.isLiked());
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

    @Override
    public void onDeleteTrackSelected(int index) {

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
        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks, "");
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
    public void onLikedTracksReceived(List<Track> tracks) {
        mFavTracks = (ArrayList<Track>) tracks;
        mFavTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mFavTracks, "");
        mFavTracksView.setAdapter(mFavTracksAdapter);

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

