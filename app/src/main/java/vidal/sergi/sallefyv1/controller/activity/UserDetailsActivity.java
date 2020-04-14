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

    private User user;
    private ImageView ivUserPhoto;
    private TextView tvUsername;
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
        ivUserPhoto = findViewById(R.id.ivUserPhoto);
        if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_account_circle_black)
                    .load(user.getImageUrl())
                    .into(ivUserPhoto);
        }
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
}