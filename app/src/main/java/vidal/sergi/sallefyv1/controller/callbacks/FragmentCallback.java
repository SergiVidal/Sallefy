package vidal.sergi.sallefyv1.controller.callbacks;

import androidx.fragment.app.Fragment;


import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;

public interface FragmentCallback {
    void onChangeFragment(Fragment fragment);

    void onPlaylistDetails(Fragment fragment, Playlist playlist);

    void onUsersDetails(Fragment fragment, User user);

    void onTrackSelection(Fragment fragment, Track track);

    void onGenreSelection(Fragment fragment, Genre genre);
}
