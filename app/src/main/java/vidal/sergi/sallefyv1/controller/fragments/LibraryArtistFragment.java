package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import vidal.sergi.sallefyv1.controller.adapters.UserAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;

public class LibraryArtistFragment extends Fragment implements UserAdapterCallback , UserCallback {
    public static final String TAG = LibraryArtistFragment.class.getName();

    public static LibraryArtistFragment getInstance() {
        return new LibraryArtistFragment();
    }

    private RecyclerView mUsersView;
    private UserAdapter mUserAdapter;
    private Button bPlaylist;
    private Button bCanciones;
    private Button bArtistas;
    private FragmentCallback fragmentCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library_artist, container, false);
        super.onCreate(savedInstanceState);
        getData();
        initViews(v);

        bPlaylist = (Button)v.findViewById(R.id.item_playlist_button);
        bArtistas= (Button)v.findViewById(R.id.item_artistas_button);
        bCanciones=  (Button)v.findViewById(R.id.item_canciones_button);

        bPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.onChangeFragment(LibraryFragment.getInstance()); // --->> cambiar fragment
            }
        });
        bCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCallback.onChangeFragment(LibraryTrackFragment.getInstance()); // --->> cambiar fragment
            }
        });
        bArtistas.setEnabled(false);
        bArtistas.setTextColor(ContextCompat.getColor(getContext(), R.color.opacity));
        return v;

    }
    private void initViews(View v) {

        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mUserAdapter = new UserAdapter(null, getContext(),this );
        mUsersView = (RecyclerView) v.findViewById(R.id.search_users_recyclerview);
        mUsersView.setLayoutManager(managerPlaylists);
        mUsersView.setAdapter(mUserAdapter);
    }
    private void getData() {
        UserManager.getInstance(getContext())
                .getFollowingUsers(this);
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
    public void onUserClick(User user) {
        fragmentCallback.onUsersDetails(UserDetailsFragment.getInstance(), user);
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
        mUserAdapter = new UserAdapter((ArrayList<User>) users, getContext(), this);
        mUsersView.setAdapter(mUserAdapter);
    }

    @Override
    public void onUsersFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }


}
