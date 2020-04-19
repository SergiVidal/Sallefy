package vidal.sergi.sallefyv1.controller.activity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;

public class GenreListActivity extends AppCompatActivity implements TrackListCallback, TrackCallback {//implements TrackCallback {

    private RecyclerView mRecyclerView;
    private ArrayList<Track> mTracks;
    private TextView tvGenreName;
    private Genre genre;
    private TrackListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_list);
        genre = (Genre) getIntent().getSerializableExtra("Genre");
        initViews();
        getData();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new TrackListAdapter(this, getApplicationContext(), null, "");
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        tvGenreName = findViewById(R.id.tvGenreName);
        tvGenreName.setText(genre.getName());
    }

    private void getData() {
        TrackManager.getInstance(this).getAllTracks(this);
        mTracks = new ArrayList<>();
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
//        mTracks = (ArrayList) tracks;
        filterByGenre((ArrayList<Track>) tracks);
        adapter = new TrackListAdapter(this, getApplicationContext(), mTracks, "");
        mRecyclerView.setAdapter(adapter);
    }
    private void filterByGenre(ArrayList<Track> tracks){
        for (int i = 0; i < tracks.size();i++){
            for(int j = 0; j<tracks.get(i).getGenres().size();j++){
                if(tracks.get(i).getGenres().get(j).getName().equals(genre.getName())){
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

    }

    @Override
    public void onIsLikedTrack(Track track) {

    }

    @Override
    public void onCreateTrack() {

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
        Intent intent = new Intent(getApplicationContext(), TrackOptionsActivity.class);
        intent.putExtra("track", mTracks.get(index));
        startActivity(intent);
    }

    @Override
    public void onDeleteTrackSelected(int index) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
