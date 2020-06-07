package vidal.sergi.sallefyv1.controller.callbacks;


import vidal.sergi.sallefyv1.model.Track;

public interface TrackListCallback {
    void onTrackSelected(Track track);
    void onTrackSelected(int index, int option);

    void onLikeTrackSelected(int index, int option);
    void onDetailsTrackSelected(int index, int option);
    void onDeleteTrackSelected(int index);
}
