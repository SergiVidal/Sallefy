package vidal.sergi.sallefyv1.restapi.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vidal.sergi.sallefyv1.model.UserLogin;
import vidal.sergi.sallefyv1.model.UserToken;

public interface UserTokenService {

    @POST("authenticate")
    Call<UserToken> loginUser(@Body UserLogin login);
}
