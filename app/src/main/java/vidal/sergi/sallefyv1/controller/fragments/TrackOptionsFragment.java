package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import vidal.sergi.sallefyv1.BuildConfig;
import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;

public class TrackOptionsFragment extends Fragment implements TrackListCallback, TrackCallback {
    public static final String TAG = TrackOptionsFragment.class.getName();

    public static TrackOptionsFragment getInstance() {
        return new TrackOptionsFragment();
    }
    private Track track;
    private BottomNavigationView mNav;
    private ImageView ivSongPhoto;
    private TextView tvNameSong;
    private TextView tvArtist;
    private ImageButton bLike;
    private ImageButton bAddSong;
    private ImageButton bDelete;
    private ImageButton bArtist;
    private ImageButton bShare;
    private FragmentCallback fragmentCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_track_options, container, false);
        track = (Track) getArguments().getSerializable("track");
        System.out.println(track);


        bLike = v.findViewById(R.id.like_btn);
        bLike.setOnClickListener(v1 -> onLikeTrackSelected(1, 0));
        bArtist = v.findViewById(R.id.artist_btn);
        bArtist.setOnClickListener(v1 -> fragmentCallback.onUsersDetails(UserDetailsFragment.getInstance(), track.getUser()));
        bAddSong = v.findViewById(R.id.add_btn);
        bAddSong.setOnClickListener(v1 -> fragmentCallback.onTrackSelection(AddTrackToListFragment.getInstance(), track));
        bShare = v.findViewById(R.id.share_btn);
        bShare.setOnClickListener(v1 -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sallefy");
                String shareMessage= "\n Song: "+track.getName()+"\n"+"By: "+track.getUser().getLogin()+"\n";
                shareMessage = shareMessage + "http://sallefy.eu-west-3.elasticbeanstalk.com/track/" + track.getId() +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Choose one"));
            } catch(Exception e) {
                e.toString();
            }
        });

        initViews(v);
        return v;
    }

    private void initViews(View v) {
        ivSongPhoto = v.findViewById(R.id.imageSong);
        if (track.getThumbnail() != null && !track.getThumbnail().equals("")) {
            Glide.with(getContext())// puede que sea getAplication
                    .asBitmap()
                    .placeholder(R.drawable.ic_account_circle_black)
                    .load(track.getThumbnail())
                    .into(ivSongPhoto);
        }


        tvNameSong = v.findViewById(R.id.nameSong);
        tvNameSong.setText(track.getName());

        tvArtist = v.findViewById(R.id.nameAuthor);
        tvArtist.setText(String.valueOf(track.getUser().getLogin()));

        if (track.isLiked()) {
            bLike.setImageResource(R.drawable.ic_plus_pause);
        } else {
            bLike.setImageResource(R.drawable.ic_plus);

        }
    }
    public void onResume() {
        super.onResume();
        getData();
    }
    private void getData() {
        TrackManager.getInstance(getContext()).getOwnTracks(this);
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
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index, int option) {

    }

    @Override
    public void onLikeTrackSelected(int index, int option) {
        TrackManager.getInstance(getContext())
                .addLikeTrack(track.getId(), this);

        if(track.isLiked()){
            bLike.setImageResource(R.drawable.ic_plus);
            track.setLiked(false);
        }else{
            bLike.setImageResource(R.drawable.ic_plus_pause);
            track.setLiked(true);
        }
    }

    @Override
    public void onDetailsTrackSelected(int index, int option) {

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

    @Override
    public void onFailure(Throwable throwable) {

    }

}
