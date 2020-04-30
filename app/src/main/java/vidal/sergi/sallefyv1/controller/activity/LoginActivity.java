package vidal.sergi.sallefyv1.controller.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.fragments.LoginFragment;
import vidal.sergi.sallefyv1.controller.fragments.RegisterFragment;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;

public class LoginActivity extends FragmentActivity implements FragmentCallback {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_login);
        initViews();
        setInitialFragment();
    }

    public void initViews() {
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
    }

    private void setInitialFragment() {
        mTransaction.add(R.id.fragment_container_login, LoginFragment.getInstance());
        mTransaction.commit();
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
                        .replace(R.id.fragment_container_login, currentFragment, fragmentTag)
                        .addToBackStack(null)
                        .commit();

            }
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_login, fragment, fragmentTag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private String getFragmentTag(Fragment fragment) {
        if (fragment instanceof LoginFragment) {
            return LoginFragment.TAG;
        } else {
            return RegisterFragment.TAG;
        }
    }

    public void onChangeFragment(Fragment fragment) {
        replaceFragment(fragment);
    }


    @Override
    public void onPlaylistDetails(Fragment fragment, Playlist playlist) {

    }

    @Override
    public void onUsersDetails(Fragment fragment, User user) {

    }

    @Override
    public void onRegisterFragment(Fragment fragment) {
        replaceFragment(fragment);
    }



    @Override
    public void onTrackSelection(Fragment fragment, Track track) {

    }
}

