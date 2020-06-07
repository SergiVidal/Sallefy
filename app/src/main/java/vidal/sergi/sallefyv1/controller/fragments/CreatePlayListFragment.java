package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.utils.Session;

public class CreatePlayListFragment extends Fragment implements PlaylistCallback {
    public static final String TAG = CreatePlayListFragment.class.getName();

    public static CreatePlayListFragment getInstance() { return new CreatePlayListFragment(); }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText etPlaylistName;
    private Button bCreatePlaylist;
    private List<Playlist> playlistList;
    private OnFragmentInteractionListener mListener;
    private FragmentCallback fragmentCallback;

    public CreatePlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_play_list, container, false);
        playlistList = Session.getInstance(getContext()).getPlaylistList();
        initViews(v);
        return v;
    }
    private void initViews(View v) {
        etPlaylistName = v.findViewById(R.id.playlist_name);
        bCreatePlaylist = v.findViewById(R.id.create_playlist_btn);
        bCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlaylist(new Playlist(etPlaylistName.getText().toString()));
            }
        });

    }
    private void createPlaylist(Playlist playlist) {
        PlaylistManager.getInstance(getContext())
                .createPlaylistAttempt(playlist, CreatePlayListFragment.this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {
        playlistList.add(playlist);
        Toast.makeText(getContext(), "onCreatePlaylist Success", Toast.LENGTH_LONG).show();
        fragmentCallback.onChangeFragment(LibraryFragment.getInstance());

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Datos incorrectos!", Toast.LENGTH_LONG).show();

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
    public void onPersonalPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
