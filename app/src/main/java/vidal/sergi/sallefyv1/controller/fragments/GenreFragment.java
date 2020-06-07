package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;

public class GenreFragment extends Fragment implements TrackListCallback, TrackCallback {
    public static final String TAG = GenreFragment.class.getName();

    public static GenreFragment getInstance() {
        return new GenreFragment();
    }

    private RecyclerView mRecyclerView;
    private ArrayList<Track> mTracks;
    private TextView tvGenreName;
    private Genre genre;
    private TrackListAdapter adapter;
    private FragmentCallback fragmentCallback;
    private int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_genre, container, false);
        genre = (Genre) getArguments().getSerializable("Genre");
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
        mRecyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        adapter = new TrackListAdapter(this, getContext(), null, "", 0);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        tvGenreName = v.findViewById(R.id.tvGenreName);
        tvGenreName.setText(genre.getName());
    }

    private void getData() {
        TrackManager.getInstance(getContext()).getAllTracks(this);
        mTracks = new ArrayList<>();
    }

    private void isLikedTrack(Track track) {
        mTracks.get(pos).setLiked(track.isLiked());
        adapter.updateTrackLikeStateIcon(pos, track.isLiked());
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
//        mTracks = (ArrayList) tracks;
        filterByGenre((ArrayList<Track>) tracks);
        adapter = new TrackListAdapter(this, getContext(), mTracks, "", 0);
        mRecyclerView.setAdapter(adapter);
    }

    private void filterByGenre(ArrayList<Track> tracks) {
        for (int i = 0; i < tracks.size(); i++) {
            for (int j = 0; j < tracks.get(i).getGenres().size(); j++) {
                if (tracks.get(i).getGenres().get(j).getName().equals(genre.getName())) {
                    mTracks.add(tracks.get(i));
                }
            }
        }
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
        isLikedTrack(track);
    }

    @Override
    public void onIsLikedTrack(Track track) {
        isLikedTrack(track);
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

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index, int option) {
        fragmentCallback.onTrackSelection(PlayerFragment.getInstance(), mTracks.get(index));
    }

    @Override
    public void onLikeTrackSelected(int index, int option) {
        pos = index;
        TrackManager.getInstance(getContext())
                .addLikeTrack(mTracks.get(index).getId(), this);

    }

    @Override
    public void onDetailsTrackSelected(int index, int option) {
        fragmentCallback.onTrackSelection(TrackOptionsFragment.getInstance(), mTracks.get(index));
    }

    @Override
    public void onDeleteTrackSelected(int index) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
