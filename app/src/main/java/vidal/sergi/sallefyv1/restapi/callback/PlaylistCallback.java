package vidal.sergi.sallefyv1.restapi.callback;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.model.Playlist;

public interface PlaylistCallback extends FailureCallback {
    void onCreatePlaylistSuccess(Playlist playlist);
    void onCreatePlaylistFailure(Throwable throwable);

    void onAddTrackToPlaylistSuccess(Playlist playlist);
    void onAddTrackToPlaylistFailure(Throwable throwable);

    void onGetPlaylistReceivedSuccess(Playlist playlist);
    void onGetPlaylistReceivedFailure(Throwable throwable);

    void onAllList(ArrayList<Playlist> playlists);

    void onFollowingPlaylist(Playlist playlist);
    void onIsFollowingPlaylist(Playlist playlist);

    void onNoPlaylist(Throwable throwable);
    void onPersonalPlaylistReceived(ArrayList<Playlist> tracks);

    void getFollowingPlayList(ArrayList<Playlist> tracks);



}
