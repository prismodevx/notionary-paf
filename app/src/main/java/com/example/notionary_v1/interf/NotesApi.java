package com.example.notionary_v1.interf;

import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.Note;
import com.example.notionary_v1.fragments.data.NoteLike;
import com.example.notionary_v1.fragments.data.UsuarioNote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotesApi {
    @GET("notas")
    Call<ApiResponse> getNotes(@Query("idUsuario") int idUsuario, @Header("Authorization") String token);

    @POST("notas")
    Call<ApiResponse> createNote(@Body Note note, @Header("Authorization") String token);

    @DELETE("notas/{id}")
    Call<ApiResponse> deleteNote(@Path("id") int id, @Header("Authorization") String token);

    @PUT("notas/{id}")
    Call<ApiResponse> updateNote(@Path("id") int id, @Body Note note, @Header("Authorization") String token);

    @POST("notas-share")
    Call<ApiResponse> shareNote(@Body UsuarioNote usuarioNote, @Header("Authorization") String token);

    @POST("notas-like")
    Call<ApiResponse> likeNote(@Body NoteLike noteLike, @Header("Authorization") String token);
}
