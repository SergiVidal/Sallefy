package vidal.sergi.sallefyv1.controller.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.fragments.HomeFragment;
import vidal.sergi.sallefyv1.controller.fragments.LibraryArtistFragment;
import vidal.sergi.sallefyv1.controller.fragments.LibraryFragment;
import vidal.sergi.sallefyv1.controller.fragments.LibraryTrackFragment;
import vidal.sergi.sallefyv1.controller.fragments.PlaylistDetailsFragment;
import vidal.sergi.sallefyv1.controller.fragments.ProfileFragment;
import vidal.sergi.sallefyv1.controller.fragments.SearchFragment;
import vidal.sergi.sallefyv1.controller.fragments.TrackOptionsFragment;
import vidal.sergi.sallefyv1.controller.fragments.UserDetailsFragment;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class MainActivity extends FragmentActivity implements FragmentCallback {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private BottomNavigationView mNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        } else if(fragment instanceof PlaylistDetailsFragment){
            return PlaylistDetailsFragment.TAG;
        }else if(fragment instanceof UserDetailsFragment){
            return UserDetailsFragment.TAG;
        }else if(fragment instanceof LibraryArtistFragment){
            return LibraryArtistFragment.TAG;
        }else if(fragment instanceof LibraryTrackFragment){
        return LibraryTrackFragment.TAG;
        }else if(fragment instanceof TrackOptionsFragment){
            return TrackOptionsFragment.TAG;

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
}
