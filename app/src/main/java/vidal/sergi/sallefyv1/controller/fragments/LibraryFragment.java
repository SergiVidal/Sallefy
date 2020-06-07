package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;

public class LibraryFragment extends Fragment implements PlaylistCallback, PlaylistAdapterCallback {
    public static final String TAG = LibraryFragment.class.getName();

    public static LibraryFragment getInstance() {
        return new LibraryFragment();
    }

    private RecyclerView mPlaylistsView;
    private RecyclerView mFavPlaylistView;
    private PlaylistListAdapter mPlaylistAdapter;
    private PlaylistListAdapter mFavPlaylistAdapter;
    private ArrayList<Playlist> mPlaylist;
    private ArrayList<Playlist> mFavPlaylist;
    private Button bPlaylist;
    private Button bArtistas;
    private Button bCanciones;
    private Button createPlayList;
    private FragmentCallback fragmentCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library, container, false);
        getData();
        initViews(v);
        createPlayList = v.findViewById(R.id.create_playlist);
        bPlaylist = v.findViewById(R.id.item_playlist_button);
        bArtistas = v.findViewById(R.id.item_artistas_button);
        bCanciones = v.findViewById(R.id.item_canciones_button);
        bArtistas.setOnClickListener(v1 -> fragmentCallback.onChangeFragment(LibraryArtistFragment.getInstance()));
        bCanciones.setOnClickListener(v1 -> fragmentCallback.onChangeFragment(LibraryTrackFragment.getInstance()));
        createPlayList.setOnClickListener(v1 -> fragmentCallback.onChangeFragment(CreatePlayListFragment.getInstance()));
        bPlaylist.setEnabled(false);
        bPlaylist.setTextColor(ContextCompat.getColor(getActivity(), R.color.opacity));
        return v;
    }

    private void initViews(View v) {
        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mPlaylistAdapter = new PlaylistListAdapter(null, getContext(), this, R.layout.item_playlist_short);

        mPlaylistsView = v.findViewById(R.id.search_playlists_recyclerview);
        mPlaylistsView.setLayoutManager(managerPlaylists);
        mPlaylistsView.setAdapter(mPlaylistAdapter);

        LinearLayoutManager managerPlaylistsFav = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mFavPlaylistAdapter = new PlaylistListAdapter(null, getContext(), this, R.layout.item_playlist_short);
        mFavPlaylistView = v.findViewById(R.id.fav_playlists_recyclerview);
        mFavPlaylistView.setLayoutManager(managerPlaylistsFav);
        mFavPlaylistView.setAdapter(mFavPlaylistAdapter);
    }

    private void getData() {
        PlaylistManager.getInstance(getContext()).getOwnPlayList(this);
        PlaylistManager.getInstance(getContext()).getFollowingPlayList(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
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
    public void onPlaylistClick(Playlist playlist) {
        fragmentCallback.onPlaylistDetails(PlaylistDetailsFragment.getInstance(), playlist);
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
    public void onPersonalPlaylistReceived(ArrayList<Playlist> p) {
        mPlaylist = p;
        mPlaylistAdapter = new PlaylistListAdapter(mPlaylist, getContext(), this, R.layout.item_playlist_short);
        mPlaylistsView.setAdapter(mPlaylistAdapter);
    }


    @Override
    public void getFollowingPlayList(ArrayList<Playlist> p) {
        mFavPlaylist = p;
        mFavPlaylistAdapter = new PlaylistListAdapter(mFavPlaylist, getContext(), this, R.layout.item_playlist_short);
        mFavPlaylistView.setAdapter(mFavPlaylistAdapter);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
