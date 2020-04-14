package vidal.sergi.sallefyv1.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;

public class UserDetailsActivity extends AppCompatActivity {

    User user;
    private ImageView ivUserPhoto;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvNumPlaylist;
    private TextView tvNumTracks;
    private TextView tvNumFollowers;
    private TextView tvNumFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        user = (User) getIntent().getSerializableExtra("User");
        System.out.println(user);
        initViews();
    }

    private void initViews() {
        ivUserPhoto = findViewById(R.id.ivPlaylistPhoto);
        if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(user.getImageUrl())
                    .into(ivUserPhoto);
        }
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText(user.getFirstName());

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(user.getEmail());

//        tvNumPlaylist = findViewById(R.id.tvNumPlaylist);
//        tvNumPlaylist.setText(String.valueOf(user.getPlaylists()));
//
//        tvNumTracks = findViewById(R.id.tvNumTracks);
//        tvNumTracks.setText(String.valueOf(user.getTracks()));
//
//        tvNumFollowers = findViewById(R.id.tvNumFollowers);
//        tvNumFollowers.setText(String.valueOf(user.getFollowers()));
//        tvNumFollowing = findViewById(R.id.tvNumFollowing);
//        tvNumFollowing.setText(String.valueOf(user.getFollowing()));
    }
}