package com.example.l2_ninalaya_crhistian.interfaz;

import com.example.l2_ninalaya_crhistian.model.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @GET("posts")
    Call<List<Posts>> getPosts();

    @GET("posts/{id}")
    Call<Posts> getPostById(@Path("id") int id);

    @POST("posts")
    Call<Posts> createPost(@Body Posts posts);

    @PUT("posts/{id}")
    Call<Posts> updatePost(@Path("id") int id, @Body Posts posts);

    // DELETE: Eliminar un post (DELETE)
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);

}