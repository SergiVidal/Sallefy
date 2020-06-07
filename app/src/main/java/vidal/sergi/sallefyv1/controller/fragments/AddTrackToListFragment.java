package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;

public class AddTrackToListFragment extends Fragment implements PlaylistCallback, PlaylistAdapterCallback {
    public static final String TAG = AddTrackToListFragment.class.getName();
    public static AddTrackToListFragment getInstance() {
        return new AddTrackToListFragment();
    }

    private RecyclerView mPlaylistsView;
    private PlaylistListAdapter mPlaylistAdapter;
    private ArrayList<Playlist> mPlaylist;
    private int pos;
    private Track track;
    private FragmentCallback fragmentCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_add_track_to_list, container, false);
        track = (Track) getArguments().getSerializable("track");
        System.out.println(track);
        getData();
        initViews(v);
        return v;
    }
    private void initViews(View v) {

        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mPlaylistAdapter = new PlaylistListAdapter(null, getContext(), this, R.layout.item_playlist_short);

        mPlaylistsView = (RecyclerView) v.findViewById(R.id.search_playlists_recyclerview);
        mPlaylistsView.setLayoutManager(managerPlaylists);
        mPlaylistsView.setAdapter(mPlaylistAdapter);
    }

    private void getData() {
        PlaylistManager.getInstance(getContext()).getOwnPlayList(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {

        int flag = 0;
        System.out.println(playlist.getTracks().size());

        for(int i= 0; i <= playlist.getTracks().size()-1; i++){
            if(track.getName().equals(playlist.getTracks().get(i).getName())){
                flag = 1;
            }
        }

        if(flag == 0){
            List<Track> t = playlist.getTracks();
            t.add(track);
            playlist.setTracks(t);
            addTrackToPlaylist(playlist);
        }else{
            Toast.makeText(getContext(), "Ya existe esta cancion!", Toast.LENGTH_LONG).show();
        }
    }
    private void addTrackToPlaylist(Playlist p){
        System.out.println(p.getName()+" <-- Playlist");
        PlaylistManager.getInstance(getContext())
                .addTrackToPlaylistAttempt(p, AddTrackToListFragment.this);


    }
    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onAddTrackToPlaylistSuccess(Playlist playlist) {
        Toast.makeText(getContext(), "Cancion añadida!", Toast.LENGTH_LONG).show();
        fragmentCallback.onTrackSelection(TrackOptionsFragment.getInstance(), track);
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
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

}
