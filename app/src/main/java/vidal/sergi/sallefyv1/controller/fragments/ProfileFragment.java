package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.utils.Session;

public class ProfileFragment extends Fragment {
    private TextView tvName;
    private TextView tvMail;
    private TextView tvFollowers;
    private TextView tvFollowings;
    private Button bShowStats;
    private Button bShowStatsLikedTracks;
    private Button bShowStatsFollowedPlaylists;
    private FragmentCallback fragmentCallback;

    public static final String TAG = ProfileFragment.class.getName();

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(v);
        return v;
    }
    private void initViews(View v) {
        tvName = v.findViewById(R.id.name);
        tvName.setText(Session.getInstance(getContext()).getUser().getLogin());

        tvMail = v.findViewById(R.id.mail);
        tvMail.setText(Session.getInstance(getContext()).getUser().getEmail());

        tvFollowers = v.findViewById(R.id.followers);
        tvFollowers.setText("Followers: "+Session.getInstance(getContext()).getUser().getFollowers());

        tvFollowings = v.findViewById(R.id.followings);
        tvFollowings.setText("Followings: "+Session.getInstance(getContext()).getUser().getFollowing());

        bShowStats = v.findViewById(R.id.top_5_songs);
        bShowStats.setOnClickListener(v1 -> fragmentCallback.onChangeFragment(StatsFragment.getInstance()));
        bShowStatsLikedTracks = v.findViewById(R.id.top_5_liked_songs);
        bShowStatsLikedTracks.setOnClickListener(v1 -> fragmentCallback.onChangeFragment(StatsLikedTracksFragment.getInstance()));
        bShowStatsFollowedPlaylists = v.findViewById(R.id.top_5_followed_playlists);
        bShowStatsFollowedPlaylists.setOnClickListener(v1 -> fragmentCallback.onChangeFragment(StatsFollowedPlaylistsFragment.getInstance()));
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
