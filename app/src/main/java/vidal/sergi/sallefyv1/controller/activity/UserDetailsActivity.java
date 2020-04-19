package vidal.sergi.sallefyv1.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;

public class UserDetailsActivity extends AppCompatActivity implements PlaylistAdapterCallback {

    private User user;
    private ImageView ivUserPhoto;
    private TextView tvUsername;
    private TextView tvNumPlaylist;
    private TextView tvNumTracks;
    private TextView tvNumFollowers;
    private TextView tvNumFollowing;

    private RecyclerView mPlaylistsView;
    private PlaylistListAdapter mPlaylistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        user = (User) getIntent().getSerializableExtra("User");
        System.out.println(user);
        initViews();
    }

    private void initViews() {
        ivUserPhoto = findViewById(R.id.ivUserPhoto);
        if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_account_circle_black)
                    .load(user.getImageUrl())
                    .into(ivUserPhoto);
        }

//        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
//        mPlaylistAdapter = new PlaylistListAdapter(user.getPlaylists(), getApplicationContext(), this, R.layout.item_playlist_short);
//        mPlaylistsView = (RecyclerView) findViewById(R.id.search_playlists_recyclerview);
//        mPlaylistsView.setLayoutManager(managerPlaylists);
//        mPlaylistsView.setAdapter(mPlaylistAdapter);

        tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText(user.getLogin());

        tvNumFollowers = findViewById(R.id.tvNumFollowers);
        tvNumFollowers.setText(String.valueOf(user.getFollowers()));

        tvNumFollowing = findViewById(R.id.tvNumFollowing);
        tvNumFollowing.setText(String.valueOf(user.getFollowing()));

        tvNumPlaylist = findViewById(R.id.tvNumPlaylist);
        tvNumPlaylist.setText(String.valueOf(user.getPlaylists()));

        // TODO: Recyrcler View Playlist

        tvNumTracks = findViewById(R.id.tvNumTracks);
        tvNumTracks.setText(String.valueOf(user.getTracks()));

        // TODO: Recyrcler View Tracks

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        Intent intent = new Intent(getApplicationContext(), PlaylistDetailsActivity.class);
        intent.putExtra("Playlist", playlist);
        startActivity(intent);
    }
}