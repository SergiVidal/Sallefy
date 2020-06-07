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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.TrackListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.UserAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Search;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.SearchCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.SearchManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.utils.Session;

public class SearchFragment extends Fragment implements UserCallback, SearchCallback, TrackListCallback, PlaylistCallback, UserAdapterCallback, PlaylistAdapterCallback, TrackCallback {

    private List<Playlist> playlistList;
    private ArrayList<Track> tracks;
    private Playlist p;
    private EditText etKeyword;
    private int checkboxid = 0;
    private FragmentCallback fragmentCallback;

    private RecyclerView mRecyclerView;

    private RecyclerView mUsersView;
    private UserAdapter mUserAdapter;

    private RecyclerView mPlaylistsView;
    private PlaylistListAdapter mPlaylistAdapter;
    private TrackListAdapter adapter;

    private Search search;
    private Button bSearch;
    private int pos;
    public static final String TAG = SearchFragment.class.getName();

    public static SearchFragment getInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        tracks = new ArrayList<>();
        playlistList = Session.getInstance(getContext()).getPlaylistList();
        initViews(v);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
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
    private void initViews(View view) {
        etKeyword = view.findViewById(R.id.keyword);
        bSearch = view.findViewById(R.id.search_btn);
        bSearch.setOnClickListener(v -> {

            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            mRecyclerView = view.findViewById(R.id.rvTracks);
            adapter = new TrackListAdapter(this, getContext(), null, "");
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(adapter);

            LinearLayoutManager managerUsers = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
            mUserAdapter = new UserAdapter(null, getContext(), this);
            mUsersView = view.findViewById(R.id.search_users_recyclerview);
            mUsersView.setLayoutManager(managerUsers);
            mUsersView.setAdapter(mUserAdapter);

            LinearLayoutManager managerPlaylists = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
            mPlaylistAdapter = new PlaylistListAdapter(null, getContext(), this, R.layout.item_playlist);
            mPlaylistsView = view.findViewById(R.id.search_playlists_recyclerview);
            mPlaylistsView.setLayoutManager(managerPlaylists);
            mPlaylistsView.setAdapter(mPlaylistAdapter);
            SearchManager.getInstance(getContext()).getSearch(etKeyword.getText().toString(),this);
        });
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
           View radioButton = radioGroup.findViewById(checkedId);
           int index = radioGroup.indexOfChild(radioButton);
            switch(index) {
                case 0:
                        checkboxid = 0;
                    break;
                case 1:
                        checkboxid = 1;
                    break;
                case 2:
                        checkboxid = 2;
                    break;
            }
        });
    }


    private void isLikedTrack(Track track){
        search.getTracks().get(pos).setLiked(track.isLiked());
        adapter.updateTrackLikeStateIcon(pos, track.isLiked());
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

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

    }

    @Override
    public void onUsersFailure(Throwable throwable) {

    }

    @Override
    public void onGetSearchReceivedSuccess(Search s) {
        System.out.println(checkboxid);
        search = s;
        if(checkboxid == 0){
            adapter = new TrackListAdapter(this, getContext(), (ArrayList<Track>) search.getTracks(), "");
            mRecyclerView.setAdapter(adapter);
        }
        if(checkboxid == 1){
            mPlaylistAdapter = new PlaylistListAdapter((ArrayList<Playlist>) search.getPlaylists(), getContext(), this, R.layout.item_playlist);
            mPlaylistsView.setAdapter(mPlaylistAdapter);
        }
        if(checkboxid == 2){
            mUserAdapter = new UserAdapter((ArrayList<User>) search.getUsers(), getContext(), this);
            mUsersView.setAdapter(mUserAdapter);;
        }


    }

    @Override
    public void onGetSearchReceivedFailure(Throwable throwable) {

    }

    @Override
    public void onTrackSelected(Track track) {

    }

    @Override
    public void onTrackSelected(int index) {
        fragmentCallback.onTrackSelection(PlayerFragment.getInstance(), search.getTracks().get(index));
    }

    @Override
    public void onLikeTrackSelected(int index) {
        pos=index;
        TrackManager.getInstance(getContext())
                .addLikeTrack(search.getTracks().get(index).getId(), this);
    }

    @Override
    public void onDetailsTrackSelected(int index) {
//        Intent intent = new Intent(getApplicationContext(), TrackOptionsActivity.class);
//        intent.putExtra("track", search.getTracks().get(index));
//        startActivity(intent);
    }

    @Override
    public void onDeleteTrackSelected(int index) {

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        fragmentCallback.onPlaylistDetails(PlaylistDetailsFragment.getInstance(), playlist);
    }

    @Override
    public void onUserClick(User user) {
        fragmentCallback.onUsersDetails(UserDetailsFragment.getInstance(), user);
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
    public void onPersonalPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

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
}
