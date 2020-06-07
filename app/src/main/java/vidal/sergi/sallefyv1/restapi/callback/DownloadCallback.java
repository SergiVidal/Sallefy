package vidal.sergi.sallefyv1.restapi.callback;

import java.util.List;

import vidal.sergi.sallefyv1.model.Database;
import vidal.sergi.sallefyv1.model.Track;

public interface DownloadCallback extends FailureCallback{
    void onSongDownload(Database database);
    void onSongDownloadFailure(Throwable throwable);


}
