package vidal.sergi.sallefyv1.restapi.service;

import java.util.List;

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
    Call<Playlist> getPlaylist(@Path("id") long id);

    @POST("playlists")
    Call<Playlist> createPlaylist(@Body Playlist playlist);

    @PUT("playlists")
    Call<Playlist> addTrackToPlaylist(@Body Playlist playlist);

    @GET("playlists")
    Call<List<Playlist>> getAllPlaylists();

    @PUT("playlists/{id}/follow")
    Call<Playlist> addFollowPlaylist(@Path("id") long id);

    @GET("playlists/{id}/follow")
    Call<Playlist> isFollowingPlaylist(@Path("id") long id);

    @GET("me/playlists")
    Call<List<Playlist>> getOwnPlayList();

    @GET("me/playlists/following")
    Call<List<Playlist>> getFollowingPlayList();

}
