package vidal.sergi.sallefyv1.restapi.service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vidal.sergi.sallefyv1.model.Track;

public interface TrackService {

    @GET("tracks")
    Call<List<Track>> getAllTracks(@Header("Authorization") String token);

    @GET("me/tracks")
    Call<List<Track>> getOwnTracks(@Header("Authorization") String token);

    @PUT("tracks/{id}/like")
    Call<Track> addLikeTrack(@Header("Authorization") String token, @Path("id") long id);

    @GET("tracks/{id}/like")
    Call<Track> isLikedTrack(@Header("Authorization") String token, @Path("id") long id);

    @POST("tracks")
    Call<ResponseBody> createTrack(@Body Track track, @Header("Authorization") String token);

    @GET("me/tracks/liked")
    Call<List<Track>> getLikedTracks(@Header("Authorization") String token);
}