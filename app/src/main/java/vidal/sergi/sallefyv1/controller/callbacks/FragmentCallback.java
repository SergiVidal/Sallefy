package vidal.sergi.sallefyv1.controller.callbacks;

import androidx.fragment.app.Fragment;

import vidal.sergi.sallefyv1.model.Playlist;

public interface FragmentCallback {
    void onChangeFragment(Fragment fragment);

    void onPlaylistDetails(Fragment fragment, Playlist playlist);
}
