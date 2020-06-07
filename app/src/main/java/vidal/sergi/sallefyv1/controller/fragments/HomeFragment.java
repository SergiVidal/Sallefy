package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.GenresAdapter;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.UserAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.GenreAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.GenreCallback;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.GenreManager;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;

public class HomeFragment extends Fragment implements UserCallback, PlaylistCallback, PlaylistAdapterCallback, GenreCallback, UserAdapterCallback, GenreAdapterCallback, TrackCallback {
    public static final String TAG = HomeFragment.class.getName();

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    private RecyclerView mUsersView;
    private UserAdapter mUserAdapter;

    private RecyclerView mPlaylistsView;
    private PlaylistListAdapter mPlaylistAdapter;

    private RecyclerView mGenresView;
    private GenresAdapter mGenresAdapter;

    private FragmentCallback fragmentCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
    }

    private void initViews(View v) {
        LinearLayoutManager managerUsers = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mUserAdapter = new UserAdapter(null, getContext(), this);
        mUsersView = v.findViewById(R.id.search_users_recyclerview);
        mUsersView.setLayoutManager(managerUsers);
        mUsersView.setAdapter(mUserAdapter);

        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mPlaylistAdapter = new PlaylistListAdapter(null, getContext(), this, R.layout.item_playlist);
        mPlaylistsView = v.findViewById(R.id.search_playlists_recyclerview);
        mPlaylistsView.setLayoutManager(managerPlaylists);
        mPlaylistsView.setAdapter(mPlaylistAdapter);

        LinearLayoutManager managerGenres = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mGenresAdapter = new GenresAdapter(null, getContext(), this, R.layout.item_genre);
        mGenresView = v.findViewById(R.id.search_genres_recyclerview);
        mGenresView.setLayoutManager(managerGenres);
        mGenresView.setAdapter(mGenresAdapter);
    }

    private void getData() {
        PlaylistManager.getInstance(getContext())
                .getListOfPlaylist(this);
        UserManager.getInstance(getContext())
                .getUsers(this);
        GenreManager.getInstance(getContext())
                .getAllGenres(this);
    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   PlaylistCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/
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
        mPlaylistAdapter = new PlaylistListAdapter(playlists, getContext(), this, R.layout.item_playlist);
        mPlaylistsView.setAdapter(mPlaylistAdapter);
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

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        fragmentCallback.onPlaylistDetails(PlaylistDetailsFragment.getInstance(), playlist);
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   UserCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onMeTracksSuccess(List<Track> trackList) {

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

    }

    @Override
    public void onUsersReceived(List<User> users) {
        mUserAdapter = new UserAdapter((ArrayList<User>) users, getContext(), this);
        mUsersView.setAdapter(mUserAdapter);
    }

    @Override
    public void onUsersFailure(Throwable throwable) {

    }

    @Override
    public void onUserClick(User user) {
        fragmentCallback.onUsersDetails(UserDetailsFragment.getInstance(), user);
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   GenreCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {
        mGenresAdapter = new GenresAdapter(genres, getContext(), this, R.layout.item_genre);
        mGenresView.setAdapter(mGenresAdapter);
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    @Override
    public void onGenreClick(Genre genre) {
        fragmentCallback.onGenreSelection(GenreFragment.getInstance(), genre);
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
    public void onLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onPlayedTrack(Track track) {

    }

    @Override
    public void onSharedTrack(Track track) {

    }
}
