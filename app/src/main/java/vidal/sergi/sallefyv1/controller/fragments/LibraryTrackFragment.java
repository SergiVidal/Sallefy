package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.activity.PlaySongActivity;
import vidal.sergi.sallefyv1.controller.activity.TrackOptionsActivity;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.music.MusicCallback;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.utils.Session;

public class LibraryTrackFragment extends Fragment implements TrackCallback, TrackListCallback  {
    public static final String TAG = LibraryTrackFragment.class.getName();

    public static LibraryTrackFragment getInstance() {
        return new LibraryTrackFragment();
    }

    private RecyclerView mTracksView;
    private RecyclerView mFavTracksView;


    private TrackListAdapter mTracksAdapter;
    private TrackListAdapter mFavTracksAdapter;
    private Button bPlaylist;
    private Button bUsers;
    private Button baddSong;
    private Button bCanciones;
    private static final String PLAY_VIEW = "PlayIcon";
    private static final String STOP_VIEW = "StopIcon";
    private FragmentCallback fragmentCallback;
    private ArrayList<Track> mTracks;
    private ArrayList<Track> mFavTracks;
    private int currentTrack = 0;
    private int pos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_library_track, container, false);
        super.onCreate(savedInstanceState);
        getData();
        initViews(v);
        bCanciones = (Button) v.findViewById(R.id.item_canciones_button);
        bUsers = (Button) v.findViewById(R.id.item_artistas_button);
        bPlaylist = (Button) v.findViewById(R.id.item_playlist_button);
        baddSong = (Button) v.findViewById(R.id.add_song);

        bUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.onChangeFragment(LibraryArtistFragment.getInstance());
            }
        });
        bPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.onChangeFragment(LibraryFragment.getInstance());
            }
        });
        baddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.onChangeFragment(UploadFragment.getInstance());
            }
        });
        bCanciones.setEnabled(false);
        bCanciones.setTextColor(ContextCompat.getColor(getContext(), R.color.opacity));

        return v;
    }

    private void getData() {
        TrackManager.getInstance(getContext()).getOwnTracks(this);
        TrackManager.getInstance(getContext()).getLikedTracks(this);
    }
    private void initViews(View v) {

        LinearLayoutManager managerTracks = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mTracksAdapter = new TrackListAdapter(this, getContext(), mTracks, Session.getInstance(getContext()).getUser().getLogin());
        mTracksView = (RecyclerView) v.findViewById(R.id.search_tracks_recyclerview);
        mTracksView.setLayoutManager(managerTracks);
        mTracksView.setAdapter(mTracksAdapter);

        LinearLayoutManager managerTracksFav = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mFavTracksAdapter = new TrackListAdapter(this, getContext(), mFavTracks, Session.getInstance(getContext()).getUser().getLogin());
        mFavTracksView = (RecyclerView) v.findViewById(R.id.fav_tracks_recyclerview);
        mFavTracksView.setLayoutManager(managerTracksFav);
        mFavTracksView.setAdapter(mFavTracksAdapter);
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
    public void onResume() {
        super.onResume();
        getData();
    }
    private void isLikedTrack(Track track){
        mTracks.get(pos).setLiked(track.isLiked());
        mTracksAdapter.updateTrackLikeStateIcon(pos, track.isLiked());
    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {
        fragmentCallback.onTrackSelection(PlayerFragment.getInstance(), mTracks.get(index));
    }

    public void onLikeTrackSelected(int index) {
        pos=index;
        TrackManager.getInstance(getContext())
                .addLikeTrack(mTracks.get(index).getId(), this);
    }

    public void onDetailsTrackSelected(int index) {
       // fragmentCallback.onTrackSelection(PlayerFragment.getInstance(), mTracks.get(index));
        // DETAILS TRACK SELECTED
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
        mTracks = (ArrayList<Track>) tracks;
        mTracksAdapter = new TrackListAdapter(this, getContext(), mTracks, "");
        mTracksView.setAdapter(mTracksAdapter);
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
        mFavTracks = (ArrayList<Track>) tracks;
        mFavTracksAdapter = new TrackListAdapter(this, getContext(), mFavTracks, "");
        mFavTracksView.setAdapter(mFavTracksAdapter);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

}
