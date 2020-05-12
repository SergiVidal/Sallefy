package vidal.sergi.sallefyv1.controller.callbacks;

import androidx.fragment.app.Fragment;

import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;

public interface SharedCallback {

    void onTrackShared(Fragment fragment, Long id);

    void onPlaylistShared(Fragment fragment, Long id);

    void onUserShared(Fragment fragment, Long id);
}
