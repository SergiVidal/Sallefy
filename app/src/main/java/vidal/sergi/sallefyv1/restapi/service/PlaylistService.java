package vidal.sergi.sallefyv1.restapi.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vidal.sergi.sallefyv1.model.Playlist;

public interface PlaylistService {
    @GET("playlists/{id}")
    Call<Playlist> getPlaylist(@Header("Authorization") String token, @Path("id") long id);

    @POST("playlists")
    Call<Playlist> createPlaylist(@Header("Authorization") String token, @Body Playlist playlist);

    @PUT("playlists")
    Call<Playlist> addTrackToPlaylist(@Header("Authorization") String token, @Body Playlist playlist);
}
