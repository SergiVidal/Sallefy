package vidal.sergi.sallefyv1.controller.music;

import vidal.sergi.sallefyv1.model.Track;

public interface MusicCallback {

    void onMusicPlayerPrepared();
    void onTrackChanged(int index);
}
