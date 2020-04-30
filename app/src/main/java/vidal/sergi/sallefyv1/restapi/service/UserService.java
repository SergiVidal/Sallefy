package vidal.sergi.sallefyv1.restapi.service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserRegister;

public interface UserService {

    @GET("users/{login}/playlists")
    Call<List<Playlist>> getUserPlaylists(@Path("login") String login, @Header("Authorization") String token);

    @GET("users/{login}/tracks")
    Call<List<Track>> getUserTracks(@Path("login") String login, @Header("Authorization") String token);

    @GET("users/{login}")
    Call<User> getUserById(@Path("login") String login, @Header("Authorization") String token);

    @GET("users")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @POST("register")
    Call<ResponseBody> registerUser(@Body UserRegister user);

    @GET("me/followings")
    Call<List<User>> getFollowingUsers (@Header("Authorization") String token);
}