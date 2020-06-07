package vidal.sergi.sallefyv1.controller.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.SharedCallback;
import vidal.sergi.sallefyv1.controller.fragments.AddTrackToListFragment;
import vidal.sergi.sallefyv1.controller.fragments.CreatePlayListFragment;
import vidal.sergi.sallefyv1.controller.fragments.GenreFragment;
import vidal.sergi.sallefyv1.controller.fragments.HomeFragment;
import vidal.sergi.sallefyv1.controller.fragments.LibraryArtistFragment;
import vidal.sergi.sallefyv1.controller.fragments.LibraryFragment;
import vidal.sergi.sallefyv1.controller.fragments.LibraryTrackFragment;
import vidal.sergi.sallefyv1.controller.fragments.PlayerFragment;
import vidal.sergi.sallefyv1.controller.fragments.PlaylistDetailsFragment;
import vidal.sergi.sallefyv1.controller.fragments.ProfileFragment;
import vidal.sergi.sallefyv1.controller.fragments.SearchFragment;
import vidal.sergi.sallefyv1.controller.fragments.StatsFollowedPlaylistsFragment;
import vidal.sergi.sallefyv1.controller.fragments.StatsFragment;
import vidal.sergi.sallefyv1.controller.fragments.StatsLikedTracksFragment;
import vidal.sergi.sallefyv1.controller.fragments.UploadFragment;
import vidal.sergi.sallefyv1.controller.fragments.UserDetailsFragment;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.TrackManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class MainActivity extends FragmentActivity implements FragmentCallback, TrackCallback, UserCallback, PlaylistCallback {

    private FragmentManager mFragmentManager;
    private FragmentCallback fragmentCallback;
    private FragmentTransaction mTransaction;
    private BottomNavigationView mNav;
    private Track trackShare;
    private Playlist playlist;
    private User user;
    private long idShared;
    private TrackManager trackListener;
    private UserManager userListener;
    private PlaylistManager playlistListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String[] segments = data.getPath().split("/");
            String path = segments[segments.length - 2];
            String id = segments[segments.length - 1];

            if (Session.getInstance(this).getUserToken() == null) {
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent1);
            }

            if (path.equals("track")) {
                long idPass = Long.parseLong(id);
                trackListener.getInstance(getApplicationContext()).shareTrack(idPass, this);
            } else if (path.equals("playlist")) {
                long idPass = Long.parseLong(id);
                playlistListener.getInstance(getApplicationContext()).getPlaylistAttempt(idPass, this);
            } else if (path.equals("user")) {
                userListener.getInstance(getApplicationContext()).getUserData(id, this);
            }
        }
        initViews();
        setInitialFragment();
        requestPermissions();

    }

    public void initViews() {
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mNav = findViewById(R.id.bottom_navigation);
        mNav.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    fragment = HomeFragment.getInstance();
                    break;
                case R.id.action_search:
                    fragment = SearchFragment.getInstance();
                    break;
                case R.id.action_library:
                    fragment = LibraryFragment.getInstance();
                    break;
                case R.id.action_profile:
                    fragment = ProfileFragment.getInstance();
                    break;

            }
            replaceFragment(fragment);
            return true;
        });
    }

    private void setInitialFragment() {
        mTransaction.add(R.id.fragment_container, HomeFragment.getInstance());
        mTransaction.commit();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS}, Constants.PERMISSIONS.MICROPHONE);

        } else {
            Session.getInstance(this).setAudioEnabled(true);
        }
    }

    private void replaceFragment(Fragment fragment) {
        String fragmentTag = getFragmentTag(fragment);
        Fragment currentFragment = mFragmentManager.findFragmentByTag(fragmentTag);
        if (currentFragment != null) {
            if (!currentFragment.isVisible()) {

                if (fragment.getArguments() != null) {
                    currentFragment.setArguments(fragment.getArguments());
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, currentFragment, fragmentTag)
                        .addToBackStack(null)
                        .commit();
            }
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, fragmentTag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private String getFragmentTag(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            return HomeFragment.TAG;
        } else if (fragment instanceof SearchFragment) {
            return SearchFragment.TAG;
        } else if (fragment instanceof LibraryFragment) {
            return LibraryFragment.TAG;
        } else if (fragment instanceof ProfileFragment) {
            return ProfileFragment.TAG;
        } else if (fragment instanceof PlaylistDetailsFragment) {
            return PlaylistDetailsFragment.TAG;
        } else if (fragment instanceof UserDetailsFragment) {
            return UserDetailsFragment.TAG;
        } else if (fragment instanceof LibraryArtistFragment) {
            return LibraryArtistFragment.TAG;
        } else if (fragment instanceof LibraryTrackFragment) {
            return LibraryTrackFragment.TAG;
        } else if (fragment instanceof UploadFragment) {
            return UploadFragment.TAG;
        } else if (fragment instanceof PlayerFragment) {
            return PlayerFragment.TAG;
        } else if (fragment instanceof GenreFragment) {
            return GenreFragment.TAG;
        } else if (fragment instanceof AddTrackToListFragment) {
            return AddTrackToListFragment.TAG;
        } else if (fragment instanceof CreatePlayListFragment) {
            return CreatePlayListFragment.TAG;
        }else if(fragment instanceof StatsFragment){
            return StatsFragment.TAG;
        }else if(fragment instanceof StatsLikedTracksFragment){
            return StatsLikedTracksFragment.TAG;
        }else if(fragment instanceof StatsFollowedPlaylistsFragment){
            return StatsFollowedPlaylistsFragment.TAG;
        }

        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println(requestCode);
        if (requestCode == Constants.PERMISSIONS.MICROPHONE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Session.getInstance(this).setAudioEnabled(true);
            }
        }
    }

    @Override
    public void onChangeFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onPlaylistDetails(Fragment fragment, Playlist playlist) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("playlist", playlist);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    @Override
    public void onUsersDetails(Fragment fragment, User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    @Override
    public void onTrackSelection(Fragment fragment, Track track) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("track", track);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    @Override
    public void onGenreSelection(Fragment fragment, Genre genre) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Genre", genre);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onSharedTrack(Track track) {
        Fragment fragment = null;
        System.out.println(track.getName() + "<----- NOMBRE CANCION");
        fragment = PlayerFragment.getInstance();
        onTrackSelection(fragment, track);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onUserInfoReceived(User userData) {
        Fragment fragment = null;
        user = userData;
        System.out.println(user + "<----- NOMBRE USER");
        fragment = UserDetailsFragment.getInstance();
        onUsersDetails(fragment, userData);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onUsersReceived(List<User> users) {

    }

    @Override
    public void onUsersFailure(Throwable throwable) {

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

    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onGetPlaylistReceivedSuccess(Playlist playlist) {
        Fragment fragment = null;
        System.out.println(playlist.getName() + "<----- NOMBRE PLAYLIST");
        fragment = PlaylistDetailsFragment.getInstance();
        onPlaylistDetails(fragment, playlist);
    }

    /////////////////////////////////////////////////////////////////////////////////
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
}
