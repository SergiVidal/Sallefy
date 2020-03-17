package vidal.sergi.sallefyv1.restapi.callback;

import vidal.sergi.sallefyv1.model.Playlist;

public interface PlaylistCallback extends FailureCallback {
    void onCreatePlaylistSuccess(Playlist playlist);
    void onCreatePlaylistFailure(Throwable throwable);

    void onAddTrackToPlaylistSuccess(Playlist playlist);
    void onAddTrackToPlaylistFailure(Throwable throwable);

    void onGetPlaylistReceivedSuccess(Playlist playlist);
    void onGetPlaylistReceivedFailure(Throwable throwable);
}
