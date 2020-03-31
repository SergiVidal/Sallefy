package vidal.sergi.sallefyv1.restapi.callback;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Search;

public interface SearchCallback extends FailureCallback {

    void onGetSearchReceivedSuccess(Search playlist);
    void onGetSearchReceivedFailure(Throwable throwable);
}
