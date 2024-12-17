package com.example.notionary_v1.interf;

import com.example.notionary_v1.fragments.data.ApiResponse;
import com.example.notionary_v1.fragments.data.ApiResponseReminder;
import com.example.notionary_v1.fragments.data.ApiResponseUsuario;
import com.example.notionary_v1.fragments.data.Reminder;
import com.example.notionary_v1.fragments.data.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioApi {
    @POST("usuarios")
    Call<ApiResponse> createUser(@Body Usuario usuario);

    @GET("configuraciones-usuario/{id}")
    Call<ApiResponseUsuario> getConfiguraciones(@Path("id") int id, @Header("Authorization") String token);

    @PATCH("change-password/{id}")
    Call<ApiResponse> changePassword(@Path("id") int id, @Body Usuario usuario, @Header("Authorization") String token);
}
