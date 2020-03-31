package vidal.sergi.sallefyv1.restapi.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import retrofit2.http.Query;
import vidal.sergi.sallefyv1.model.Search;

public interface SearchService {
    @GET("search")
    Call<Search> getSearch(@Header("Authorization") String token, @Query("keyword") String keyword);

}
