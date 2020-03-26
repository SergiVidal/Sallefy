package vidal.sergi.sallefyv1.restapi.callback;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;

public interface GenreCallback extends FailureCallback {

    void onGenresReceive(ArrayList<Genre> genres);
    void onTracksByGenre(ArrayList<Track> tracks);
}
