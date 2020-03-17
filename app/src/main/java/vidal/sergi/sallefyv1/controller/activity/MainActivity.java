package vidal.sergi.sallefyv1.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.Session;

public class MainActivity extends AppCompatActivity implements UserCallback {

    private Button mList;
    private Button mAdvancedList;
    private Button mCreatePlaylist;
    private Button mAddTrackToPlaylist;
    private Button mGetPlaylist;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = Session.getInstance(getApplicationContext()).getUser();
        UserManager.getInstance(getApplicationContext()).getUserData(user.getLogin(), this);
        UserManager.getInstance(getApplicationContext()).getUserPlaylists(user.getLogin(), this);


        initViews();
    }


    @Override
    protected void onResume() {
        super.onResume();

        user = Session.getInstance(getApplicationContext()).getUser();
        UserManager.getInstance(getApplicationContext()).getUserData(user.getLogin(), this);
        UserManager.getInstance(getApplicationContext()).getUserPlaylists(user.getLogin(), this);


    }

    private void initViews() {
        mList = findViewById(R.id.show_tracks);
        mList.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(intent);
        });

        mAdvancedList = findViewById(R.id.show_advanced_tracks);
        mAdvancedList.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AdvancedListActivity.class);
            startActivity(intent);
        });

        mCreatePlaylist = findViewById(R.id.create_new_playlist);
        mCreatePlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreatePlaylistActivity.class);
            startActivity(intent);
        });

        mAddTrackToPlaylist = findViewById(R.id.add_track_to_playlist);
        mAddTrackToPlaylist.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);
            startActivity(intent);
        });

        mGetPlaylist = findViewById(R.id.show_playlist_musics);
        mGetPlaylist.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DisplayPlaylistTracksActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLoginSuccess(String username, UserToken userToken) {

    }

    @Override
    public void onLoginFailure(Throwable throwable) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {
        Session.getInstance(getApplicationContext())
                .setUser(userData);
    }

    @Override
    public void onUserPlaylistsReceived(List<Playlist> playlistList) {
        Session.getInstance(getApplicationContext())
                .setplaylistList(playlistList);
    }
}
