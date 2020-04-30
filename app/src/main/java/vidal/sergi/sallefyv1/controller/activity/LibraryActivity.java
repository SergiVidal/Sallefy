package vidal.sergi.sallefyv1.controller.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;


import vidal.sergi.sallefyv1.controller.fragments.HomeFragment;
import vidal.sergi.sallefyv1.controller.fragments.LibraryFragment;
import vidal.sergi.sallefyv1.controller.fragments.ProfileFragment;
import vidal.sergi.sallefyv1.controller.fragments.SearchFragment;
import vidal.sergi.sallefyv1.model.Playlist;


import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.utils.Constants;
import vidal.sergi.sallefyv1.utils.Session;

public class LibraryActivity extends FragmentActivity implements FragmentCallback {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private BottomNavigationView mNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        initViews();
        setInitialFragment();
        requestPermissions();
    }



    private void initViews() {
//        LinearLayoutManager managerTracks = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
//        mTracksAdapter = new TrackListAdapter(this, getApplicationContext(), mTracks);
//        mTracksView = (RecyclerView) findViewById(R.id.search_tracks_recyclerview);
//        mTracksView.setLayoutManager(managerTracks);
//        mTracksView.setAdapter(mTracksAdapter);

//        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
//        mPlaylistAdapter = new PlaylistListAdapter(null, getApplicationContext(), this, R.layout.item_playlist_short);
//
//        mPlaylistsView = (RecyclerView) findViewById(R.id.search_playlists_recyclerview);
//        mPlaylistsView.setLayoutManager(managerPlaylists);
//        mPlaylistsView.setAdapter(mPlaylistAdapter);
//
//        LinearLayoutManager managerPlaylistsFav = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
//        mFavPlaylistAdapter = new PlaylistListAdapter(null, getApplicationContext(), this, R.layout.item_playlist_short);
//        mFavPlaylistView = (RecyclerView) findViewById(R.id.fav_playlists_recyclerview);
//        mFavPlaylistView.setLayoutManager(managerPlaylistsFav);
//        mFavPlaylistView.setAdapter(mFavPlaylistAdapter);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
            }
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
        } else {
            if (fragment instanceof SearchFragment) {
                return SearchFragment.TAG;
            } else {
                if (fragment instanceof LibraryFragment) {
                    return LibraryFragment.TAG;
                } else {
                    return ProfileFragment.TAG;
                }
            }
        }
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

    }

    @Override
    public void onPlaylistDetails(Fragment fragment, Playlist playlist) {

    }

    @Override
    public void onLibrarySelection(Fragment fragment) {
        replaceFragment(fragment);
    }
}
