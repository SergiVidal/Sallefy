package vidal.sergi.sallefyv1.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;

public class UserDetailsFragment extends Fragment implements PlaylistAdapterCallback, TrackListCallback, TrackCallback, PlaylistCallback {
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

    private RecyclerView rvPlaylist;
    private PlaylistListAdapter mPlaylistAdapter;

    private RecyclerView rvTrack;
    private TrackListAdapter mTrackAdapter;

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
        getData();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void getData() {
        UserManager.getInstance(getContext()).getUserTracks(user.getLogin(), this);
        UserManager.getInstance(getContext()).getUserPlaylists(user.getLogin(), this);

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

        tvUsername = v.findViewById(R.id.tvUsername);
        tvUsername.setText(user.getLogin());

        tvNumFollowers = v.findViewById(R.id.tvNumFollowers);
        tvNumFollowers.setText(String.valueOf(user.getFollowers()));

        tvNumFollowing = v.findViewById(R.id.tvNumFollowing);
        tvNumFollowing.setText(String.valueOf(user.getFollowing()));

        tvNumPlaylist = v.findViewById(R.id.tvNumPlaylist);
        tvNumPlaylist.setText(String.valueOf(user.getPlaylists()));

        // TODO: Recyrcler View Playlist

        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mPlaylistAdapter = new PlaylistListAdapter(null, getContext(), this, R.layout.item_playlist_short);
        rvPlaylist = v.findViewById(R.id.user_playlist_recyclerview);
        rvPlaylist.setLayoutManager(managerPlaylists);
        rvPlaylist.setAdapter(mPlaylistAdapter);


        tvNumTracks = v.findViewById(R.id.tvNumTracks);
        tvNumTracks.setText(String.valueOf(user.getTracks()));

        // TODO: Recyrcler View Tracks

        LinearLayoutManager managerTracks = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mTrackAdapter = new TrackListAdapter(this, getContext(), null, "");
        rvTrack = v.findViewById(R.id.user_track_recyclerview);
        rvTrack.setLayoutManager(managerTracks);
        rvTrack.setAdapter(mTrackAdapter);
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {

    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {

    }

    @Override
    public void onLikeTrackSelected(int index) {

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
        mTrackAdapter = new TrackListAdapter(this, getContext(), (ArrayList<Track>) tracks, user.getLogin());
        rvTrack.setAdapter(mTrackAdapter);
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
    public void onLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onAddTrackToPlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onAddTrackToPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onGetPlaylistReceivedSuccess(Playlist playlist) {

    }

    @Override
    public void onGetPlaylistReceivedFailure(Throwable throwable) {

    }

    @Override
    public void onAllList(ArrayList<Playlist> playlists) {
        mPlaylistAdapter = new PlaylistListAdapter(playlists, getContext(), this, R.layout.item_playlist_short);
        rvPlaylist.setAdapter(mPlaylistAdapter);
    }

    @Override
    public void onFollowingPlaylist(Playlist playlist) {

    }

    @Override
    public void onIsFollowingPlaylist(Playlist playlist) {

    }

    @Override
    public void onNoPlaylist(Throwable throwable) {

    }

    @Override
    public void onPersonalPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }
}
