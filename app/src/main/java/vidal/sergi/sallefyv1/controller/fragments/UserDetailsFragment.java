package vidal.sergi.sallefyv1.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.model.User;

public class UserDetailsFragment extends Fragment {
    public static final String TAG = UserDetailsFragment.class.getName();

    public static UserDetailsFragment getInstance() {
        return new UserDetailsFragment();
    }

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_details, container, false);

        user = (User) getArguments().getSerializable("user");
        System.out.println(user);
        initViews(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }




    private void initViews(View v) {
        ivUserPhoto = v.findViewById(R.id.ivUserPhoto);
        if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
            Glide.with(getContext())
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

        tvUsername = v.findViewById(R.id.tvUsername);
        tvUsername.setText(user.getLogin());

        tvNumFollowers = v.findViewById(R.id.tvNumFollowers);
        tvNumFollowers.setText(String.valueOf(user.getFollowers()));

        tvNumFollowing = v.findViewById(R.id.tvNumFollowing);
        tvNumFollowing.setText(String.valueOf(user.getFollowing()));

        tvNumPlaylist = v.findViewById(R.id.tvNumPlaylist);
        tvNumPlaylist.setText(String.valueOf(user.getPlaylists()));

        // TODO: Recyrcler View Playlist

        tvNumTracks = v.findViewById(R.id.tvNumTracks);
        tvNumTracks.setText(String.valueOf(user.getTracks()));

        // TODO: Recyrcler View Tracks

    }

//    @Override
//    public void onPlaylistClick(Playlist playlist) {
//        Intent intent = new Intent(getApplicationContext(), PlaylistDetailsActivity.class);
//        intent.putExtra("Playlist", playlist);
//        startActivity(intent);
//    }
}
