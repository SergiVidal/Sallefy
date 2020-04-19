package vidal.sergi.sallefyv1.restapi.callback;

import java.util.List;

import vidal.sergi.sallefyv1.model.Track;

public interface TrackCallback extends FailureCallback{
    void onTracksReceived(List<Track> tracks);
    void onNoTracks(Throwable throwable);
    void onPersonalTracksReceived(List<Track> tracks);
    void onUserTracksReceived(List<Track> tracks);

    void onLikedTrack(Track track);
    void onIsLikedTrack(Track track);
    void onCreateTrack();
    void onLikedTracksReceived(List<Track> tracks);
}
