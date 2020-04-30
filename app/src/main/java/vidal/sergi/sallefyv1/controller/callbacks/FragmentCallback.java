package vidal.sergi.sallefyv1.controller.callbacks;

import androidx.fragment.app.Fragment;


import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;

public interface FragmentCallback {
    void onChangeFragment(Fragment fragment);

    void onPlaylistDetails(Fragment fragment, Playlist playlist);

    void onUsersDetails(Fragment fragment, User user);

    void onRegisterFragment(Fragment fragment);

    void onLibrarySelection(Fragment fragment);

    void onTrackSelection(Fragment fragment, Track track);


}
